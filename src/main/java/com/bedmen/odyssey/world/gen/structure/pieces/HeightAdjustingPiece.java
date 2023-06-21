package com.bedmen.odyssey.world.gen.structure.pieces;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.function.Function;

public abstract class HeightAdjustingPiece extends RotatedStructurePiece {

    protected boolean hasCalculatedHeightPosition = false;
    private static final String HAS_CALCULATED_HEIGHT_POSITION_TAG = "HasCalculatedHeightPosition";

    public HeightAdjustingPiece(StructurePieceType structurePieceType, int genDepth, StructureTemplateManager structureTemplateManager, ResourceLocation resourceLocation, String name, StructurePlaceSettings structurePlaceSettings, BlockPos blockPos, Rotation rotation) {
        super(structurePieceType, genDepth, structureTemplateManager, resourceLocation, name, structurePlaceSettings, blockPos, rotation);
    }

    public HeightAdjustingPiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructureTemplateManager structureTemplateManager, Function<ResourceLocation, StructurePlaceSettings> function) {
        super(structurePieceType, compoundTag, structureTemplateManager, function);
        this.hasCalculatedHeightPosition = compoundTag.getBoolean(HAS_CALCULATED_HEIGHT_POSITION_TAG);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putBoolean(HAS_CALCULATED_HEIGHT_POSITION_TAG, this.hasCalculatedHeightPosition);
    }

    public void postProcess(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos pos) {
        if(updateHeightPosition(worldGenLevel)){
            super.postProcess(worldGenLevel, structureManager, chunkGenerator, randomSource, chunkBoundingBox, chunkPos, pos);
            this.postProcessAfterHeightUpdate(worldGenLevel, structureManager, chunkGenerator, randomSource, chunkBoundingBox, chunkPos, pos);
        }
    }

    protected abstract boolean updateHeightPosition(LevelAccessor levelAccessor);

    protected abstract void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos);
}
