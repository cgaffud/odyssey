package com.bedmen.odyssey.block;

import com.bedmen.odyssey.entity.boss.AbandonedIronGolem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class AbandonedIronGolemBlock extends Block {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape SHAPE_UPPER_NORTH = Shapes.join(box(3,-0.5,4.5,13,5.5,11.5),box(-1,5,2,17,17,13), BooleanOp.OR);
    protected static final VoxelShape SHAPE_UPPER_EAST = Shapes.join(box(4.5,-0.5,4,11.5,5.5,13),box(3,5,-1,14,17,17), BooleanOp.OR);
    protected static final VoxelShape SHAPE_UPPER_SOUTH = Shapes.join(box(3,-0.5,4.5,13,5.5,11.5),box(-1,5,3,17,17,14), BooleanOp.OR);
    protected static final VoxelShape SHAPE_UPPER_WEST = Shapes.join(box(4.5,-0.5,3,11.5,5.5,13),box(2,5,-1,13,17,17), BooleanOp.OR);
    protected static final VoxelShape SHAPE_LOWER_SOUTH = Shapes.join(box(9.5,0,6,15.5,16,11),box(0.5,0,6,6.5,16,11), BooleanOp.OR);
    protected static final VoxelShape SHAPE_LOWER_WEST = Shapes.join(box(5,0,9.5,10,16,15.5),box(5,0,0.5,10,16,6.5), BooleanOp.OR);
    protected static final VoxelShape SHAPE_LOWER_NORTH = Shapes.join(box(9.5,0,5,15.5,16,10),box(0.5,0,5,6.5,16,10), BooleanOp.OR);
    protected static final VoxelShape SHAPE_LOWER_EAST = Shapes.join(box(6,0,9.5,11,16,15.5),box(6,0,0.5,11,16,6.5), BooleanOp.OR);

    public AbandonedIronGolemBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(FACING, Direction.NORTH));
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        if(blockState.getValue(HALF) == DoubleBlockHalf.UPPER){
            switch(blockState.getValue(FACING)){
                case EAST: return SHAPE_UPPER_EAST;
                case SOUTH: return SHAPE_UPPER_SOUTH;
                case WEST: return SHAPE_UPPER_WEST;
                default: return SHAPE_UPPER_NORTH;
            }
        }
        switch(blockState.getValue(FACING)){
            case EAST: return SHAPE_LOWER_EAST;
            case SOUTH: return SHAPE_LOWER_SOUTH;
            case WEST: return SHAPE_LOWER_WEST;
            default: return SHAPE_LOWER_NORTH;
        }
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);
        if (pFacing.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (pFacing == Direction.UP) || pFacingState.is(this) && pFacingState.getValue(HALF) != doubleblockhalf) {
            return doubleblockhalf == DoubleBlockHalf.LOWER && pFacing == Direction.DOWN && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockState =  blockpos.getY() < 255 && pContext.getLevel().getBlockState(blockpos.above()).canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
        return blockState.setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        pLevel.setBlock(pPos.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER).setValue(FACING, pState.getValue(FACING)), 3);
    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        if (pState.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(pState, pLevel, pPos);
        } else {
            BlockState blockstate = pLevel.getBlockState(pPos.below());
            if (pState.getBlock() != this) return super.canSurvive(pState, pLevel, pPos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    public void placeAt(LevelAccessor p_196390_1_, BlockPos p_196390_2_, int p_196390_3_) {
        p_196390_1_.setBlock(p_196390_2_, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), p_196390_3_);
        p_196390_1_.setBlock(p_196390_2_.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), p_196390_3_);
    }

    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        //Spawn Golem
        if (!pLevel.isClientSide) {
            double d0 = pState.getValue(HALF) == DoubleBlockHalf.LOWER ? 0d : -1d;
            AbandonedIronGolem abandonedIronGolem = EntityTypeRegistry.ABANDONED_IRON_GOLEM.get().create(pLevel);
            abandonedIronGolem.setPos(pPos.getX() + 0.5d, pPos.getY()+d0, pPos.getZ() + 0.5d);
            float f0 = pState.getValue(FACING).toYRot();
            abandonedIronGolem.setYBodyRot(f0);
            abandonedIronGolem.setYHeadRot(f0);
            pLevel.addFreshEntity(abandonedIronGolem);
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pTe, ItemStack pStack) {
        super.playerDestroy(pLevel, pPlayer, pPos, Blocks.AIR.defaultBlockState(), pTe, pStack);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF, FACING);
    }
}
