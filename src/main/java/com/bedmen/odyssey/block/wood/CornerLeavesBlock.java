package com.bedmen.odyssey.block.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class CornerLeavesBlock extends LeavesBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE_NORTH_EAST = Shapes.or(Block.box(8,0,0,16,16,8));
    public static final VoxelShape SHAPE_EAST_SOUTH = Shapes.or(Block.box(8,0,8,16,16,16));
    public static final VoxelShape SHAPE_SOUTH_WEST = Shapes.or(Block.box(0,0,8,8,16,16));
    public static final VoxelShape SHAPE_WEST_NORTH = Shapes.or(Block.box(0,0,0,8,16,8));


    public CornerLeavesBlock(Properties p_54422_) {
        super(p_54422_);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockPos blockpos = blockPlaceContext.getClickedPos();
        Vec3 vec3 = blockPlaceContext.getClickLocation();
        double x = vec3.x - (double)blockpos.getX();
        double z = vec3.z - (double)blockpos.getZ();
        Direction facingDirection;
        if (z > 0.5) {
            if (x > 0.5) {
                facingDirection = Direction.EAST;
            } else {
                facingDirection = Direction.SOUTH;
            }
        } else {
            if (x > 0.5) {
                facingDirection = Direction.NORTH;
            } else {
                facingDirection = Direction.WEST;
            }
        }
        return updateDistance(this.defaultBlockState().setValue(PERSISTENT, Boolean.TRUE), blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos()).setValue(FACING, facingDirection);
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch ((Direction) blockState.getValue(FACING)) {
            default -> SHAPE_NORTH_EAST;
            case SOUTH -> SHAPE_SOUTH_WEST;
            case EAST -> SHAPE_EAST_SOUTH;
            case WEST -> SHAPE_WEST_NORTH;
        };
    }

    public void tick(BlockState p_54426_, ServerLevel p_54427_, BlockPos p_54428_, Random p_54429_) {
        p_54427_.setBlock(p_54428_, updateDistance(p_54426_, p_54427_, p_54428_), 3);
    }

    public static BlockState updateDistance(BlockState p_54436_, LevelAccessor p_54437_, BlockPos p_54438_) {
        int i = 7;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(p_54438_, direction);
            i = Math.min(i, getDistanceAt(p_54437_.getBlockState(blockpos$mutableblockpos)));
            if (i < 2) {
                break;
            }
        }

        i = Math.max(1, i);

        System.out.println(i);
        return p_54436_.setValue(DISTANCE, i);
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
