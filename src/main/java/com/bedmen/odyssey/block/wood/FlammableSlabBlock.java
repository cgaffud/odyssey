package com.bedmen.odyssey.block.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FlammableSlabBlock extends SlabBlock {

    public FlammableSlabBlock(Properties p_i48331_1_) {
        super(p_i48331_1_);
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
