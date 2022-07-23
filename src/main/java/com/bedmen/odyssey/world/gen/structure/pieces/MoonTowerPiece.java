package com.bedmen.odyssey.world.gen.structure.pieces;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.OdysseySkeleton;
import com.bedmen.odyssey.loot.OdysseyLootTables;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.StructurePieceTypeRegistry;
import com.bedmen.odyssey.util.WorldGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;

public class MoonTowerPiece extends TemplateStructurePiece {
    private static final ResourceLocation STRUCTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"moon_tower/moon_tower");

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

    private boolean isValidTowerPosition(WorldGenLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos  mutx = pos.mutable();
        for (int x = 0; x < 5; x++){
            mutx.move(Direction.EAST, 1);
            if (WorldGenUtil.isEmpty(level, mutx))
                return false;
        }
        mutx.move(Direction.EAST, 1);
        BlockPos.MutableBlockPos mutz = pos.mutable();
        for (int z = 0; z < 5; z++) {
            mutx.move(Direction.SOUTH, 1);
            mutz.move(Direction.SOUTH, 1);
            if (WorldGenUtil.isEmpty(level, mutx) || WorldGenUtil.isEmpty(level, mutz))
                return false;
        }
        mutz.move(Direction.SOUTH, 1);
        for (int x = 0; x < 5; x++) {
            mutz.move(Direction.EAST, 1);
            if (WorldGenUtil.isEmpty(level, mutz))
                return false;
        }
        return true;
    }


    @Override
    public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        for(int y = 200; y > 90; y--){
            if (isValidTowerPosition(level, mutable.immutable())) {
                mutable.move(0, -1, 0);
                this.templatePosition = mutable.immutable();
                System.out.print(this.templatePosition.getX());
                System.out.print(",");
                System.out.println(this.templatePosition.getZ());
                System.out.print(level.getChunk(this.templatePosition).getPos().getMinBlockX());
                System.out.print(",");
                System.out.println(level.getChunk(this.templatePosition).getPos().getMinBlockZ());

                super.postProcess(level, manager, chunkGenerator, random, boundingBox, chunkPos, pos);
                return;
            }
            mutable.move(0,-1,0);
        }
    }

    private static ItemStack itemWithEnchantment(Item item, Random random){
        ItemStack itemStack = new ItemStack(item);
        if (random.nextBoolean() || random.nextBoolean())
            itemStack.enchant(EnchantmentRegistry.ALL_DAMAGE_PROTECTION.get(), 1);
        return itemStack;
    }

    @Override
    protected void handleDataMarker(String dataMarker, BlockPos blockPos, ServerLevelAccessor accessor, Random random, BoundingBox boundingBox) {
        if ("chests_anchor".equals(dataMarker)) {
            accessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
            BlockPos[] chests = {blockPos.above(3), blockPos.below(4), new BlockPos(2, 0, 2).rotate(getRotation()).offset(blockPos)};
            int loot = random.nextInt(3);
            for (int i = 0; i < 3; i++) {
                BlockEntity blockentity = accessor.getBlockEntity(chests[i]);
                if (i == loot)
                    ((ChestBlockEntity) blockentity).setLootTable(OdysseyLootTables.MOON_TOWER_CHEST, random.nextLong());
                else
                    ((ChestBlockEntity) blockentity).setLootTable(OdysseyLootTables.STERLING_SILVER_TREASURE_CHEST, random.nextLong());
                System.out.println("Lootable Added");
            }
        } else if ("zombie".equals(dataMarker) || "beefy_zombie".equals(dataMarker)) {
            Zombie zombie = EntityType.ZOMBIE.create(accessor.getLevel());
            zombie.setPersistenceRequired();
            zombie.moveTo(blockPos, 0.0F, 0.0F);

            if (zombie.getItemBySlot(EquipmentSlot.HEAD).isEmpty())
                zombie.setItemSlot(EquipmentSlot.HEAD, itemWithEnchantment(ItemRegistry.STERLING_SILVER_HELMET.get(), random));
            if (zombie.getItemBySlot(EquipmentSlot.FEET).isEmpty() && (random.nextBoolean() || random.nextBoolean()))
                zombie.setItemSlot(EquipmentSlot.FEET,  itemWithEnchantment(ItemRegistry.STERLING_SILVER_BOOTS.get(), random));

            if ("beefy_zombie".equals(dataMarker) && zombie.getItemBySlot(EquipmentSlot.CHEST).isEmpty())
                zombie.setItemSlot(EquipmentSlot.CHEST, itemWithEnchantment(ItemRegistry.STERLING_SILVER_CHESTPLATE.get(), random));

            zombie.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.STERLING_SILVER_SWORD.get()));
            zombie.finalizeSpawn(accessor, accessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, (SpawnGroupData)null, (CompoundTag)null);
            accessor.addFreshEntityWithPassengers(zombie);
            accessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
        } else if ("skeleton".equals(dataMarker)) {
            OdysseySkeleton skeleton = EntityTypeRegistry.SKELETON.get().create(accessor.getLevel());
            skeleton.setPersistenceRequired();
            skeleton.moveTo(blockPos, 0.0F, 0.0F);

            if (skeleton.getItemBySlot(EquipmentSlot.HEAD).isEmpty())
                skeleton.setItemSlot(EquipmentSlot.HEAD, itemWithEnchantment(ItemRegistry.STERLING_SILVER_HELMET.get(), random));
            if (skeleton.getItemBySlot(EquipmentSlot.LEGS).isEmpty() && random.nextBoolean())
                skeleton.setItemSlot(EquipmentSlot.LEGS, itemWithEnchantment(ItemRegistry.STERLING_SILVER_LEGGINGS.get(), random));

            skeleton.finalizeSpawn(accessor, accessor.getCurrentDifficultyAt(blockPos), MobSpawnType.STRUCTURE, (SpawnGroupData)null, (CompoundTag)null);
            skeleton.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.BOWN.get()));

            accessor.addFreshEntityWithPassengers(skeleton);
            accessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
}
