package com.bedmen.odyssey.block.wood;

import com.bedmen.odyssey.block.entity.CovenHutDoorBlockEntity;
import com.bedmen.odyssey.items.KeyItem;
import com.bedmen.odyssey.lock.LockableDoorType;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CovenHutDoorBlock extends TransparentDoorBlock implements EntityBlock {

    public CovenHutDoorBlock(Properties properties) {
        super(properties);
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if(itemStack.getItem() instanceof KeyItem keyItem && keyItem.lockType == LockableDoorType.COVEN_HUT){
            BlockPos lowerBlockPos = blockState.getValue(HALF) == DoubleBlockHalf.UPPER ? blockPos.below() : blockPos;
            BlockPos upperBlockPos = blockState.getValue(HALF) == DoubleBlockHalf.UPPER ? blockPos : blockPos.above();
            BlockState lowerBlockState = level.getBlockState(lowerBlockPos);
            BlockState upperBlockState = level.getBlockState(upperBlockPos);
            level.setBlock(lowerBlockPos, Blocks.AIR.defaultBlockState(), 3);
            level.setBlock(upperBlockPos, Blocks.AIR.defaultBlockState(), 3);
            BlockState oakDoorLowerBlockState = getOakDoorReplacementBlockState(lowerBlockState);
            if(Blocks.OAK_DOOR.canSurvive(oakDoorLowerBlockState, level, lowerBlockPos)){
                level.setBlock(lowerBlockPos, oakDoorLowerBlockState, 3);
                level.setBlock(upperBlockPos, getOakDoorReplacementBlockState(upperBlockState), 3);
            }
            level.playSound(player, blockPos, SoundEventRegistry.KEY_UNLOCK.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        } else {
            level.playSound(player, blockPos, SoundEventRegistry.LOCKED_CHEST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.FAIL;
        }
    }

    public BlockState getOakDoorReplacementBlockState(BlockState blockState){
        return Blocks.OAK_DOOR.defaultBlockState()
                .setValue(FACING, blockState.getValue(FACING))
                .setValue(OPEN, blockState.getValue(OPEN))
                .setValue(HINGE, blockState.getValue(HINGE))
                .setValue(HALF, blockState.getValue(HALF));
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

    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPosNeighbor, boolean flag) {
    }

    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return true;
    }
}
