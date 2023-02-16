package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.block.entity.CovenHutDoorBlockEntity;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class CovenHutDoorBlock extends TransparentDoorBlock implements EntityBlock {

    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;

    public CovenHutDoorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LOCKED, true));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LOCKED);
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(blockState.getValue(LOCKED)){
            return InteractionResult.PASS;
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        // Only the bottom half of the door has the block entity
        if(blockState.getValue(HALF) == DoubleBlockHalf.UPPER){
            return null;
        }
        return new CovenHutDoorBlockEntity(blockPos, blockState);
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        // Only the bottom half of the door has the block entity
        if(blockState.getValue(HALF) == DoubleBlockHalf.UPPER){
            return null;
        }
        return createCovenHutDoorTicker(level, blockEntityType, BlockEntityTypeRegistry.COVEN_HUT_DOOR.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createCovenHutDoorTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends CovenHutDoorBlockEntity> blockEntityType2) {
        return level.isClientSide ? BaseEntityBlock.createTickerHelper(blockEntityType, blockEntityType2, CovenHutDoorBlockEntity::clientTick) : BaseEntityBlock.createTickerHelper(blockEntityType, blockEntityType2, CovenHutDoorBlockEntity::serverTick);
    }
}
