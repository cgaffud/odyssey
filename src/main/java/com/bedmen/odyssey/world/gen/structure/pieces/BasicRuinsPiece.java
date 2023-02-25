package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.world.gen.processor.BarnFloorProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;

public class BasicRuinsPiece extends HeightAdjustingPiece {

    public static final ResourceLocation[] STRUCTURE_LOCATIONS = {
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_0"),
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_1"),
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_2"),
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_3"),
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_4"),
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_5")
    };

    public BasicRuinsPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation, int id) {
        super(StructurePieceTypeRegistry.BASIC_RUINS.get(), 0, structureManager, STRUCTURE_LOCATIONS[id], STRUCTURE_LOCATIONS[id].toString(), makeSettings(), blockPos, rotation);
    }

    public BasicRuinsPiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.BASIC_RUINS.get(), compoundTag, structureManager, (resourceLocation) -> makeSettings());
    }

    private static StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(BarnFloorProcessor.INSTANCE);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
    }

    @Override
    protected boolean updateHeightPosition(LevelAccessor levelAccessor) {
        if (this.hasCalculatedHeightPosition) {
            return true;
        } else {
            int height = levelAccessor.getMaxBuildHeight();
            boolean heightHasBeenSet = false;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

            for(int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
                for(int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
                    mutableBlockPos.set(x, 0, z);
                    height = Math.min(height, levelAccessor.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, mutableBlockPos).getY());
                    heightHasBeenSet = true;
                }
            }

            if (!heightHasBeenSet) {
                return false;
            } else {
                int heightChange = height - this.boundingBox.minY();
                this.move(0, heightChange, 0);
                this.hasCalculatedHeightPosition = true;
                return true;
            }
        }
    }

    @Override
    protected void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {

    }
}
