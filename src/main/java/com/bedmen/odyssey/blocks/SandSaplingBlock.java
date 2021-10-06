package com.bedmen.odyssey.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class SandSaplingBlock extends SaplingBlock {
    public SandSaplingBlock(Tree p_i48337_1_, Properties p_i48337_2_) {
        super(p_i48337_1_, p_i48337_2_);
    }

    protected boolean mayPlaceOn(BlockState blockState, IBlockReader blockReader, BlockPos blockPos) {
        return blockState.is(Blocks.SAND) || super.mayPlaceOn(blockState, blockReader, blockPos);
    }
}
