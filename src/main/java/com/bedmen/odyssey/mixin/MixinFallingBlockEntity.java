package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FallingBlockEntity.class)
public abstract class MixinFallingBlockEntity extends Entity {

    @Shadow
    private BlockState blockState;

    public MixinFallingBlockEntity(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void startTick(CallbackInfo ci) {
        if(this.blockState.getBlock() == BlockRegistry.HOLLOW_COCONUT.get()){
            List<PlayerEntity> list = this.level.getEntitiesOfClass(PlayerEntity.class, this.getBoundingBox());
            for(PlayerEntity player : list){
                ItemStack head = player.getItemBySlot(EquipmentSlotType.HEAD);
                if(head == ItemStack.EMPTY || head.getItem() == Items.AIR){
                    player.setItemSlot(EquipmentSlotType.HEAD, ItemRegistry.HOLLOW_COCONUT.get().getDefaultInstance());
                    this.remove();
                    return;
                }
            }
        }
    }
}
