package com.bedmen.odyssey.block.light_emitters;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Random;

public class LightEmitterBlock extends Block {

    private IsLitProvider provider;

    // SET IsRandomlyTicking to true in properties before using
    public LightEmitterBlock(Properties p_49795_, IsLitProvider provider) {
        super(p_49795_);
        this.provider = provider;
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.LIT,false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(BlockStateProperties.LIT);
        super.createBlockStateDefinition(stateBuilder);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        System.out.print(this.provider.isLit(state, serverLevel));
        System.out.print(" ");
        System.out.println(serverLevel.getDayTime() % 24000L);
        state.setValue(BlockStateProperties.LIT, this.provider.isLit(state, serverLevel));
        super.randomTick(state, serverLevel, blockPos, random);
    }

}
