//package com.bedmen.odyssey.block.wood;
//
//import com.google.common.collect.ImmutableMap;
//import com.google.common.collect.Maps;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.LevelAccessor;
//import net.minecraft.world.level.LevelReader;
//import net.minecraft.world.level.block.*;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.StateDefinition;
//import net.minecraft.world.level.block.state.properties.DirectionProperty;
//import net.minecraft.world.level.block.state.properties.WoodType;
//import net.minecraft.world.level.material.FluidState;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraft.world.phys.shapes.CollisionContext;
//import net.minecraft.world.phys.shapes.VoxelShape;
//
//import javax.annotation.Nullable;
//import java.util.Map;
//
//public class OdysseyWallSignBlock extends OdysseySignBlock {
//    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
//    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D), Direction.SOUTH, Block.box(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D), Direction.EAST, Block.box(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D), Direction.WEST, Block.box(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D)));
//
//    public OdysseyWallSignBlock(BlockBehaviour.Properties p_i225766_1_, WoodType p_i225766_2_) {
//        super(p_i225766_1_, p_i225766_2_);
//        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
//    }
//
//    public String getDescriptionId() {
//        return this.asItem().getDescriptionId();
//    }
//
//    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
//        return AABBS.get(p_220053_1_.getValue(FACING));
//    }
//
//    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
//        return p_196260_2_.getBlockState(p_196260_3_.relative(p_196260_1_.getValue(FACING).getOpposite())).getMaterial().isSolid();
//    }
//
//    @Nullable
//    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
//        BlockState blockstate = this.defaultBlockState();
//        FluidState fluidstate = p_196258_1_.getLevel().getFluidState(p_196258_1_.getClickedPos());
//        LevelReader iworldreader = p_196258_1_.getLevel();
//        BlockPos blockpos = p_196258_1_.getClickedPos();
//        Direction[] adirection = p_196258_1_.getNearestLookingDirections();
//
//        for(Direction direction : adirection) {
//            if (direction.getAxis().isHorizontal()) {
//                Direction direction1 = direction.getOpposite();
//                blockstate = blockstate.setValue(FACING, direction1);
//                if (blockstate.canSurvive(iworldreader, blockpos)) {
//                    return blockstate.setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
//                }
//            }
//        }
//
//        return null;
//    }
//
//    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
//        return p_196271_2_.getOpposite() == p_196271_1_.getValue(FACING) && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
//    }
//
//    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
//        return p_185499_1_.setValue(FACING, p_185499_2_.rotate(p_185499_1_.getValue(FACING)));
//    }
//
//    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
//        return p_185471_1_.rotate(p_185471_2_.getRotation(p_185471_1_.getValue(FACING)));
//    }
//
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
//        p_206840_1_.add(FACING, WATERLOGGED);
//    }
//
//    @Nullable
//    @Override
//    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
//        return new OdysseySignBlockEntity(blockPos, blockState);
//    }
//}