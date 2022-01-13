package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;

public class TreasureChestBlockEntity extends ChestBlockEntity {
    protected TreasureChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos blockPos1, BlockState blockState1) {
                TreasureChestBlockEntity.playSound(level, blockPos1, SoundEvents.CHEST_OPEN);
            }

            protected void onClose(Level level, BlockPos blockPos1, BlockState blockState1) {
                TreasureChestBlockEntity.playSound(level, blockPos1, SoundEvents.CHEST_CLOSE);
            }

            protected void openerCountChanged(Level level, BlockPos blockPos1, BlockState blockState1, int p_155364_, int p_155365_) {
                TreasureChestBlockEntity.this.signalOpenCount(level, blockPos1, blockState1, p_155364_, p_155365_);
            }

            protected boolean isOwnContainer(Player p_155355_) {
                if (!(p_155355_.containerMenu instanceof ChestMenu)) {
                    return false;
                } else {
                    Container container = ((ChestMenu)p_155355_.containerMenu).getContainer();
                    return container == TreasureChestBlockEntity.this || container instanceof CompoundContainer && ((CompoundContainer)container).contains(TreasureChestBlockEntity.this);
                }
            }
        };
    }

    public TreasureChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BlockEntityTypeRegistry.TREASURE_CHEST.get(), blockPos, blockState);
    }

    static void playSound(Level level, BlockPos blockPos, SoundEvent soundEvent) {
        double d0 = (double)blockPos.getX() + 0.5D;
        double d1 = (double)blockPos.getY() + 0.5D;
        double d2 = (double)blockPos.getZ() + 0.5D;

        level.playSound((Player)null, d0, d1, d2, soundEvent, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }
}
