package com.bedmen.odyssey.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.List;
import java.util.stream.Collectors;

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
        return blockState.isAir() || blockState.getMaterial().isLiquid() || blockState.getBlock() instanceof BushBlock || blockState.is(Blocks.GLOW_LICHEN);
    }

    public static void fillColumnDownOnAllPosts(WorldGenLevel worldGenLevel, BlockState blockState, List<Pair<Integer, Integer>> relativePostList, BoundingBox chunkBoundingBox, BlockPos templateBlockPos, StructurePlaceSettings structurePlaceSettings){
        fillColumnDownOnAllPosts(worldGenLevel, new BlockState[]{blockState}, relativePostList, chunkBoundingBox, templateBlockPos, structurePlaceSettings);
    }

    public static void fillColumnDownOnAllPosts(WorldGenLevel worldGenLevel, BlockState[] blockStateArray, List<Pair<Integer, Integer>> relativePostList, BoundingBox chunkBoundingBox, BlockPos templateBlockPos, StructurePlaceSettings structurePlaceSettings){
        List<BlockPos> relativePostBlockPosList = relativePostList.stream().map(pair -> new BlockPos(pair.getFirst(), -1, pair.getSecond())).collect(Collectors.toList());
        fillColumnDownOnAllPostBlockPos(worldGenLevel, blockStateArray, relativePostBlockPosList, chunkBoundingBox, templateBlockPos, structurePlaceSettings);
    }

    public static void fillColumnDownOnAllPostBlockPos(WorldGenLevel worldGenLevel, BlockState[] blockStateArray, List<BlockPos> relativePostBlockPosList, BoundingBox chunkBoundingBox, BlockPos templateBlockPos, StructurePlaceSettings structurePlaceSettings){
        for(BlockPos relativePostBlockPos : relativePostBlockPosList) {
            BlockPos postPos = getWorldPosition(relativePostBlockPos, templateBlockPos, structurePlaceSettings);
            fillColumnDown(worldGenLevel, blockStateArray, postPos, chunkBoundingBox);
        }
    }

    public static void fillColumnDown(WorldGenLevel worldGenLevel, BlockState[] blockStateArray, BlockPos blockPos, BoundingBox chunkBoundingBox) {
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
        if (chunkBoundingBox.isInside(mutableBlockPos)) {
            while(isReplaceableByStructures(worldGenLevel.getBlockState(mutableBlockPos)) && mutableBlockPos.getY() > worldGenLevel.getMinBuildHeight() + 1) {
                worldGenLevel.setBlock(mutableBlockPos, blockStateArray[worldGenLevel.getRandom().nextInt(blockStateArray.length)], 2);
                mutableBlockPos.move(Direction.DOWN);
            }
        }
    }

    public static void fillColumnDown(WorldGenLevel worldGenLevel, BlockState blockState, BlockPos blockPos, BoundingBox chunkBoundingBox) {
        fillColumnDown(worldGenLevel, new BlockState[]{blockState}, blockPos, chunkBoundingBox);
    }

    public static BlockPos getWorldPosition(BlockPos blockPos, BlockPos templateBlockPos, StructurePlaceSettings structurePlaceSettings){
        return templateBlockPos.offset(blockPos.rotate(structurePlaceSettings.getRotation()));
    }

    public static void addEntityToStructure(Entity entity, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor){
        entity.moveTo((double)blockPos.getX() + 0.5D, blockPos.getY(), (double)blockPos.getZ() + 0.5D, 0.0F, 0.0F);
        if(entity instanceof Mob mob){
            mob.setPersistenceRequired();
            mob.finalizeSpawn(serverLevelAccessor, serverLevelAccessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, null, null);
        }
        serverLevelAccessor.addFreshEntityWithPassengers(entity);
    }

}
