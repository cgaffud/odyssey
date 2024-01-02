package com.bedmen.odyssey.block;

import com.bedmen.odyssey.block.entity.GetJumpedByBanditsBlockEntity;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class GetJumpedByBanditsBlock extends BaseEntityBlock {

    public GetJumpedByBanditsBlock(BlockBehaviour.Properties p_56781_) {
        super(p_56781_);
    }

    public BlockEntity newBlockEntity(BlockPos p_154687_, BlockState p_154688_) {
        return new GetJumpedByBanditsBlockEntity(p_154687_, p_154688_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_154684_, BlockEntityType<T> p_154685_) {
        return  createTickerHelper(p_154685_, BlockEntityTypeRegistry.GET_JUMPED_BY_BANDITS_BLOCK.get(), level.isClientSide ? GetJumpedByBanditsBlockEntity::clientTick : GetJumpedByBanditsBlockEntity::serverTick);
    }

    public void spawnAfterBreak(BlockState p_222477_, ServerLevel p_222478_, BlockPos p_222479_, ItemStack p_222480_, boolean p_222481_) {
        super.spawnAfterBreak(p_222477_, p_222478_, p_222479_, p_222480_, p_222481_);

    }

    public RenderShape getRenderShape(BlockState p_56794_) {
        return RenderShape.MODEL;
    }

    public ItemStack getCloneItemStack(BlockGetter p_56785_, BlockPos p_56786_, BlockState p_56787_) {
        return ItemStack.EMPTY;
    }
}
