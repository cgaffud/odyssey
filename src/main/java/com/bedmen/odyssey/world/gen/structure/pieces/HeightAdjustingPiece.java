package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.world.WorldGenUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;
import java.util.function.Function;

public abstract class HeightAdjustingPiece extends TemplateStructurePiece {

    protected boolean hasCalculatedHeightPosition = false;
    private static final String HAS_CALCULATED_HEIGHT_POSITION_TAG = "HasCalculatedHeightPosition";

    public HeightAdjustingPiece(StructurePieceType structurePieceType, int genDepth, StructureManager structureManager, ResourceLocation resourceLocation, String name, StructurePlaceSettings structurePlaceSettings, BlockPos blockPos) {
        super(structurePieceType, genDepth, structureManager, resourceLocation, name, structurePlaceSettings, blockPos);
    }

    public HeightAdjustingPiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructureManager structureManager, Function<ResourceLocation, StructurePlaceSettings> function) {
        super(structurePieceType, compoundTag, structureManager, function);
        this.hasCalculatedHeightPosition = compoundTag.getBoolean(HAS_CALCULATED_HEIGHT_POSITION_TAG);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putBoolean(HAS_CALCULATED_HEIGHT_POSITION_TAG, this.hasCalculatedHeightPosition);
    }

    public void postProcess(WorldGenLevel worldGenLevel, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos pos) {
        if(updateHeightPosition(worldGenLevel)){
            super.postProcess(worldGenLevel, manager, chunkGenerator, random, chunkBoundingBox, chunkPos, pos);
            this.postProcessAfterHeightUpdate(worldGenLevel, manager, chunkGenerator, random, chunkBoundingBox, chunkPos, pos);
        }
    }

    protected abstract boolean updateHeightPosition(LevelAccessor levelAccessor);

    protected abstract void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos);
}
