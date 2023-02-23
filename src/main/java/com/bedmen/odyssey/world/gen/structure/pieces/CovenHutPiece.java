package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.world.WorldGenUtil;
import com.bedmen.odyssey.world.gen.block_processor.MossyBlockProcessor;
import com.bedmen.odyssey.world.gen.structure.CovenHutFeature;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.*;

public class CovenHutPiece extends HeightAdjustingPiece {

    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"coven_hut");

    private static final List<Pair<Integer, Integer>> RELATIVE_POSTS = List.of(
            new Pair<>(5, 1),
            new Pair<>(8, 7),
            new Pair<>(14, 1),
            new Pair<>(16, 7),
            new Pair<>(17, 13),
            new Pair<>(17, 17),
            new Pair<>(12, 13),
            new Pair<>(12, 17),
            new Pair<>(15, 19),
            new Pair<>(5,9),
            new Pair<>(1,10),
            new Pair<>(8,15),
            new Pair<>(1,20),
            new Pair<>(8,20)
    );

    public CovenHutPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        super(StructurePieceTypeRegistry.COVEN_HUT.get(), 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(rotation), blockPos);
    }

    public CovenHutPiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.COVEN_HUT.get(), compoundTag, structureManager, (resourceLocation) -> makeSettings(Rotation.valueOf(compoundTag.getString("Rot"))));
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(MossyBlockProcessor.INSTANCE);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putString("Rot", this.placeSettings.getRotation().name());
    }

    protected void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        WorldGenUtil.fillColumnDownOnAllPosts(worldGenLevel, Blocks.OAK_LOG.defaultBlockState(), RELATIVE_POSTS, chunkBoundingBox, this.templatePosition, this.placeSettings);
    }

    protected boolean updateHeightPosition(LevelAccessor levelAccessor) {
        if (this.hasCalculatedHeightPosition) {
            return true;
        } else {
            int height = levelAccessor.getMinBuildHeight();
            boolean heightHasBeenSet = false;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

            for(int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
                for(int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
                    mutableBlockPos.set(x, 0, z);
                    height = Math.max(height, levelAccessor.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, mutableBlockPos).getY());
                    heightHasBeenSet = true;
                }
            }

            if (!heightHasBeenSet) {
                return false;
            } else {
                this.boundingBox.move(0, height - this.boundingBox.minY(), 0);
                this.templatePosition = this.templatePosition.atY(this.boundingBox.minY());
                this.hasCalculatedHeightPosition = true;
                return true;
            }
        }
    }

    protected void handleDataMarker(String dataMarker, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, Random random, BoundingBox chunkBoundingBox) {
        // todo adjust chest loot
        WorldGenUtil.fillChestBelowDataMarker("chest", dataMarker, serverLevelAccessor, blockPos, random, this.placeSettings, OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST);
        WorldGenUtil.fillChestBelowDataMarker("secret_chest", dataMarker, serverLevelAccessor, blockPos, random, this.placeSettings, OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST);
        WorldGenUtil.fillChestBelowDataMarker("ingredients", dataMarker, serverLevelAccessor, blockPos, random, this.placeSettings, OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST);
        WorldGenUtil.fillChestBelowDataMarker("tomes", dataMarker, serverLevelAccessor, blockPos, random, this.placeSettings, OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST);
    }
}
