package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.util.WorldGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class BarnPiece extends TemplateStructurePiece {
    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"barn");
    public BarnPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        super(StructurePieceTypeRegistry.BARN.get(), 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(rotation), blockPos);
    }

    public BarnPiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.BARN.get(), compoundTag, structureManager, (resourceLocation) -> {
            return makeSettings(Rotation.valueOf(compoundTag.getString("Rot")));
        });
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putString("Rot", this.placeSettings.getRotation().name());
    }

    protected void handleDataMarker(String s, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, Random random, BoundingBox boundingBox) {

    }

    public void postProcess(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        BoundingBox structureBoundingBox = this.boundingBox;
        int i = 0;
        if(structureBoundingBox != null){
            BlockPos center = structureBoundingBox.getCenter();
            i = worldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, center.getX(), center.getZ());
        }
        BlockPos blockpos2 = this.templatePosition;
        this.templatePosition = this.templatePosition.offset(0, i - 90 - 1, 0);
        super.postProcess(worldGenLevel, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, blockPos);
        this.templatePosition = blockpos2;
    }
}
