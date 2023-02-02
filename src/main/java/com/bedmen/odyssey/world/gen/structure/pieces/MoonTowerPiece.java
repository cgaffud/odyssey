package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectTierManager;
import com.bedmen.odyssey.entity.monster.DungeonSkeleton;
import com.bedmen.odyssey.entity.monster.DungeonZombie;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.util.WorldGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
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

import java.util.Map;
import java.util.Random;

public class MoonTowerPiece extends TemplateStructurePiece {
    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"moon_tower/moon_tower");
    private static final SimpleWeightedRandomList<Block> DEEPSLATE_BRICKS = new SimpleWeightedRandomList.Builder<Block>()
            .add(Blocks.DEEPSLATE_BRICKS, 1)
            .add(Blocks.CRACKED_DEEPSLATE_BRICKS, 1).build();
    private static final SimpleWeightedRandomList<Block> DEEPSLATE_TILES = new SimpleWeightedRandomList.Builder<Block>()
            .add(Blocks.DEEPSLATE_TILES, 1)
            .add(Blocks.CRACKED_DEEPSLATE_TILES, 1).build();
    private static final Map<Block, SimpleWeightedRandomList<Block>> BLOCK_MAP = Map.of(Blocks.DEEPSLATE_BRICKS, DEEPSLATE_BRICKS, Blocks.DEEPSLATE_TILES, DEEPSLATE_TILES);

    public MoonTowerPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation) {
        super(StructurePieceTypeRegistry.MOON_TOWER.get(), 0, structureManager, STRUCTURE_LOCATION, STRUCTURE_LOCATION.toString(), makeSettings(rotation), blockPos);
    }

    public MoonTowerPiece(StructureManager structureManager, CompoundTag tag) {
        super(StructurePieceTypeRegistry.MOON_TOWER.get(), tag, structureManager, (loc) -> {
            return makeSettings(Rotation.valueOf(tag.getString("Rot")));
        });
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putString("Rot", this.placeSettings.getRotation().name());
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        BlockPos entrance = new BlockPos(0, 0, -4).rotate(getRotation()).offset(pos);
        int height = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, entrance.getX(), entrance.getZ());
        BlockPos blockpos2 = this.templatePosition;
        int y = height - 200 - 1;
        this.templatePosition = this.templatePosition.offset(0, y, 0);
        BlockPos.MutableBlockPos mutable = BlockPos.ZERO.mutable();
        for(int x = -3; x <= 3; x++){
            for(int z = -3; z <= 3; z++){
                if(Math.abs(x+z) == 6 || Math.abs(x-z) == 6) {
                    continue;
                }
                mutable.setWithOffset(pos, x, y-1, z);
                while(WorldGenUtil.isEmpty(level, mutable)) {
                    level.setBlock(mutable, DEEPSLATE_BRICKS.getRandomValue(random).get().defaultBlockState(), 3);
                    mutable.move(Direction.DOWN);
                }
            }
        }
        super.postProcess(level, manager, chunkGenerator, random, boundingBox, chunkPos, pos);
        for(int x1 = this.boundingBox.minX(); x1 <= this.boundingBox.maxX(); x1++){
            for(int y1 = this.boundingBox.minY(); y1 <= this.boundingBox.maxY(); y1++){
                for(int z1 = this.boundingBox.minZ(); z1 <= this.boundingBox.maxZ(); z1++){
                    BlockPos blockPos1 = new BlockPos(x1, y1, z1);
                    BlockState blockState = level.getBlockState(blockPos1);
                    Block block = blockState.getBlock();
                    if(BLOCK_MAP.containsKey(block)){
                        SimpleWeightedRandomList<Block> simpleWeightedRandomList = BLOCK_MAP.get(block);
                        Block block1 = simpleWeightedRandomList.getRandomValue(random).get();
                        BlockState blockState1 = block1.withPropertiesOf(blockState);
                        level.setBlock(blockPos1, blockState1, 3);
                    }
                }
            }
        }
        this.templatePosition = blockpos2;
    }

//    private static ItemStack itemWithEnchantment(Item item, Random random){
//        ItemStack itemStack = new ItemStack(item);
//        if (random.nextBoolean() || random.nextBoolean())
//            itemStack.enchant(EnchantmentRegistry.ALL_DAMAGE_PROTECTION.get(), 1);
//        return itemStack;
//    }

    @Override
    protected void handleDataMarker(String dataMarker, BlockPos blockPos, ServerLevelAccessor accessor, Random random, BoundingBox boundingBox) {
        if ("chests_anchor".equals(dataMarker)) {
            accessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            BlockPos[] chests = {blockPos.above(3), blockPos.below(4), new BlockPos(2, 0, 2).rotate(getRotation()).offset(blockPos)};
            int loot;
            if (random.nextBoolean())
                loot = 0;
            else
                loot = random.nextInt(2) + 1;

            for (int i = 0; i < 3; i++) {
                BlockEntity blockentity = accessor.getBlockEntity(chests[i]);
                if (i == loot)
                    ((ChestBlockEntity) blockentity).setLootTable(OdysseyLootTables.MOON_TOWER_CHEST, random.nextLong());
                else
                    ((ChestBlockEntity) blockentity).setLootTable(OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST, random.nextLong());
            }
        } else if ("zombie".equals(dataMarker) || "beefy_zombie".equals(dataMarker)) {
            DungeonZombie zombie = new DungeonZombie(EntityTypeRegistry.MOON_TOWER_ZOMBIE.get(), accessor.getLevel(), blockPos);
            zombie.setPersistenceRequired();
            zombie.moveTo(blockPos, 0.0F, 0.0F);

            if (zombie.getItemBySlot(EquipmentSlot.HEAD).isEmpty())
                zombie.setItemSlot(EquipmentSlot.HEAD, AspectTierManager.itemBuffedByTier(ItemRegistry.STERLING_SILVER_HELMET.get(), random , 1, 0.75));
            if (zombie.getItemBySlot(EquipmentSlot.FEET).isEmpty())
                zombie.setItemSlot(EquipmentSlot.FEET,  AspectTierManager.itemBuffedByTier(ItemRegistry.STERLING_SILVER_BOOTS.get(), random, 1, 0.75));
            if (zombie.getItemBySlot(EquipmentSlot.LEGS).isEmpty() && (random.nextBoolean() || random.nextBoolean()))
                zombie.setItemSlot(EquipmentSlot.LEGS,  AspectTierManager.itemBuffedByTier(ItemRegistry.STERLING_SILVER_LEGGINGS.get(), random, 1, 0.75));

            if ("beefy_zombie".equals(dataMarker) && zombie.getItemBySlot(EquipmentSlot.CHEST).isEmpty())
                zombie.setItemSlot(EquipmentSlot.CHEST, AspectTierManager.itemBuffedByTier(ItemRegistry.STERLING_SILVER_CHESTPLATE.get(), random, 1, 0.75));

            zombie.setItemInHand(InteractionHand.MAIN_HAND, AspectTierManager.itemBuffedByTier(ItemRegistry.STERLING_SILVER_MACE.get(), random, 1, 1.0));

            zombie.finalizeSpawn(accessor, accessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, (SpawnGroupData)null, (CompoundTag)null);
            accessor.addFreshEntityWithPassengers(zombie);
            accessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
        } else if ("skeleton".equals(dataMarker)) {
            DungeonSkeleton skeleton = new DungeonSkeleton(EntityTypeRegistry.MOON_TOWER_SKELETON.get(), accessor.getLevel(), blockPos);
            skeleton.setPersistenceRequired();
            skeleton.moveTo(blockPos, 0.0F, 0.0F);

            if (skeleton.getItemBySlot(EquipmentSlot.HEAD).isEmpty())
                skeleton.setItemSlot(EquipmentSlot.HEAD, AspectTierManager.itemBuffedByTier(ItemRegistry.STERLING_SILVER_HELMET.get(), random, 1, 0.75));
            if (skeleton.getItemBySlot(EquipmentSlot.LEGS).isEmpty())
                skeleton.setItemSlot(EquipmentSlot.LEGS, AspectTierManager.itemBuffedByTier(ItemRegistry.STERLING_SILVER_LEGGINGS.get(), random, 1, 0.75));
            if (skeleton.getItemBySlot(EquipmentSlot.FEET).isEmpty() && (random.nextBoolean() || random.nextBoolean()))
                skeleton.setItemSlot(EquipmentSlot.FEET, AspectTierManager.itemBuffedByTier(ItemRegistry.STERLING_SILVER_BOOTS.get(), random, 1, 0.75));

            skeleton.finalizeSpawn(accessor, accessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, (SpawnGroupData)null, (CompoundTag)null);
            skeleton.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.BOWN.get()));

            accessor.addFreshEntityWithPassengers(skeleton);
            accessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
}
