package com.bedmen.odyssey.blocks;

import com.bedmen.odyssey.entity.boss.AbandonedIronGolemEntity;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AbandonedIronGolemBlock extends Block implements INeedsToRegisterRenderType {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    protected static final VoxelShape SHAPE_UPPER_NORTH = VoxelShapes.join(box(3,-0.5,4.5,13,5.5,11.5),box(-1,5,2,17,17,13), IBooleanFunction.OR);
    protected static final VoxelShape SHAPE_UPPER_EAST = VoxelShapes.join(box(4.5,-0.5,4,11.5,5.5,13),box(3,5,-1,14,17,17), IBooleanFunction.OR);
    protected static final VoxelShape SHAPE_UPPER_SOUTH = VoxelShapes.join(box(3,-0.5,4.5,13,5.5,11.5),box(-1,5,3,17,17,14), IBooleanFunction.OR);
    protected static final VoxelShape SHAPE_UPPER_WEST = VoxelShapes.join(box(4.5,-0.5,3,11.5,5.5,13),box(2,5,-1,13,17,17), IBooleanFunction.OR);
    protected static final VoxelShape SHAPE_LOWER_NORTH = VoxelShapes.join(box(9.5,0,6,15.5,16,11),box(0.5,0,6,6.5,16,11), IBooleanFunction.OR);
    protected static final VoxelShape SHAPE_LOWER_EAST = VoxelShapes.join(box(5,0,9.5,10,16,15.5),box(5,0,0.5,10,16,6.5), IBooleanFunction.OR);
    protected static final VoxelShape SHAPE_LOWER_SOUTH = VoxelShapes.join(box(9.5,0,5,15.5,16,10),box(0.5,0,5,6.5,16,10), IBooleanFunction.OR);
    protected static final VoxelShape SHAPE_LOWER_WEST = VoxelShapes.join(box(6,0,9.5,11,16,15.5),box(6,0,0.5,11,16,6.5), IBooleanFunction.OR);

    public AbandonedIronGolemBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(FACING, Direction.NORTH));
    }

    public VoxelShape getShape(BlockState blockState, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
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

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);
        if (pFacing.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (pFacing == Direction.UP) || pFacingState.is(this) && pFacingState.getValue(HALF) != doubleblockhalf) {
            return doubleblockhalf == DoubleBlockHalf.LOWER && pFacing == Direction.DOWN && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockState =  blockpos.getY() < 255 && pContext.getLevel().getBlockState(blockpos.above()).canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
        return blockState.setValue(FACING, pContext.getHorizontalDirection());
    }

    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        pLevel.setBlock(pPos.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER).setValue(FACING, pState.getValue(FACING)), 3);
    }

    public boolean canSurvive(BlockState pState, IWorldReader pLevel, BlockPos pPos) {
        if (pState.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(pState, pLevel, pPos);
        } else {
            BlockState blockstate = pLevel.getBlockState(pPos.below());
            if (pState.getBlock() != this) return super.canSurvive(pState, pLevel, pPos); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    public void placeAt(IWorld p_196390_1_, BlockPos p_196390_2_, int p_196390_3_) {
        p_196390_1_.setBlock(p_196390_2_, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER), p_196390_3_);
        p_196390_1_.setBlock(p_196390_2_.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), p_196390_3_);
    }

    public void playerWillDestroy(World pLevel, BlockPos pPos, BlockState pState, PlayerEntity pPlayer) {
        //Spawn Golem
        if (!pLevel.isClientSide) {
            double d0 = pState.getValue(HALF) == DoubleBlockHalf.LOWER ? 0d : -1d;
            AbandonedIronGolemEntity abandonedIronGolemEntity = EntityTypeRegistry.ABANDONED_IRON_GOLEM.get().create(pLevel);
            abandonedIronGolemEntity.setPos(pPos.getX() + 0.5d, pPos.getY()+d0, pPos.getZ() + 0.5d);
            float f0 = pState.getValue(FACING).getOpposite().toYRot();
            abandonedIronGolemEntity.setYBodyRot(f0);
            abandonedIronGolemEntity.setYHeadRot(f0);
            pLevel.addFreshEntity(abandonedIronGolemEntity);
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    public void playerDestroy(World pLevel, PlayerEntity pPlayer, BlockPos pPos, BlockState pState, @Nullable TileEntity pTe, ItemStack pStack) {
        super.playerDestroy(pLevel, pPlayer, pPos, Blocks.AIR.defaultBlockState(), pTe, pStack);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF, FACING);
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}
