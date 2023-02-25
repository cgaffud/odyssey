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
            Pair.of(5, 1),
            Pair.of(8, 7),
            Pair.of(14, 1),
            Pair.of(16, 7),
            Pair.of(17, 13),
            Pair.of(17, 17),
            Pair.of(12, 13),
            Pair.of(12, 17),
            Pair.of(15, 19),
            Pair.of(5,9),
            Pair.of(1,10),
            Pair.of(8,15),
            Pair.of(1,20),
            Pair.of(8,20)
    );

    public CovenHutPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        super(StructurePieceTypeRegistry.COVEN_HUT.get(), 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(), blockPos, rotation);
    }

    public CovenHutPiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.COVEN_HUT.get(), compoundTag, structureManager, (resourceLocation) -> makeSettings());
    }

    private static StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(MossyBlockProcessor.INSTANCE);
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
}
