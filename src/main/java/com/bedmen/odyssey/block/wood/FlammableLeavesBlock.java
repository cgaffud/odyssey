package com.bedmen.odyssey.block.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FlammableLeavesBlock extends LeavesBlock {
    public FlammableLeavesBlock(Properties properties) {
        super(properties);
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
    {
        return 30;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
    {
        return 60;
    }
}
