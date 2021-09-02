package com.bedmen.odyssey.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class DiagonalLeavesBlock extends LeavesBlock {
    public DiagonalLeavesBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
        p_225534_2_.setBlock(p_225534_3_, updateDistance(p_225534_1_, p_225534_2_, p_225534_3_), 3);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return updateDistance(this.defaultBlockState().setValue(PERSISTENT, Boolean.valueOf(true)), p_196258_1_.getLevel(), p_196258_1_.getClickedPos());
    }

    // Distance now should check diagonals
    private static BlockState updateDistance(BlockState blockState, IWorld world, BlockPos blockPos) {
        int i = 7;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(Direction direction : Direction.values()) {
            if(direction.getAxis().getPlane() == Direction.Plane.HORIZONTAL)

            blockpos$mutable.setWithOffset(blockPos, direction);
            i = Math.min(i, getDistanceAt(world.getBlockState(blockpos$mutable)) + 1);
            if (i == 1) break;

            if(direction.getAxis().getPlane() == Direction.Plane.HORIZONTAL){
                blockpos$mutable.move(direction.getClockWise());
                i = Math.min(i, getDistanceAt(world.getBlockState(blockpos$mutable)) + 1);
                if (i == 1) break;
            }
        }

        return blockState.setValue(DISTANCE, Integer.valueOf(i));
    }

    private static int getDistanceAt(BlockState blockState) {
        if (BlockTags.LOGS.contains(blockState.getBlock())) {
            return 0;
        } else {
            return (blockState.getBlock() instanceof DiagonalLeavesBlock) || (blockState.getBlock() instanceof LeavesBlock) ? blockState.getValue(DISTANCE) : 7;
        }
    }
}
