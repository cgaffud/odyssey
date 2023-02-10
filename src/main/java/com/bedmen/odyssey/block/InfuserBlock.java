package com.bedmen.odyssey.block;


import com.bedmen.odyssey.block.entity.InfuserBlockEntity;
import com.bedmen.odyssey.block.entity.InfusionPedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InfuserBlock extends InfusionPedestalBlock {

    protected static final VoxelShape BASE_1 = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 2.0d, 16.0d);
    protected static final VoxelShape BASE_2 = Block.box(2.0d, 2.0d, 2.0d, 14.0d, 3.0d, 14.0d);
    protected static final VoxelShape TOP = Block.box(2.0d, 14.0d, 2.0d, 14.0d, 15.0d, 14.0d);
    protected static final VoxelShape SOUTH_TOP_PIECE = Block.box(3.0d, 11.0d, 0.0d, 13.0d, 15.0d, 2.0d);
    protected static final VoxelShape NORTH_TOP_PIECE = Block.box(3.0d, 11.0d, 14.0d, 13.0d, 15.0d, 16.0d);
    protected static final VoxelShape WEST_TOP_PIECE = Block.box(0.0d, 11.0d, 3.0d, 2.0d, 15.0d, 13.0d);
    protected static final VoxelShape EAST_TOP_PIECE = Block.box(14.0d, 11.0d, 3.0d, 16.0d, 15.0d, 13.0d);
    protected static final VoxelShape NORTH_WEST_CORNER = Block.box(0.0d, 11.0d, 0.0d, 3.0d, 16.0d, 3.0d);
    protected static final VoxelShape SOUTH_WEST_CORNER = Block.box(0.0d, 11.0d, 13.0d, 3.0d, 16.0d, 16.0d);
    protected static final VoxelShape SOUTH_EAST_CORNER = Block.box(13.0d, 11.0d, 13.0d, 16.0d, 16.0d, 16.0d);
    protected static final VoxelShape NORTH_EAST_CORNER = Block.box(13.0d, 11.0d, 0.0d, 16.0d, 16.0d, 3.0d);
    protected static final VoxelShape BOTTOM_TOP = Block.box(4.0d, 13.0d, 4.0d, 12.0d, 14.0d, 12.0d);
    protected static final VoxelShape CENTER = Block.box(6.0d, 4.0d, 6.0d, 10.0d, 13.0d, 10.0d);
    protected static final VoxelShape WEST_CENTER_PIECE = Block.box(5.0d, 3.0d, 7.0d, 6.0d, 12.0d, 9.0d);
    protected static final VoxelShape EAST_CENTER_PIECE = Block.box(10.0d, 3.0d, 7.0d, 11.0d, 12.0d, 9.0d);
    protected static final VoxelShape SOUTH_CENTER_PIECE = Block.box(7.0d, 3.0d, 5.0d, 9.0d, 12.0d, 6.0d);
    protected static final VoxelShape NORTH_CENTER_PIECE = Block.box(7.0d, 3.0d, 10.0d, 9.0d, 12.0d, 11.0d);
    protected static final VoxelShape VOXEL_SHAPE = Shapes.or(BASE_1, BASE_2, CENTER, WEST_CENTER_PIECE, EAST_CENTER_PIECE, NORTH_CENTER_PIECE, SOUTH_CENTER_PIECE, TOP, SOUTH_TOP_PIECE, NORTH_TOP_PIECE, WEST_TOP_PIECE, EAST_TOP_PIECE, NORTH_EAST_CORNER, SOUTH_EAST_CORNER, SOUTH_WEST_CORNER, NORTH_WEST_CORNER, BOTTOM_TOP);

    public InfuserBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VOXEL_SHAPE;
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfuserBlockEntity(blockPos, blockState);
    }
}
