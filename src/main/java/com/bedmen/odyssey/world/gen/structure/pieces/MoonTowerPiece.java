package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectTierManager;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.structure.StructurePieceTypeRegistry;
import com.bedmen.odyssey.world.WorldGenUtil;
import com.bedmen.odyssey.world.gen.processor.CrackedBlockProcessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class MoonTowerPiece extends HeightAdjustingPiece {
    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"moon_tower");
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
    private static final List<BlockPos> RELATIVE_CHESTS = List.of(
            new BlockPos(4, 7, 4),
            new BlockPos(6, 11, 6),
            new BlockPos(4, 14, 4)
    );
    private static final List<Pair<BlockPos, MoonTowerEnemyType>> ENEMY_INFO = List.of(
            Pair.of(new BlockPos(2, 1, 4), MoonTowerEnemyType.ZOMBIE),
            Pair.of(new BlockPos(6, 1, 4), MoonTowerEnemyType.ZOMBIE),
            Pair.of(new BlockPos(5, 7, 4), MoonTowerEnemyType.SKELETON),
            Pair.of(new BlockPos(3, 7, 3), MoonTowerEnemyType.BEEFY_ZOMBIE),
            Pair.of(new BlockPos(3, 7, 5), MoonTowerEnemyType.BEEFY_ZOMBIE),
            Pair.of(new BlockPos(3, 11, 4), MoonTowerEnemyType.SKELETON),
            Pair.of(new BlockPos(5, 11, 4), MoonTowerEnemyType.BEEFY_ZOMBIE),
            Pair.of(new BlockPos(5, 14, 4), MoonTowerEnemyType.ZOMBIE),
            Pair.of(new BlockPos(2, 18, 2), MoonTowerEnemyType.SKELETON),
            Pair.of(new BlockPos(2, 18, 6), MoonTowerEnemyType.SKELETON),
            Pair.of(new BlockPos(6, 18, 6), MoonTowerEnemyType.SKELETON)

    );
    private static final BlockPos RELATIVE_ENTRANCE = new BlockPos(4,0,0);

    protected byte[] hasSpawnedMonsters = new byte[ENEMY_INFO.size()];
    private static final String HAS_SPAWNED_MONSTERS_TAG = "HasSpawnedMonsters";

    public MoonTowerPiece(StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation) {
        super(StructurePieceTypeRegistry.MOON_TOWER.get(), 0, structureTemplateManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(), blockPos, rotation);
    }

    public MoonTowerPiece(StructureTemplateManager structureTemplateManager, CompoundTag compoundTag) {
        super(StructurePieceTypeRegistry.MOON_TOWER.get(), compoundTag, structureTemplateManager, (resourceLocation) -> makeSettings());
        this.hasSpawnedMonsters = compoundTag.getByteArray(HAS_SPAWNED_MONSTERS_TAG);
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
        super.addAdditionalSaveData(context, compoundTag);
        compoundTag.putByteArray(HAS_SPAWNED_MONSTERS_TAG, this.hasSpawnedMonsters);
    }

    private static StructurePlaceSettings makeSettings() {
        return (new StructurePlaceSettings()).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(new CrackedBlockProcessor(0.5f));
    }

    protected boolean updateHeightPosition(LevelAccessor levelAccessor) {
        if (this.hasCalculatedHeightPosition) {
            return true;
        } else {
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
    }

    @Override
    protected void postProcessAfterHeightUpdate(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBoundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        WorldGenUtil.fillColumnDownOnAllPosts(worldGenLevel, DEEPSLATE_BRICKS, RELATIVE_POSTS, chunkBoundingBox, this.templatePosition, this.placeSettings);
        // Set chest loot tables, 50% of being 2 (on top), 25% of 1 (middle) or 0 (bottom)
        int lootFloor = Integer.min(2, randomSource.nextInt(4));
        for(int i = 0; i < 3; i++){
            BlockPos chestBlockPos = WorldGenUtil.getWorldPosition(RELATIVE_CHESTS.get(i), this.templatePosition, this.placeSettings);
            BlockEntity blockEntity = worldGenLevel.getBlockEntity(chestBlockPos);
            if(blockEntity instanceof ChestBlockEntity chestBlockEntity){
                ResourceLocation lootTable = lootFloor == i ? OdysseyLootTables.MOON_TOWER_CHEST : OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST;
                chestBlockEntity.setLootTable(lootTable, randomSource.nextLong());
            }
        }
        spawnMonsters(worldGenLevel, chunkBoundingBox);
    }

    private void spawnMonsters(ServerLevelAccessor serverLevelAccessor, BoundingBox chunkBoundingBox) {
        for(int i = 0; i < ENEMY_INFO.size(); i++){
            if(this.hasSpawnedMonsters[i] == 0){
                Pair<BlockPos, MoonTowerEnemyType> pair = ENEMY_INFO.get(i);
                BlockPos blockPos = WorldGenUtil.getWorldPosition(pair.getFirst(), this.templatePosition, this.placeSettings);
                if(chunkBoundingBox.isInside(blockPos)){
                    this.hasSpawnedMonsters[i] = 1;
                    MoonTowerEnemyType moonTowerEnemyType = pair.getSecond();
                    Mob dungeonMonster = moonTowerEnemyType.entityType.create(serverLevelAccessor.getLevel());
                    if(dungeonMonster != null){
                        moonTowerEnemyType.equipper.accept(dungeonMonster, serverLevelAccessor.getRandom());
                        WorldGenUtil.addEntityToStructure(dungeonMonster, blockPos, serverLevelAccessor);
                    }
                }
            }

        }
    }

    private enum MoonTowerEnemyType{
        ZOMBIE(EntityTypeRegistry.MOON_TOWER_ZOMBIE.get(), (livingEntity, random) -> {
            livingEntity.setItemSlot(EquipmentSlot.HEAD, AspectTierManager.itemModifyByTier(ItemRegistry.STERLING_SILVER_HELMET.get(), random , 2, 0.5f, false));
            livingEntity.setItemSlot(EquipmentSlot.LEGS, AspectTierManager.itemModifyByTier(ItemRegistry.STERLING_SILVER_LEGGINGS.get(), random , 2, 0.5f, false));
            livingEntity.setItemSlot(EquipmentSlot.FEET, AspectTierManager.itemModifyByTier(ItemRegistry.STERLING_SILVER_BOOTS.get(), random , 2, 0.5f, false));
            livingEntity.setItemInHand(InteractionHand.MAIN_HAND, AspectTierManager.itemModifyByTier(ItemRegistry.STERLING_SILVER_MACE.get(), random, 2, 0.5f, false));
        }),
        BEEFY_ZOMBIE(EntityTypeRegistry.MOON_TOWER_ZOMBIE.get(), (livingEntity, random) -> {
            ZOMBIE.equipper.accept(livingEntity, random);
            livingEntity.setItemSlot(EquipmentSlot.CHEST, AspectTierManager.itemModifyByTier(ItemRegistry.STERLING_SILVER_CHESTPLATE.get(), random , 2, 0.5f, false));
        }),
        SKELETON(EntityTypeRegistry.MOON_TOWER_SKELETON.get(), (livingEntity, random) -> {
            ZOMBIE.equipper.accept(livingEntity, random);
            livingEntity.setItemInHand(InteractionHand.MAIN_HAND, ItemRegistry.BOWN.get().getDefaultInstance());
        });

        public EntityType<? extends Mob> entityType;
        public BiConsumer<LivingEntity, RandomSource> equipper;
        MoonTowerEnemyType(EntityType<? extends Mob> entityType,  BiConsumer<LivingEntity, RandomSource> equipper){
            this.entityType = entityType;
            this.equipper = equipper;
        }
    }
}
