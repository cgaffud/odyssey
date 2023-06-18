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
    public FallingHollowCoconut(EntityType<? extends FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FallingHollowCoconut(Level level, double x, double y, double z, BlockState blockState) {
        super(level, x, y, z, blockState);
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
