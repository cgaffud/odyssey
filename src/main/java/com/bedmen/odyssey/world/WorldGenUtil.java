package com.bedmen.odyssey.world;

import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.List;
import java.util.Random;

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

    public static void fillColumnDown(WorldGenLevel worldGenLevel, BlockState blockState, BlockPos blockPos, BoundingBox chunkBoundingBox) {
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
        if (chunkBoundingBox.isInside(mutableBlockPos)) {
            while(isReplaceableByStructures(worldGenLevel.getBlockState(mutableBlockPos)) && mutableBlockPos.getY() > worldGenLevel.getMinBuildHeight() + 1) {
                worldGenLevel.setBlock(mutableBlockPos, blockState, 2);
                mutableBlockPos.move(Direction.DOWN);
            }
        }
    }

    public static void fillColumnDownOnAllPosts(WorldGenLevel worldGenLevel, BlockState blockState, List<Pair<Integer, Integer>> relativePosts, BoundingBox chunkBoundingBox, BlockPos templateBlockPos, StructurePlaceSettings structurePlaceSettings){
        for(Pair<Integer,Integer> pair : relativePosts) {
            BlockPos postPos = templateBlockPos.offset(new BlockPos(pair.getFirst(), -1, pair.getSecond()).rotate(structurePlaceSettings.getRotation()));
            fillColumnDown(worldGenLevel, blockState, postPos, chunkBoundingBox);
        }
    }

    public static void fillChestBelowDataMarker(String dataKey, String dataMarker, ServerLevelAccessor serverLevelAccessor, BlockPos blockPos, Random random, StructurePlaceSettings structurePlaceSettings, ResourceLocation lootTable){
        if (dataKey.startsWith(dataMarker)) {
            int colonIndex = dataKey.indexOf(":");
            Direction direction = Direction.DOWN;
            if(colonIndex > -1){
                String directionName = dataMarker.substring(colonIndex+1);
                direction = Direction.valueOf(directionName);
            }
            System.out.println(direction);
            BlockPos directionBlockPos = new BlockPos(direction.getNormal()).rotate(structurePlaceSettings.getRotation());
            BlockPos containerBlockPos = blockPos.offset(directionBlockPos);
            serverLevelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            BlockEntity blockentity = serverLevelAccessor.getBlockEntity(containerBlockPos);
            if (blockentity instanceof RandomizableContainerBlockEntity) {
                System.out.println("Found container");
                ((RandomizableContainerBlockEntity)blockentity).setLootTable(lootTable, random.nextLong());
            }
        }
    }
}
