package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.structure.StructurePieceTypeRegistry;
import com.bedmen.odyssey.world.WorldGenUtil;
import com.bedmen.odyssey.world.gen.processor.CrackedBlockProcessor;
import com.bedmen.odyssey.world.gen.processor.MossyBlockProcessor;
import com.bedmen.odyssey.world.gen.processor.VineProcessor;
import com.bedmen.odyssey.world.gen.processor.WoodProcessor;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class LoneWarWatchTowerPiece extends HeightAdjustingPiece {

    // This allows us to have generally more explicitly ruined watch towers.
    public static final SimpleWeightedRandomList<ResourceLocation> STRUCTURE_LOCATIONS = SimpleWeightedRandomList.<ResourceLocation>builder()
            .add(new ResourceLocation(Odyssey.MOD_ID,"lone_war_watch_tower"), 1)
            .add(new ResourceLocation(Odyssey.MOD_ID,"ruined_lone_war_watch_tower"), 3).build();

    private static final BlockPos RELATIVE_CHEST = new BlockPos(6,22,4);

    public LoneWarWatchTowerPiece(StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation, ResourceLocation location) {
        super(StructurePieceTypeRegistry.LONE_WAR_WATCH_TOWER.get(), 0, structureTemplateManager, location, location.toString(), makeSettings(), blockPos, rotation);
    }

    public LoneWarWatchTowerPiece(StructureTemplateManager structureTemplateManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.LONE_WAR_WATCH_TOWER.get(), compoundTag, structureTemplateManager, (resourceLocation) -> makeSettings());
    }

    private static StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(new MossyBlockProcessor(0.33f)).addProcessor(new CrackedBlockProcessor(0.6f)).addProcessor(new WoodProcessor()).addProcessor(new VineProcessor(0.7f));
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
    }

    /** This is identical to BasicRunisPiece::updateHeightPosition. Consider unifying/unification */
    @Override
    protected boolean updateHeightPosition(LevelAccessor levelAccessor) {
        return reduceHeightFromBBOXHeightmap(levelAccessor, Heightmap.Types.OCEAN_FLOOR_WG, levelAccessor.getMaxBuildHeight(), (a,b) -> Math.min(a,b));
    }

    @Override
    protected void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        BlockPos chestBlockPos = WorldGenUtil.getWorldPosition(RELATIVE_CHEST, this.templatePosition, this.placeSettings);
        BlockEntity blockEntity = worldGenLevel.getBlockEntity(chestBlockPos);

        if (blockEntity instanceof TreasureChestBlockEntity){
            ResourceLocation lootTable = OdysseyLootTables.LONE_WAR_WATCH_TOWER;
            RandomizableContainerBlockEntity.setLootTable(worldGenLevel, randomSource, chestBlockPos, lootTable);
        }
    }
}
