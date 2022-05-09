package com.bedmen.odyssey.block.light_emitters;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Supplier;

public class LightEmitterStairs extends StairBlock {

    private IsLitProvider provider;
    private static final BooleanProperty LIT = BlockStateProperties.LIT;

    public LightEmitterStairs(java.util.function.Supplier<BlockState> state, BlockBehaviour.Properties properties, IsLitProvider provider) {
        super(state, properties);
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
    public void randomTick(BlockState state, @NotNull ServerLevel serverLevel, BlockPos blockPos, Random random) {
        super.randomTick(state, serverLevel, blockPos, random);
        if (!serverLevel.isAreaLoaded(blockPos, 1)) return;
        // equiv to (lit && !lit_cond) || (!lit && lit_cond)
        if (state.getValue(LIT) ^ this.provider.isLit(blockPos, serverLevel)) {
            serverLevel.setBlock(blockPos, state.cycle(LIT), 2);
        }
    }
}
