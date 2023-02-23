package com.bedmen.odyssey.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public class WorldGenUtil {

    public static boolean isEmpty(WorldGenLevel worldgenlevel, BlockPos blockPos){
        return (worldgenlevel.isEmptyBlock(blockPos) || worldgenlevel.getBlockState(blockPos).getCollisionShape(worldgenlevel, blockPos).isEmpty()) && worldgenlevel.getBlockState(blockPos).getFluidState().isEmpty();
    }

    public static boolean isEmpty(Level level, BlockPos blockPos){
        return (level.isEmptyBlock(blockPos) || level.getBlockState(blockPos).getCollisionShape(level, blockPos).isEmpty()) && level.getBlockState(blockPos).getFluidState().isEmpty();
    }

    public static boolean isSolid(WorldGenLevel worldgenlevel, BlockPos blockPos){
        BlockState blockState = worldgenlevel.getBlockState(blockPos);
        return !(worldgenlevel.isEmptyBlock(blockPos) || blockState.getCollisionShape(worldgenlevel, blockPos).isEmpty() || !blockState.getFluidState().isEmpty());
    }

    public static boolean isReplaceableByStructures(BlockState blockState) {
        return blockState.isAir() || blockState.getMaterial().isLiquid() || blockState.is(Blocks.GLOW_LICHEN) || blockState.is(Blocks.SEAGRASS) || blockState.is(Blocks.TALL_SEAGRASS) || blockState.is(Blocks.LILY_PAD);
    }

    public static void fillColumnDown(WorldGenLevel worldGenLevel, BlockState blockState, BlockPos blockPos, BoundingBox chunkBoundingBox) {
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
        if (chunkBoundingBox.isInside(mutableBlockPos)) {
            while(isReplaceableByStructures(worldGenLevel.getBlockState(mutableBlockPos)) && mutableBlockPos.getY() > worldGenLevel.getMinBuildHeight() + 1) {
                worldGenLevel.setBlock(mutableBlockPos, blockState, 2);
                mutableBlockPos.move(Direction.DOWN);
            }
        }
    }
}
