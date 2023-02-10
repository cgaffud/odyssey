package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class InfuserBlockEntity extends InfusionPedestalBlockEntity {
    public InfuserBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.INFUSER.get(), blockPos, blockState);
    }
}
