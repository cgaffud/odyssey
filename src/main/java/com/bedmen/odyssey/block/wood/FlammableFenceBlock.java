package com.bedmen.odyssey.block.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class FlammableFenceBlock extends FenceBlock {
    public FlammableFenceBlock(Properties p_i48399_1_) {
        super(p_i48399_1_);
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
    {
        return 5;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
    {
        return 20;
    }
}
