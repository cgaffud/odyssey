package com.bedmen.odyssey.mixin;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CCreativeInventoryActionPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetHandler.class)
public abstract class MixinServerPlayNetHandler {

    @Shadow
    public ServerPlayerEntity player;
    @Shadow
    private int dropSpamTickCount;


    public void handleSetCreativeModeSlot(CCreativeInventoryActionPacket p_147344_1_) {
        PacketThreadUtil.ensureRunningOnSameThread(p_147344_1_, (ServerPlayNetHandler)(Object)this, this.player.getLevel());
        if (this.player.gameMode.isCreative()) {
            boolean flag = p_147344_1_.getSlotNum() < 0;
            ItemStack itemstack = p_147344_1_.getItem();
            CompoundNBT compoundnbt = itemstack.getTagElement("BlockEntityTag");
            if (!itemstack.isEmpty() && compoundnbt != null && compoundnbt.contains("x") && compoundnbt.contains("y") && compoundnbt.contains("z")) {
                BlockPos blockpos = new BlockPos(compoundnbt.getInt("x"), compoundnbt.getInt("y"), compoundnbt.getInt("z"));
                TileEntity tileentity = this.player.level.getBlockEntity(blockpos);
                if (tileentity != null) {
                    CompoundNBT compoundnbt1 = tileentity.save(new CompoundNBT());
                    compoundnbt1.remove("x");
                    compoundnbt1.remove("y");
                    compoundnbt1.remove("z");
                    itemstack.addTagElement("BlockEntityTag", compoundnbt1);
                }
            }

            boolean flag1 = p_147344_1_.getSlotNum() >= 1 && p_147344_1_.getSlotNum() <= 46;
            boolean flag2 = itemstack.isEmpty() || itemstack.getDamageValue() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty();
            if (flag1 && flag2) {
                if (itemstack.isEmpty()) {
                    this.player.inventoryMenu.setItem(p_147344_1_.getSlotNum(), ItemStack.EMPTY);
                } else {
                    this.player.inventoryMenu.setItem(p_147344_1_.getSlotNum(), itemstack);
                }

                this.player.inventoryMenu.setSynched(this.player, true);
                this.player.inventoryMenu.broadcastChanges();
            } else if (flag && flag2 && this.dropSpamTickCount < 200) {
                this.dropSpamTickCount += 20;
                this.player.drop(itemstack, true);
            }
        }

    }

}
