package com.bedmen.odyssey.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import java.util.Random;

public class DiagonalLeavesBlock extends LeavesBlock {
    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    public DiagonalLeavesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, 7).setValue(PERSISTENT, Boolean.FALSE));
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, IBlockReader iBlockReader, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(DISTANCE) == 7 && !blockState.getValue(PERSISTENT);
    }

    @Override
    public void randomTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        BlockState newBlockState = updateDistance(blockState, serverWorld, blockPos);
        serverWorld.setBlock(blockPos, newBlockState, 3);
        if (!newBlockState.getValue(PERSISTENT) && newBlockState.getValue(DISTANCE) == 7) {
            dropResources(newBlockState, serverWorld, blockPos);
            serverWorld.removeBlock(blockPos, false);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        serverWorld.setBlock(blockPos, updateDistance(blockState, serverWorld, blockPos), 3);
    }

    @Override
    public int getLightBlock(BlockState p_200011_1_, IBlockReader p_200011_2_, BlockPos p_200011_3_) {
        return 1;
    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        int i = getDistanceAt(p_196271_3_) + 1;
        if (i != 1 || p_196271_1_.getValue(DISTANCE) != i) {
            p_196271_4_.getBlockTicks().scheduleTick(p_196271_5_, this, 1);
        }
        return p_196271_1_;
    }

    private static BlockState updateDistance(BlockState blockState, IWorld world, BlockPos blockPos) {
        int i = 7;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(Direction direction : Direction.values()) {
            blockpos$mutable.setWithOffset(blockPos, direction);
            i = Math.min(i, getDistanceAt(world.getBlockState(blockpos$mutable)) + 1);
            if(direction.getAxis().isHorizontal()){
                blockpos$mutable.setWithOffset(blockpos$mutable, direction.getClockWise());
                i = Math.min(i, getDistanceAt(world.getBlockState(blockpos$mutable)) + 1);
            }
            if (i == 1) {
                break;
            }
        }
        if(blockState.getValue(DISTANCE) != i){
            for(Direction direction : Direction.values()) {
                if (direction.getAxis().isHorizontal()) {
                    blockpos$mutable.setWithOffset(blockPos, direction);
                    blockpos$mutable.setWithOffset(blockpos$mutable, direction.getClockWise());
                    world.getBlockTicks().scheduleTick(blockpos$mutable, blockState.getBlock(), 1);
                }
            }
        }
        return blockState.setValue(DISTANCE, i);
    }

    private static int getDistanceAt(BlockState blockState) {
        if (BlockTags.LOGS.contains(blockState.getBlock())) {
            return 0;
        } else {
            return (blockState.getBlock() instanceof DiagonalLeavesBlock) || (blockState.getBlock() instanceof LeavesBlock) ? blockState.getValue(DISTANCE) : 7;
        }
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(DISTANCE, PERSISTENT);
    }
}
