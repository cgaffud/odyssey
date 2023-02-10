package com.bedmen.odyssey.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InfusionPedestalBlock extends Block {

    public static final VoxelShape BASE_1 = Block.box(1.0d, 0.0d, 1.0d, 15.0d, 2.0d, 15.0d);
    public static final VoxelShape BASE_2 = Block.box(3.0d, 2.0d, 3.0d, 13.0d, 3.0d, 13.0d);
    public static final VoxelShape CENTER = Block.box(6.0d, 3.0d, 6.0d, 10.0d, 13.0d, 10.0d);
    public static final VoxelShape WEST_CENTER_PIECE = Block.box(5.0d, 3.0d, 7.0d, 6.0d, 10.0d, 9.0d);
    public static final VoxelShape EAST_CENTER_PIECE = Block.box(10.0d, 3.0d, 7.0d, 11.0d, 10.0d, 9.0d);
    public static final VoxelShape NORTH_CENTER_PIECE = Block.box(7.0d, 3.0d, 10.0d, 9.0d, 10.0d, 11.0d);
    public static final VoxelShape SOUTH_CENTER_PIECE = Block.box(7.0d, 3.0d, 5.0d, 9.0d, 10.0d, 6.0d);
    public static final VoxelShape TOP = Block.box(3.0d, 13.0d, 3.0d, 13.0d, 15.0d, 13.0d);
    public static final VoxelShape SOUTH_TOP_PIECE = Block.box(1.0d, 11.0d, 1.0d, 15.0d, 15.0d, 3.0d);
    public static final VoxelShape NORTH_TOP_PIECE = Block.box(1.0d, 11.0d, 13.0d, 15.0d, 15.0d, 15.0d);
    public static final VoxelShape WEST_TOP_PIECE = Block.box(1.0d, 11.0d, 3.0d, 3.0d, 15.0d, 13.0d);
    public static final VoxelShape EAST_TOP_PIECE = Block.box(13.0d, 11.0d, 3.0d, 15.0d, 15.0d, 13.0d);
    public static final VoxelShape VOXEL_SHAPE = Shapes.or(BASE_1, BASE_2, CENTER, WEST_CENTER_PIECE, EAST_CENTER_PIECE, NORTH_CENTER_PIECE, SOUTH_CENTER_PIECE, TOP, SOUTH_TOP_PIECE, NORTH_TOP_PIECE, WEST_TOP_PIECE, EAST_TOP_PIECE);

    public InfusionPedestalBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VOXEL_SHAPE;
    }
}
