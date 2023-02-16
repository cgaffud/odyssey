package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CovenHutDoorBlockEntity extends BlockEntity {
    public CovenHutDoorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.COVEN_HUT_DOOR.get(), blockPos, blockState);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, CovenHutDoorBlockEntity covenHutDoorBlockEntity) {

    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CovenHutDoorBlockEntity covenHutDoorBlockEntity) {

    }
}
