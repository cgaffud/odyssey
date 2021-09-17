package com.bedmen.odyssey.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
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
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, Integer.valueOf(7)).setValue(PERSISTENT, Boolean.valueOf(false)));
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
        if (!blockState.getValue(PERSISTENT) && blockState.getValue(DISTANCE) == 7) {
            dropResources(blockState, serverWorld, blockPos);
            System.out.println("dropped");
            serverWorld.removeBlock(blockPos, false);
        }

    }

    @Override
    public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
        p_225534_2_.setBlock(p_225534_3_, updateDistance(p_225534_1_, p_225534_2_, p_225534_3_), 3);
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

    // Distance now should check diagonals
    private static BlockState updateDistance(BlockState blockState, IWorld world, BlockPos blockPos) {
        int i = 7;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(Direction direction : Direction.values()) {
            blockpos$mutable.setWithOffset(blockPos, direction);
            i = Math.min(i, getDistanceAt(world.getBlockState(blockpos$mutable)) + 1);
            if (i == 1) break;

            if(direction.getAxis().getPlane() == Direction.Plane.HORIZONTAL){
                System.out.println("diagonal checked:");
                System.out.print(blockPos);
                blockpos$mutable.move(direction.getCounterClockWise());
                System.out.print(",");
                i = Math.min(i, getDistanceAt(world.getBlockState(blockpos$mutable)) + 1);
                System.out.print(",");
                System.out.print( getDistanceAt(world.getBlockState(blockpos$mutable)) + 1);
                System.out.print(",");
                System.out.print(i);
                System.out.print("\n");
                if (i == 1) break;
            }
        }

        System.out.println(i);
        return blockState.setValue(DISTANCE, Integer.valueOf(i));
    }

    private static int getDistanceAt(BlockState blockState) {
        if (BlockTags.LOGS.contains(blockState.getBlock())) {
            System.out.println("found log");
            return 0;
        } else {
            return (blockState.getBlock() instanceof DiagonalLeavesBlock) || (blockState.getBlock() instanceof LeavesBlock) ? blockState.getValue(DISTANCE) : 7;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return updateDistance(this.defaultBlockState().setValue(PERSISTENT, Boolean.valueOf(true)), p_196258_1_.getLevel(), p_196258_1_.getClickedPos());
    }

}
