package com.bedmen.odyssey.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface TemporaryBlock {

    IntegerProperty getAgeProperty();
    int getMaxAge();

    default boolean slightlyBreak(BlockState blockState, Level level, BlockPos blockPos) {
        IntegerProperty ageProperty = this.getAgeProperty();
        int age = blockState.getValue(ageProperty);
        if (age < this.getMaxAge()) {
            level.setBlock(blockPos, blockState.setValue(ageProperty, age + 1), 2);
            return false;
        } else {
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
            return true;
        }
    }

    default void scheduleNextTick(Level level, BlockPos blockPos, Block block){
        level.scheduleTick(blockPos, block, Mth.nextInt(level.getRandom(), 5, 10));
    }

}
