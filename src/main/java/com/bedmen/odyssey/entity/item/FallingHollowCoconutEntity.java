package com.bedmen.odyssey.entity.item;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import java.util.List;

public class FallingHollowCoconutEntity extends FallingBlockEntity {
    public FallingHollowCoconutEntity(EntityType<? extends FallingBlockEntity> p_i50218_1_, World p_i50218_2_) {
        super(p_i50218_1_, p_i50218_2_);
    }

    public FallingHollowCoconutEntity(World p_i45848_1_, double p_i45848_2_, double p_i45848_4_, double p_i45848_6_, BlockState p_i45848_8_) {
        super(p_i45848_1_, p_i45848_2_, p_i45848_4_, p_i45848_6_, p_i45848_8_);
    }

    public void tick() {
        List<PlayerEntity> list = this.level.getEntitiesOfClass(PlayerEntity.class, this.getBoundingBox());
        for(PlayerEntity player : list){
            ItemStack head = player.getItemBySlot(EquipmentSlotType.HEAD);
            if(head == ItemStack.EMPTY || head.getItem() == Items.AIR){
                player.setItemSlot(EquipmentSlotType.HEAD, ItemRegistry.HOLLOW_COCONUT.get().getDefaultInstance());
                this.remove();
                return;
            }
        }
        super.tick();
    }
}
