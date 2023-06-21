package com.bedmen.odyssey.world.gen.structure.pieces;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.function.Function;

public abstract class RotatedStructurePiece extends TemplateStructurePiece {

    private static final String ROTATION_TAG = "Rotation";

    public RotatedStructurePiece(StructurePieceType structurePieceType, int genDepth, StructureTemplateManager structureTemplateManager, ResourceLocation resourceLocation, String name, StructurePlaceSettings structurePlaceSettings, BlockPos blockPos, Rotation rotation) {
        super(structurePieceType, genDepth, structureTemplateManager, resourceLocation, name, structurePlaceSettings.setRotation(rotation), blockPos);
    }

    public RotatedStructurePiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructureTemplateManager structureTemplateManager, Function<ResourceLocation, StructurePlaceSettings> function) {
        super(structurePieceType, compoundTag, structureTemplateManager, (resourceLocation) -> function.apply(resourceLocation).setRotation(Rotation.valueOf(compoundTag.getString(ROTATION_TAG))));
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putString(ROTATION_TAG, this.placeSettings.getRotation().name());
    }

    protected void handleDataMarker(String dataMarker, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, RandomSource randomSource, BoundingBox boundingBox) {
    }
}
