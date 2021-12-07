package com.bedmen.odyssey.block.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CornerLeavesBlock extends LeavesBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE_NORTH_EAST = Shapes.or(Block.box(14,0,0,16,16,2));
    public static final VoxelShape SHAPE_EAST_SOUTH = Shapes.or(Block.box(14,0,14,16,16,16));
    public static final VoxelShape SHAPE_SOUTH_WEST = Shapes.or(Block.box(0,0,14,2,16,16));
    public static final VoxelShape SHAPE_WEST_NORTH = Shapes.or(Block.box(0,0,0,2,16,2));


    public CornerLeavesBlock(Properties p_54422_) {
        super(p_54422_);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return updateDistance(this.defaultBlockState().setValue(PERSISTENT, Boolean.TRUE), blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos()).setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch ((Direction) blockState.getValue(FACING)) {
            default -> SHAPE_NORTH_EAST;
            case SOUTH -> SHAPE_SOUTH_WEST;
            case EAST -> SHAPE_EAST_SOUTH;
            case WEST -> SHAPE_WEST_NORTH;
        };
    }

}
