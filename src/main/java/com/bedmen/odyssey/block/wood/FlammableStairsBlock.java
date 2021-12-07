package com.bedmen.odyssey.block.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class FlammableStairsBlock extends StairBlock {
    public FlammableStairsBlock(Supplier<BlockState> state, Properties properties) {
        super(state, properties);
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
