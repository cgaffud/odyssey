package com.bedmen.odyssey.block.light_emitters;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class LightEmitterBlock extends Block {

    private IsLitProvider provider;
    private static final BooleanProperty LIT = BlockStateProperties.LIT;

    // SET IsRandomlyTicking to true in properties before using
    public LightEmitterBlock(Properties p_49795_, IsLitProvider provider) {
        super(p_49795_);
        this.provider = provider;
        this.registerDefaultState(this.defaultBlockState().setValue(LIT,false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LIT);
        super.createBlockStateDefinition(stateBuilder);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, @NotNull ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.randomTick(state, serverLevel, blockPos, randomSource);
        if (!serverLevel.isAreaLoaded(blockPos, 1)) return;
        // equiv to (lit && !lit_cond) || (!lit && lit_cond)
        if (state.getValue(LIT) ^ this.provider.isLit(blockPos, serverLevel)) {
            serverLevel.setBlock(blockPos, state.cycle(LIT), 2);
        }
    }

}
