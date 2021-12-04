package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.armor.OdysseyArmorMaterials;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.items.equipment.EquipmentArmorItem;
import com.bedmen.odyssey.items.equipment.EquipmentMeleeItem;
import com.bedmen.odyssey.tools.OdysseyTiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Odyssey.MOD_ID);
    public static DeferredRegister<Item> ITEMS_VANILLA = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Building Blocks
    public static final RegistryObject<Item> SILVER_ORE = ITEMS.register("silver_ore", () -> new BlockItem(BlockRegistry.SILVER_ORE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> DEEPSLATE_SILVER_ORE = ITEMS.register("deepslate_silver_ore", () -> new BlockItem(BlockRegistry.DEEPSLATE_SILVER_ORE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> RAW_SILVER_BLOCK = ITEMS.register("raw_silver_block", () -> new BlockItem(BlockRegistry.RAW_SILVER_BLOCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_BLOCK = ITEMS.register("silver_block", () -> new BlockItem(BlockRegistry.SILVER_BLOCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));

    //Materials
    public static final RegistryObject<Item> FEATHER_BUNDLE = ITEMS.register("feather_bundle", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));

    // LevEnchSups for Equipment
    public static final LevEnchSup FALL_PROTECTION_1 = new LevEnchSup(EnchantmentRegistry.FALL_PROTECTION, 1);
    public static final LevEnchSup RESPIRATION_1 = new LevEnchSup(EnchantmentRegistry.RESPIRATION, 1);
    public static final LevEnchSup RESPIRATION_2 = new LevEnchSup(EnchantmentRegistry.RESPIRATION, 2);
    public static final LevEnchSup DEPTH_STRIDER_1 = new LevEnchSup(EnchantmentRegistry.DEPTH_STRIDER, 1);
    public static final LevEnchSup DEPTH_STRIDER_2 = new LevEnchSup(EnchantmentRegistry.DEPTH_STRIDER, 2);
    public static final LevEnchSup BLAST_PROTECTION_1 = new LevEnchSup(EnchantmentRegistry.BLAST_PROTECTION, 1);
    public static final LevEnchSup FIRE_PROTECTION_1 = new LevEnchSup(EnchantmentRegistry.FIRE_PROTECTION, 1);
    public static final LevEnchSup FALL_PROTECTION_2 = new LevEnchSup(EnchantmentRegistry.FALL_PROTECTION, 2);
    //    public static final LevEnchSup PYROPNEUMATIC_1 = new LevEnchSup(EnchantmentRegistry.PYROPNEUMATIC , 1);
//    public static final LevEnchSup VULCAN_STRIDER_1 = new LevEnchSup(EnchantmentRegistry.VULCAN_STRIDER, 1);
    public static final LevEnchSup AQUA_AFFINITY = new LevEnchSup(EnchantmentRegistry.AQUA_AFFINITY);
    public static final LevEnchSup BINDING = new LevEnchSup(EnchantmentRegistry.BINDING_CURSE);
    public static final LevEnchSup FORTUNE_1 = new LevEnchSup(EnchantmentRegistry.BLOCK_FORTUNE, 1);
    public static final LevEnchSup LOOTING_1 = new LevEnchSup(EnchantmentRegistry.MOB_LOOTING, 1);
    public static final LevEnchSup KNOCKBACK_1 = new LevEnchSup(EnchantmentRegistry.KNOCKBACK, 1);
    public static final LevEnchSup KNOCKBACK_2 = new LevEnchSup(EnchantmentRegistry.KNOCKBACK, 2);
    public static final LevEnchSup BANE_OF_ARTHROPODS_1 = new LevEnchSup(EnchantmentRegistry.BANE_OF_ARTHROPODS, 1);
    public static final LevEnchSup BANE_OF_ARTHROPODS_2 = new LevEnchSup(EnchantmentRegistry.BANE_OF_ARTHROPODS, 2);
    public static final LevEnchSup SHATTERING_1 = new LevEnchSup(EnchantmentRegistry.SHATTERING, 1);
    public static final LevEnchSup SHATTERING_2 = new LevEnchSup(EnchantmentRegistry.SHATTERING, 2);
    public static final LevEnchSup SWEEPING_EDGE_1 = new LevEnchSup(EnchantmentRegistry.SWEEPING_EDGE, 1);
    public static final LevEnchSup SWEEPING_EDGE_2 = new LevEnchSup(EnchantmentRegistry.SWEEPING_EDGE, 2);
    public static final LevEnchSup SWEEPING_EDGE_3 = new LevEnchSup(EnchantmentRegistry.SWEEPING_EDGE, 3);
    public static final LevEnchSup SWEEPING_EDGE_4 = new LevEnchSup(EnchantmentRegistry.SWEEPING_EDGE, 4);
    public static final LevEnchSup SMITE_1 = new LevEnchSup(EnchantmentRegistry.SMITE, 1);
    public static final LevEnchSup SMITE_2 = new LevEnchSup(EnchantmentRegistry.SMITE, 2);
    public static final LevEnchSup SMITE_3 = new LevEnchSup(EnchantmentRegistry.SMITE, 3);
    public static final LevEnchSup SMITE_4 = new LevEnchSup(EnchantmentRegistry.SMITE, 4);
    public static final LevEnchSup PIERCING_1 = new LevEnchSup(EnchantmentRegistry.PIERCING, 1);
    public static final LevEnchSup PIERCING_2 = new LevEnchSup(EnchantmentRegistry.PIERCING, 2);
    public static final LevEnchSup QUICK_CHARGE_1 = new LevEnchSup(EnchantmentRegistry.QUICK_CHARGE, 1);
    public static final LevEnchSup QUICK_CHARGE_2 = new LevEnchSup(EnchantmentRegistry.QUICK_CHARGE, 2);
    public static final LevEnchSup PUNCH_1 = new LevEnchSup(EnchantmentRegistry.PUNCH_ARROWS, 1);
    public static final LevEnchSup PUNCH_2 = new LevEnchSup(EnchantmentRegistry.PUNCH_ARROWS, 2);
    public static final LevEnchSup LOYALTY_1 = new LevEnchSup(EnchantmentRegistry.LOYALTY, 1);
    public static final LevEnchSup LOYALTY_2 = new LevEnchSup(EnchantmentRegistry.LOYALTY, 2);

    //Melee Weapons
    public static final RegistryObject<Item> COPPER_HAMMER = ITEMS.register("copper_hammer", () -> new EquipmentMeleeItem(OdysseyTiers.COPPER, 8, -3.2f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), BANE_OF_ARTHROPODS_1));
    public static final RegistryObject<Item> COPPER_BAT = ITEMS.register("copper_bat", () -> new EquipmentMeleeItem(OdysseyTiers.COPPER, 6, -2.7f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), KNOCKBACK_1));
    public static final RegistryObject<Item> COPPER_BATTLE_AXE = ITEMS.register("copper_battle_axe", () -> new EquipmentMeleeItem(OdysseyTiers.COPPER, 8, -3.1f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SHATTERING_1));

    public static final RegistryObject<Item> OBSIDIAN_HAMMER = ITEMS.register("obsidian_hammer", () -> new EquipmentMeleeItem(OdysseyTiers.OBSIDIAN, 10, -3.2f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), BANE_OF_ARTHROPODS_2));
    public static final RegistryObject<Item> OBSIDIAN_BAT = ITEMS.register("obsidian_bat", () -> new EquipmentMeleeItem(OdysseyTiers.OBSIDIAN, 8, -2.7f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), KNOCKBACK_2));
    public static final RegistryObject<Item> OBSIDIAN_BATTLE_AXE = ITEMS.register("obsidian_battle_axe", () -> new EquipmentMeleeItem(OdysseyTiers.OBSIDIAN, 10, -3.1f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SHATTERING_2));

    //Ranged Weapons
    public static final RegistryObject<Item> AMETHYST_ARROW = ITEMS.register("amethyst_arrow", () -> new OdysseyArrowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyArrow.ArrowType.AMETHYST));


    //Armors
    public static final RegistryObject<Item> CHICKEN_HELMET = ITEMS.register("chicken_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterials.CHICKEN, EquipmentSlot.HEAD, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FALL_PROTECTION_1));
    public static final RegistryObject<Item> CHICKEN_CHESTPLATE = ITEMS.register("chicken_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterials.CHICKEN, EquipmentSlot.CHEST, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FALL_PROTECTION_1));
    public static final RegistryObject<Item> CHICKEN_LEGGINGS = ITEMS.register("chicken_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterials.CHICKEN, EquipmentSlot.LEGS, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FALL_PROTECTION_1));
    public static final RegistryObject<Item> CHICKEN_BOOTS = ITEMS.register("chicken_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterials.CHICKEN, EquipmentSlot.FEET, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FALL_PROTECTION_1));

    public static final RegistryObject<Item> TURTLE_HELMET = ITEMS.register("turtle_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterials.TURTLE, EquipmentSlot.HEAD, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), RESPIRATION_1));
    public static final RegistryObject<Item> TURTLE_CHESTPLATE = ITEMS.register("turtle_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterials.TURTLE, EquipmentSlot.CHEST, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), RESPIRATION_1));
    public static final RegistryObject<Item> TURTLE_LEGGINGS = ITEMS.register("turtle_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterials.TURTLE, EquipmentSlot.LEGS, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), DEPTH_STRIDER_1));
    public static final RegistryObject<Item> TURTLE_BOOTS = ITEMS.register("turtle_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterials.TURTLE, EquipmentSlot.FEET, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), DEPTH_STRIDER_1));

    //public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterials.NETHERITE.getTotalDefense(), "netherite", (new Item.Properties()).stacksTo(1).fireResistant().tab(OdysseyCreativeModeTab.ARMOR)));

    //Spawning
    public static final RegistryObject<Item> BABY_CREEPER_SPAWN_EGG = ITEMS.register("baby_creeper_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.BABY_CREEPER, 894731, 0, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> CAMO_CREEPER_SPAWN_EGG = ITEMS.register("camo_creeper_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.CAMO_CREEPER, 0x50692c, 0x79553a, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> BABY_LEVIATHAN_SPAWN_EGG = ITEMS.register("baby_leviathan_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.BABY_LEVIATHAN, 0x2f2f37, 0x646464, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> BABY_LEVIATHAN_BUCKET = ITEMS.register("baby_leviathan_bucket", () -> new BabyLeviathanBucket((new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.SPAWNING)));

    /////////////////////////////////////////////////////////////////////////

    //Vanilla Overrides
    public static final RegistryObject<Item> LEATHER_HELMET = ITEMS_VANILLA.register("leather_helmet", () -> new DyeableArmorItem(OdysseyArmorMaterials.LEATHER, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_CHESTPLATE = ITEMS_VANILLA.register("leather_chestplate", () -> new DyeableArmorItem(OdysseyArmorMaterials.LEATHER, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_LEGGINGS = ITEMS_VANILLA.register("leather_leggings", () -> new DyeableArmorItem(OdysseyArmorMaterials.LEATHER, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_BOOTS = ITEMS_VANILLA.register("leather_boots", () -> new DyeableArmorItem(OdysseyArmorMaterials.LEATHER, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_HORSE_ARMOR = ITEMS_VANILLA.register("leather_horse_armor", () -> new DyeableHorseArmorItem(OdysseyArmorMaterials.LEATHER.getTotalDefense(), "leather", (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> CHAINMAIL_HELMET = ITEMS_VANILLA.register("chainmail_helmet", () -> new ArmorItem(OdysseyArmorMaterials.CHAIN, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_CHESTPLATE = ITEMS_VANILLA.register("chainmail_chestplate", () -> new ArmorItem(OdysseyArmorMaterials.CHAIN, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_LEGGINGS = ITEMS_VANILLA.register("chainmail_leggings", () -> new ArmorItem(OdysseyArmorMaterials.CHAIN, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_BOOTS = ITEMS_VANILLA.register("chainmail_boots", () -> new ArmorItem(OdysseyArmorMaterials.CHAIN, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistryObject<Item> IRON_HELMET = ITEMS_VANILLA.register("iron_helmet", () -> new ArmorItem(OdysseyArmorMaterials.IRON, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_CHESTPLATE = ITEMS_VANILLA.register("iron_chestplate", () -> new ArmorItem(OdysseyArmorMaterials.IRON, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_LEGGINGS = ITEMS_VANILLA.register("iron_leggings", () -> new ArmorItem(OdysseyArmorMaterials.IRON, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_BOOTS = ITEMS_VANILLA.register("iron_boots", () -> new ArmorItem(OdysseyArmorMaterials.IRON, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_HORSE_ARMOR = ITEMS_VANILLA.register("iron_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterials.IRON.getTotalDefense(), "iron", (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> GOLDEN_HELMET = ITEMS_VANILLA.register("golden_helmet", () -> new GoldArmorItem(OdysseyArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_CHESTPLATE = ITEMS_VANILLA.register("golden_chestplate", () -> new GoldArmorItem(OdysseyArmorMaterials.GOLD, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_LEGGINGS = ITEMS_VANILLA.register("golden_leggings", () -> new GoldArmorItem(OdysseyArmorMaterials.GOLD, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_BOOTS = ITEMS_VANILLA.register("golden_boots", () -> new GoldArmorItem(OdysseyArmorMaterials.GOLD, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_HORSE_ARMOR = ITEMS_VANILLA.register("golden_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterials.GOLD.getTotalDefense(), "gold", (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> DIAMOND_HELMET = ITEMS_VANILLA.register("diamond_helmet", () -> new ArmorItem(OdysseyArmorMaterials.DIAMOND, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_CHESTPLATE = ITEMS_VANILLA.register("diamond_chestplate", () -> new ArmorItem(OdysseyArmorMaterials.DIAMOND, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_LEGGINGS = ITEMS_VANILLA.register("diamond_leggings", () -> new ArmorItem(OdysseyArmorMaterials.DIAMOND, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_BOOTS = ITEMS_VANILLA.register("diamond_boots", () -> new ArmorItem(OdysseyArmorMaterials.DIAMOND, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_HORSE_ARMOR = ITEMS_VANILLA.register("diamond_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterials.DIAMOND.getTotalDefense(), "diamond", (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> NETHERITE_HELMET = ITEMS_VANILLA.register("netherite_helmet", () -> new ArmorItem(OdysseyArmorMaterials.NETHERITE, EquipmentSlot.HEAD, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_CHESTPLATE = ITEMS_VANILLA.register("netherite_chestplate", () -> new ArmorItem(OdysseyArmorMaterials.NETHERITE, EquipmentSlot.CHEST, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_LEGGINGS = ITEMS_VANILLA.register("netherite_leggings", () -> new ArmorItem(OdysseyArmorMaterials.NETHERITE, EquipmentSlot.LEGS, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_BOOTS = ITEMS_VANILLA.register("netherite_boots", () -> new ArmorItem(OdysseyArmorMaterials.NETHERITE, EquipmentSlot.FEET, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistryObject<Item> BOW = ITEMS_VANILLA.register("bow", () -> new OdysseyBowItem((new Item.Properties()).durability(150).tab(CreativeModeTab.TAB_COMBAT), 1.0f, 20));
    public static final RegistryObject<Item> ARROW = ITEMS_VANILLA.register("arrow", () -> new OdysseyArrowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyArrow.ArrowType.FLINT));
}