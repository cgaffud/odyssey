package com.bedmen.odyssey.world.gen.structure.pieces;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;
import java.util.function.Function;

public abstract class RotatedStructurePiece extends TemplateStructurePiece {

    private static final String ROTATION_TAG = "Rotation";

    public RotatedStructurePiece(StructurePieceType structurePieceType, int genDepth, StructureManager structureManager, ResourceLocation resourceLocation, String name, StructurePlaceSettings structurePlaceSettings, BlockPos blockPos, Rotation rotation) {
        super(structurePieceType, genDepth, structureManager, resourceLocation, name, structurePlaceSettings.setRotation(rotation), blockPos);
    }

    public RotatedStructurePiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructureManager structureManager, Function<ResourceLocation, StructurePlaceSettings> function) {
        super(structurePieceType, compoundTag, structureManager, (resourceLocation) -> function.apply(resourceLocation).setRotation(Rotation.valueOf(compoundTag.getString(ROTATION_TAG))));
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putString(ROTATION_TAG, this.placeSettings.getRotation().name());
    }

    protected void handleDataMarker(String dataMarker, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, Random random, BoundingBox boundingBox) {
    }
}
