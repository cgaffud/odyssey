package com.bedmen.odyssey.block.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class SandSaplingBlock extends SaplingBlock {
    public SandSaplingBlock(AbstractTreeGrower p_i48337_1_, Properties p_i48337_2_) {
        super(p_i48337_1_, p_i48337_2_);
    }

    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockReader, BlockPos blockPos) {
        return blockState.is(Blocks.SAND) || super.mayPlaceOn(blockState, blockReader, blockPos);
    }
}
