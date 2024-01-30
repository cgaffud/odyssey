package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.block.entity.GraveBlockEntity;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.util.GeneralUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Inventory.class)
public abstract class MixinInventory implements Container, Nameable {
    @Shadow public final Player player;
    @Shadow public final NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);
    @Shadow public final NonNullList<ItemStack> armor = NonNullList.withSize(4, ItemStack.EMPTY);
    @Shadow public final NonNullList<ItemStack> offhand = NonNullList.withSize(1, ItemStack.EMPTY);
    @Shadow private final List<NonNullList<ItemStack>> compartments = ImmutableList.of(this.items, this.armor, this.offhand);


    public MixinInventory(Player p_35983_) {
        this.player = p_35983_;
    }

    // This function is called on player death whenever keepInventory is not turned on. We convert it to now check if the item is soulbound.
    // If so, it is returned to player with appropriate damage dealt. Then we check if player produces grave on death. If not, drop item.
    // Soulbound items get transferred to new player in PlayerEvents#onPlayerEventClone
    public void dropAll() {
        BlockEntity blockEntity = null;
        if (AspectUtil.getBuffAspectStrength(this.player, Aspects.GRAVE) && !GeneralUtil.isActuallyEmpty(this.compartments)) {
            this.player.level.setBlock(this.player.blockPosition(), BlockRegistry.GRAVE.get().defaultBlockState(), 2);
            blockEntity = this.player.level.getBlockEntity(this.player.blockPosition());
        }

        int totalTicker = 0;
        for(List<ItemStack> list : this.compartments) {
            for (int i = 0; i < list.size(); i++) {
                ItemStack itemstack = list.get(i);
                float soulboundAmount = itemstack.isEmpty() ? 0 : AspectUtil.getItemStackAspectStrength(itemstack, Aspects.SOULBOUND);
                if (soulboundAmount > 0) {
                    int soulboundPenality = itemstack.getMaxDamage() / ((int) soulboundAmount + 1);
                    // This should be a safe cast - fn only runs serverside
                    itemstack.hurt(soulboundPenality, this.player.getRandom(), (ServerPlayer)this.player);
                } else {
                    // Sanity check
                    if (blockEntity instanceof GraveBlockEntity graveBlockEntity) {
                        graveBlockEntity.setItem(totalTicker, itemstack);
                    } else {
                        this.player.drop(itemstack, true, false);
                    }
                    list.set(i, ItemStack.EMPTY);
                }
                totalTicker++;
            }
        }
    }
}
