package com.bedmen.odyssey.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Random;

public class TemporaryWebBlock extends OdysseyWebBlock {
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public TemporaryWebBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean p_60570_) {
        level.scheduleTick(blockPos, this, Mth.nextInt(level.random, 5, 10));
    }

    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        this.slightlyBreak(blockState, serverLevel, blockPos);
        serverLevel.scheduleTick(blockPos, this, Mth.nextInt(random, 5, 10));
    }

    private boolean slightlyBreak(BlockState blockState, Level level, BlockPos blockPos) {
        int age = blockState.getValue(AGE);
        if (age < MAX_AGE) {
            level.setBlock(blockPos, blockState.setValue(AGE, age + 1), 2);
            return false;
        } else {
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
            return true;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return ItemStack.EMPTY;
    }
}
