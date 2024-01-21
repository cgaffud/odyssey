package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.structure.StructurePieceTypeRegistry;
import com.bedmen.odyssey.world.WorldGenUtil;
import com.bedmen.odyssey.world.gen.processor.CrackedBlockProcessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DungeonlessMoonTowerPiece extends HeightAdjustingPiece {

    public static final SimpleWeightedRandomList<ResourceLocation> RUINS_LOCATIONS = SimpleWeightedRandomList.<ResourceLocation>builder().add(new ResourceLocation(Odyssey.MOD_ID,"moon_tower_ruins_1"), 1)
            .add(new ResourceLocation(Odyssey.MOD_ID,"moon_tower_ruins_2"), 1).build();

    public DungeonlessMoonTowerPiece(StructurePieceType structurePieceType, int genDepth, StructureTemplateManager structureTemplateManager, ResourceLocation resourceLocation, String name, StructurePlaceSettings structurePlaceSettings, BlockPos blockPos, Rotation rotation) {
        super(structurePieceType, genDepth, structureTemplateManager, resourceLocation, name, structurePlaceSettings, blockPos, rotation);
    }

    public DungeonlessMoonTowerPiece(StructureTemplateManager structureTemplateManager, ResourceLocation structureLocation, BlockPos blockPos, Rotation rotation) {
        this(StructurePieceTypeRegistry.DUNGEONLESS_MOON_TOWER.get(), 0, structureTemplateManager, structureLocation, structureLocation.toString(), makeSettings(), blockPos, rotation);
    }

    public DungeonlessMoonTowerPiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructureTemplateManager structureTemplateManager) {
        super(structurePieceType, compoundTag, structureTemplateManager, (resourceLocation) -> makeSettings());
    }

    public DungeonlessMoonTowerPiece(StructureTemplateManager structureTemplateManager, CompoundTag compoundTag) {
        this(StructurePieceTypeRegistry.DUNGEONLESS_MOON_TOWER.get(), compoundTag, structureTemplateManager);
    }


    private static final BlockPos RELATIVE_ENTRANCE = new BlockPos(4,0,0);
    private static final BlockState[] DEEPSLATE_BRICKS = {Blocks.DEEPSLATE_BRICKS.defaultBlockState(), Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState()};
    private static final List<Pair<Integer, Integer>> RELATIVE_POSTS = new ArrayList<>();
    static {
        for(int x = 1; x <= 7; x++){
            for(int z = 1; z <= 7; z++){
                boolean xEdge = x == 1 || x == 7;
                boolean zEdge = z == 1 || z == 7;
                // exclude corners
                if(xEdge && zEdge) {
                    continue;
                }
                RELATIVE_POSTS.add(Pair.of(x,z));
            }
        }
    }

    protected boolean updateHeightPosition(LevelAccessor levelAccessor) {
        BlockPos entranceBlockPos = WorldGenUtil.getWorldPosition(RELATIVE_ENTRANCE, this.templatePosition, this.placeSettings);
        int height = levelAccessor.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, entranceBlockPos).getY();
        if (height == levelAccessor.getMinBuildHeight()) {
            return false;
        } else {
            // -1 is added here to put the inside of the first floor at ground level
            int heightChange = height - this.boundingBox.minY() - 1;
            this.move(0, heightChange, 0);
            this.hasCalculatedHeightPosition = true;
            return true;
        }
    }

    @Override
    protected void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        WorldGenUtil.fillColumnDownOnAllPosts(worldGenLevel, DEEPSLATE_BRICKS, RELATIVE_POSTS, chunkBoundingBox, this.templatePosition, this.placeSettings);
    }

    protected static StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(new CrackedBlockProcessor(0.5f));
    }

}
