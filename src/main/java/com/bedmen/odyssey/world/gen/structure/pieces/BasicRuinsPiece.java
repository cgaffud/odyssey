package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.structure.StructurePieceTypeRegistry;
import com.bedmen.odyssey.world.gen.processor.CrackedBlockProcessor;
import com.bedmen.odyssey.world.gen.processor.MossyBlockProcessor;
import com.bedmen.odyssey.world.gen.processor.VineProcessor;
import com.bedmen.odyssey.world.gen.processor.WoodProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class BasicRuinsPiece extends HeightAdjustingPiece {

    public static final ResourceLocation[] STRUCTURE_LOCATIONS = {
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_0"),
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_1"),
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_2"),
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_3"),
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_4"),
            new ResourceLocation(Odyssey.MOD_ID,"basic_ruins/stone_5")
    };

    public BasicRuinsPiece(StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation, int id) {
        super(StructurePieceTypeRegistry.BASIC_RUINS.get(), 0, structureTemplateManager, STRUCTURE_LOCATIONS[id], STRUCTURE_LOCATIONS[id].toString(), makeSettings(), blockPos, rotation);
    }

    public BasicRuinsPiece(StructureTemplateManager structureTemplateManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.BASIC_RUINS.get(), compoundTag, structureTemplateManager, (resourceLocation) -> makeSettings());
    }

    private static StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(new MossyBlockProcessor(0.33f)).addProcessor(new CrackedBlockProcessor(0.5f)).addProcessor(new WoodProcessor()).addProcessor(new VineProcessor(0.1f));
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
    protected void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {

    }
}
