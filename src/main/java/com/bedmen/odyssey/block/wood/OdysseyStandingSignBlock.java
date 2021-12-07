//package com.bedmen.odyssey.block.wood;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.material.FluidState;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.block.state.properties.IntegerProperty;
//import net.minecraft.world.level.block.state.StateDefinition;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.block.Mirror;
//import net.minecraft.world.level.block.Rotation;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.Mth;
//import net.minecraft.world.level.LevelAccessor;
//import net.minecraft.world.level.LevelReader;
//
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.WoodType;
//
//import javax.annotation.Nullable;
//
//public class OdysseyStandingSignBlock extends AbstractOdysseySignBlock {
//    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
//
//    public OdysseyStandingSignBlock(BlockBehaviour.Properties p_i225764_1_, WoodType p_i225764_2_) {
//        super(p_i225764_1_, p_i225764_2_);
//        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, Integer.valueOf(0)).setValue(WATERLOGGED, Boolean.valueOf(false)));
//    }
//
//    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
//        return p_196260_2_.getBlockState(p_196260_3_.below()).getMaterial().isSolid();
//    }
//
//    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
//        FluidState fluidstate = p_196258_1_.getLevel().getFluidState(p_196258_1_.getClickedPos());
//        return this.defaultBlockState().setValue(ROTATION, Integer.valueOf(Mth.floor((double)((180.0F + p_196258_1_.getRotation()) * 16.0F / 360.0F) + 0.5D) & 15)).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
//    }
//
//    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
//        return p_196271_2_ == Direction.DOWN && !this.canSurvive(p_196271_1_, p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
//    }
//
//    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
//        return p_185499_1_.setValue(ROTATION, Integer.valueOf(p_185499_2_.rotate(p_185499_1_.getValue(ROTATION), 16)));
//    }
//
//    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
//        return p_185471_1_.setValue(ROTATION, Integer.valueOf(p_185471_2_.mirror(p_185471_1_.getValue(ROTATION), 16)));
//    }
//
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
//        p_206840_1_.add(ROTATION, WATERLOGGED);
//    }
//
//    @Nullable
//    @Override
//    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
//        return new OdysseySignBlockEntity();
//    }
//}