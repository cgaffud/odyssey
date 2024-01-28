package com.bedmen.odyssey.block;

import com.bedmen.odyssey.block.entity.SculkBulbBlockEntity;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SculkBulbBlock extends BaseEntityBlock {

    private final int listenerRange;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 6.0D, 15.0D);


    public SculkBulbBlock(Properties properties, int listenerRange) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, true));
        this.listenerRange = listenerRange;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    public int getListenerRange() {
        return this.listenerRange;
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SculkBulbBlockEntity(blockPos, blockState);
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> GameEventListener getListener(ServerLevel p_222123_, T p_222124_) {
        return p_222124_ instanceof SculkBulbBlockEntity ? ((SculkBulbBlockEntity)p_222124_).getListener() : null;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_154401_, BlockState p_154402_, BlockEntityType<T> p_154403_) {
        return !p_154401_.isClientSide ? createTickerHelper(p_154403_, BlockEntityTypeRegistry.SCULK_BULB.get(), (p_154417_, p_154418_, p_154419_, p_154420_) -> {
            p_154420_.getListener().tick(p_154417_);
        }) : null;
    }

    public static void deactivate(Level level, BlockPos blockPos, BlockState blockState) {
        level.setBlock(blockPos, blockState.setValue(LIT, false), 3);
    }

    public RenderShape getRenderShape(BlockState p_154477_) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState p_154432_, BlockGetter p_154433_, BlockPos p_154434_, CollisionContext p_154435_) {
        return SHAPE;
    }
}
