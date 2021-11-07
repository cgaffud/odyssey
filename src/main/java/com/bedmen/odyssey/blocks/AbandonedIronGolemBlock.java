package com.bedmen.odyssey.blocks;

import com.bedmen.odyssey.entity.boss.AbandonedIronGolemEntity;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
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
    protected static final VoxelShape SHAPE_UPPER = VoxelShapes.join(box(3,-0.5,4.5,13,5.5,11.5),box(-1,5,2,17,17,13), IBooleanFunction.OR);
    protected static final VoxelShape SHAPE_LOWER = VoxelShapes.join(box(9.5,0,6,15.5,16,11),box(0.5,0,6,6.5,16,11), IBooleanFunction.OR);

    public AbandonedIronGolemBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public VoxelShape getShape(BlockState blockState, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        if(blockState.getValue(HALF) == DoubleBlockHalf.UPPER){
            return SHAPE_UPPER;
        }
        return SHAPE_LOWER;
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
        return blockpos.getY() < 255 && pContext.getLevel().getBlockState(blockpos.above()).canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
    }

    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        pLevel.setBlock(pPos.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 3);
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
            pLevel.addFreshEntity(abandonedIronGolemEntity);
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    public void playerDestroy(World pLevel, PlayerEntity pPlayer, BlockPos pPos, BlockState pState, @Nullable TileEntity pTe, ItemStack pStack) {
        super.playerDestroy(pLevel, pPlayer, pPos, Blocks.AIR.defaultBlockState(), pTe, pStack);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF);
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}
