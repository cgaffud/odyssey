package com.bedmen.odyssey.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;

public class WorldGenUtil {

    public static boolean isEmpty(WorldGenLevel worldgenlevel, BlockPos blockPos){
        return (worldgenlevel.isEmptyBlock(blockPos) || worldgenlevel.getBlockState(blockPos).getCollisionShape(worldgenlevel, blockPos).isEmpty()) && worldgenlevel.getBlockState(blockPos).getFluidState().isEmpty();
    }

    public static boolean isEmpty(Level level, BlockPos blockPos){
        return (level.isEmptyBlock(blockPos) || level.getBlockState(blockPos).getCollisionShape(level, blockPos).isEmpty()) && level.getBlockState(blockPos).getFluidState().isEmpty();
    }

    public static boolean isSolid(WorldGenLevel worldgenlevel, BlockPos blockPos){
        return !(worldgenlevel.isEmptyBlock(blockPos) || worldgenlevel.getBlockState(blockPos).getCollisionShape(worldgenlevel, blockPos).isEmpty() || !worldgenlevel.getBlockState(blockPos).getFluidState().isEmpty());
    }
}
