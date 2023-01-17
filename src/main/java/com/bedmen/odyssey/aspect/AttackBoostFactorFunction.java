package com.bedmen.odyssey.aspect;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface AttackBoostFactorFunction {
    float getBoostFactor(BlockPos pos, Level level);
}