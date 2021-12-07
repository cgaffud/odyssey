package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.block.INeedsToRegisterRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class DiagonalLeavesBlock extends LeavesBlock implements INeedsToRegisterRenderType {
    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    public DiagonalLeavesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(DISTANCE, 7).setValue(PERSISTENT, Boolean.FALSE));
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter iBlockReader, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(DISTANCE) == 7 && !blockState.getValue(PERSISTENT);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverWorld, BlockPos blockPos, Random random) {
        BlockState newBlockState = updateDistance(blockState, serverWorld, blockPos);
        serverWorld.setBlock(blockPos, newBlockState, 3);
        if (!newBlockState.getValue(PERSISTENT) && newBlockState.getValue(DISTANCE) == 7) {
            dropResources(newBlockState, serverWorld, blockPos);
            serverWorld.removeBlock(blockPos, false);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverWorld, BlockPos blockPos, Random random) {
        serverWorld.setBlock(blockPos, updateDistance(blockState, serverWorld, blockPos), 3);
    }

    @Override
    public int getLightBlock(BlockState p_200011_1_, BlockGetter p_200011_2_, BlockPos p_200011_3_) {
        return 1;
    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        int i = getDistanceAt(p_196271_3_) + 1;
        if (i != 1 || p_196271_1_.getValue(DISTANCE) != i) {
            p_196271_4_.getBlockTicks().scheduleTick(p_196271_5_, this, 1);
        }
        return p_196271_1_;
    }

    private static BlockState updateDistance(BlockState blockState, LevelAccessor world, BlockPos blockPos) {
        int i = 7;
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

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
        return blockState.setValue(DISTANCE, i);
    }

    private static int getDistanceAt(BlockState blockState) {
        if (BlockTags.LOGS.contains(blockState.getBlock())) {
            return 0;
        } else {
            return (blockState.getBlock() instanceof DiagonalLeavesBlock) || (blockState.getBlock() instanceof LeavesBlock) ? blockState.getValue(DISTANCE) : 7;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(DISTANCE, PERSISTENT);
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face)
    {
        return 30;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face)
    {
        return 60;
    }

    public RenderType getRenderType() {
        return RenderType.cutoutMipped();
    }
}
