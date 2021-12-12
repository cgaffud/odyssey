package com.bedmen.odyssey.entity.item;

import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class FallingHollowCoconut extends FallingBlockEntity {
    public FallingHollowCoconut(EntityType<? extends FallingBlockEntity> p_i50218_1_, Level p_i50218_2_) {
        super(p_i50218_1_, p_i50218_2_);
    }

    public FallingHollowCoconut(Level p_i45848_1_, double p_i45848_2_, double p_i45848_4_, double p_i45848_6_, BlockState p_i45848_8_) {
        super(p_i45848_1_, p_i45848_2_, p_i45848_4_, p_i45848_6_, p_i45848_8_);
    }

    public void tick() {
        List<Player> list = this.level.getEntitiesOfClass(Player.class, this.getBoundingBox());
        for(Player player : list){
            ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
            if(head == ItemStack.EMPTY || head.getItem() == Items.AIR){
                player.setItemSlot(EquipmentSlot.HEAD, ItemRegistry.HOLLOW_COCONUT.get().getDefaultInstance());
                this.discard();
                return;
            }
        }
        super.tick();
    }
}
