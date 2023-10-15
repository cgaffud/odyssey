package com.bedmen.odyssey.block;

import javax.annotation.Nullable;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.block.entity.AbstractCrafterBlockEntity;
import com.bedmen.odyssey.items.aspect_items.AspectItem;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.util.OdysseyStats;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class AbstractCrafterBlock extends BaseEntityBlock {

    public AbstractCrafterBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AbstractCrafterBlockEntity(blockPos, blockState);
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openContainer(level, blockPos, player);
            return InteractionResult.CONSUME;
        }
    }

    protected void openContainer(Level level, BlockPos blockPos, Player player) {
        BlockEntity blockentity = level.getBlockEntity(blockPos);
        if (blockentity instanceof AbstractCrafterBlockEntity) {
            player.openMenu((MenuProvider)blockentity);
            player.awardStat(OdysseyStats.INTERACT_WITH_ABSTRACT_CRAFTER);
        }

    }

    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockentity = level.getBlockEntity(blockPos);
            if (blockentity instanceof AbstractCrafterBlockEntity abstractCrafterBlockEntity) {
                abstractCrafterBlockEntity.setCustomName(itemStack.getHoverName());
            }
        }

    }

    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean flag) {
        if (!blockState.is(blockState1.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(blockPos);
            if (blockentity instanceof AbstractCrafterBlockEntity abstractCrafterBlockEntity) {
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, blockPos, abstractCrafterBlockEntity);
                    abstractCrafterBlockEntity.getRecipesToAwardAndPopExperience((ServerLevel)level, Vec3.atCenterOf(blockPos));
                }

                level.updateNeighbourForOutputSignal(blockPos, this);
            }

            super.onRemove(blockState, level, blockPos, blockState1, flag);
        }
    }

    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createAbstractCrafterTicker(level, blockEntityType, BlockEntityTypeRegistry.ABSTRACT_CRAFTER.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createAbstractCrafterTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends AbstractCrafterBlockEntity> blockEntityType2) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, blockEntityType2, AbstractCrafterBlockEntity::serverTick);
    }

    // TODO use this function to set block entity ModularInventory
    protected boolean updateCustomBlockEntityTag(BlockPos p_40597_, Level p_40598_, @Nullable Player p_40599_, ItemStack p_40600_, BlockState p_40601_) {
        return updateCustomBlockEntityTag(p_40598_, p_40599_, p_40597_, p_40600_);
    }

    protected void setModularInventory(BlockPos blockPos, Level level, @Nullable Player player, ItemStack itemStack, BlockState blockState){
        BlockEntity blockentity = level.getBlockEntity(blockPos);
        if(blockentity instanceof AbstractCrafterBlockEntity abstractCrafterBlockEntity){
            if(itemStack.getItem() instanceof AspectItem aspectItem){
                Aspe vbctUtil.get
            }
        }
    }
}