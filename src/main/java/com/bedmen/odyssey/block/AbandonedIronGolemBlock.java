package com.bedmen.odyssey.block;

import com.bedmen.odyssey.entity.boss.AbandonedIronGolem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class AbandonedIronGolemBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape SHAPE_NORTH = Shapes.join(box(3,3.5,4.5,13,9.5,11.5),box(-1,9,2,17,22,13), BooleanOp.OR);
    protected static final VoxelShape SHAPE_EAST = Shapes.join(box(4.5,3.5,4,11.5,9.5,13),box(3,9,-1,14,22,17), BooleanOp.OR);
    protected static final VoxelShape SHAPE_SOUTH = Shapes.join(box(3,3.5,4.5,13,9.5,11.5),box(-1,9,3,17,22,14), BooleanOp.OR);
    protected static final VoxelShape SHAPE_WEST = Shapes.join(box(4.5,3.5,3,11.5,9.5,13),box(2,9,-1,13,22,17), BooleanOp.OR);

    public AbandonedIronGolemBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        switch(blockState.getValue(FACING)){
            case EAST: return SHAPE_EAST;
            case SOUTH: return SHAPE_SOUTH;
            case WEST: return SHAPE_WEST;
            default: return SHAPE_NORTH;
        }
    }


    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockState =  blockpos.getY() < 255 && pContext.getLevel().getBlockState(blockpos.above()).canBeReplaced(pContext) ? super.getStateForPlacement(pContext) : null;
        return blockState.setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        //Spawn Golem
        if (!pLevel.isClientSide) {
            AbandonedIronGolem abandonedIronGolem = EntityTypeRegistry.ABANDONED_IRON_GOLEM.get().create(pLevel);
            abandonedIronGolem.setPos(pPos.getX() + 0.5d, pPos.getY(), pPos.getZ() + 0.5d);
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
        pBuilder.add(FACING);
    }
}
