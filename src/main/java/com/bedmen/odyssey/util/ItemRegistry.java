package com.bedmen.odyssey.util;

import com.bedmen.odyssey.armor.OdysseyArmorMaterial;
import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.equipment.*;
import com.bedmen.odyssey.tools.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.Items; //Used as easy access to item registry

import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS , Odyssey.MOD_ID);
    public static DeferredRegister<Item> ITEMS_VANILLA = DeferredRegister.create(ForgeRegistries.ITEMS , "minecraft");

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Block Items
    public static final RegistryObject<Item> BEACON = ITEMS_VANILLA.register("beacon", () -> new BlockItem(BlockRegistry.BEACON.get(), (new Item.Properties()).tab(ItemGroup.TAB_MISC).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> SMITHING_TABLE = ITEMS_VANILLA.register("smithing_table", () -> new BlockItem(BlockRegistry.SMITHING_TABLE.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> ALLOY_FURNACE = ITEMS.register("alloy_furnace", () -> new BlockItem(BlockRegistry.ALLOY_FURNACE.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> ENCHANTING_TABLE = ITEMS_VANILLA.register("enchanting_table", () -> new BlockItem(BlockRegistry.ENCHANTING_TABLE.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BOOKSHELF = ITEMS_VANILLA.register("bookshelf", () -> new BlockItem(BlockRegistry.BOOKSHELF.get(), (new Item.Properties()).tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> RECYCLE_FURNACE = ITEMS.register("recycle_furnace", () -> new BlockItem(BlockRegistry.RECYCLE_FURNACE.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> RESEARCH_TABLE = ITEMS.register("research_table", () -> new BlockItem(BlockRegistry.RESEARCH_TABLE.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> COPPER_ORE = ITEMS.register("copper_ore", () -> new BlockItem(BlockRegistry.COPPER_ORE.get(), (new Item.Properties()).tab(Odyssey.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_ORE = ITEMS.register("silver_ore", () -> new BlockItem(BlockRegistry.SILVER_ORE.get(), (new Item.Properties()).tab(Odyssey.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> RUBY_ORE = ITEMS.register("ruby_ore", () -> new BlockItem(BlockRegistry.RUBY_ORE.get(), (new Item.Properties()).tab(Odyssey.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SAPPHIRE_ORE = ITEMS.register("sapphire_ore", () -> new BlockItem(BlockRegistry.SAPPHIRE_ORE.get(), (new Item.Properties()).tab(Odyssey.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_BLOCK = ITEMS.register("silver_block", () -> new BlockItem(BlockRegistry.SILVER_BLOCK.get(), (new Item.Properties()).tab(Odyssey.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STERLING_SILVER_BLOCK = ITEMS.register("sterling_silver_block", () -> new BlockItem(BlockRegistry.STERLING_SILVER_BLOCK.get(), (new Item.Properties()).tab(Odyssey.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> LEATHER_PILE = ITEMS.register("leather_pile", () -> new BlockItem(BlockRegistry.LEATHER_PILE.get(), (new Item.Properties()).tab(Odyssey.BUILDING_BLOCKS)));

    public static final RegistryObject<Item> FOG1 = ITEMS.register("fog1", () -> new BlockItem(BlockRegistry.FOG1.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG2 = ITEMS.register("fog2", () -> new BlockItem(BlockRegistry.FOG2.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG3 = ITEMS.register("fog3", () -> new BlockItem(BlockRegistry.FOG3.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG4 = ITEMS.register("fog4", () -> new BlockItem(BlockRegistry.FOG4.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG5 = ITEMS.register("fog5", () -> new BlockItem(BlockRegistry.FOG5.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG6 = ITEMS.register("fog6", () -> new BlockItem(BlockRegistry.FOG6.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG7 = ITEMS.register("fog7", () -> new BlockItem(BlockRegistry.FOG7.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG8 = ITEMS.register("fog8", () -> new BlockItem(BlockRegistry.FOG8.get(), (new Item.Properties()).tab(Odyssey.DECORATION_BLOCKS)));

    //Items

    //Emeralds
    public static final RegistryObject<Item> EMERALD_BIT = ITEMS.register("emerald_bit", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> EMERALD_SHARD = ITEMS.register("emerald_shard", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> EMERALD_FRAGMENT = ITEMS.register("emerald_fragment", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> EMERALD_PIECE = ITEMS.register("emerald_piece", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));

    public static final RegistryObject<Item> POTION = ITEMS_VANILLA.register("potion", () -> new OdysseyPotionItem((new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_BREWING)));
    public static final RegistryObject<Item> SPLASH_POTION = ITEMS_VANILLA.register("splash_potion", () -> new OdysseySplashPotionItem((new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_BREWING)));
    public static final RegistryObject<Item> LINGERING_POTION = ITEMS_VANILLA.register("lingering_potion", () -> new OdysseyLingeringPotionItem((new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_BREWING)));
    public static final RegistryObject<Item> DIAMOND_SHARD = ITEMS.register("diamond_shard", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> RAW_IRON = ITEMS.register("raw_iron", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> RAW_COPPER = ITEMS.register("raw_copper", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> RAW_GOLD = ITEMS.register("raw_gold", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> STERLING_SILVER_NUGGET = ITEMS.register("sterling_silver_nugget", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> STERLING_SILVER_INGOT = ITEMS.register("sterling_silver_ingot", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> CLOVER = ITEMS.register("clover", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> PERIDOT = ITEMS.register("peridot", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> SERPENT_SCALE = ITEMS.register("serpent_scale", () -> new Item((new Item.Properties()).fireResistant().tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PEARL = ITEMS.register("pearl", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> NETHERITE_NUGGET = ITEMS.register("netherite_nugget", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> HALLOWED_GOLD_INGOT = ITEMS.register("hallowed_gold_ingot", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> FEATHER_BUNDLE = ITEMS.register("feather_bundle", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> PERMAFROST_SHARD = ITEMS.register("permafrost_shard", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> ARCTIC_HEART = ITEMS.register("arctic_heart", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> WEREWOLF_CLAW = ITEMS.register("werewolf_claw", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> ARCTIC_HORN = ITEMS.register("arctic_horn", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> RAVEN_FEATHER = ITEMS.register("raven_feather", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS)));
    public static final RegistryObject<Item> BEWITCHED_QUILL = ITEMS.register("bewitched_quill", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> MALEVOLENT_QUILL = ITEMS.register("malevolent_quill", () -> new Item((new Item.Properties()).tab(Odyssey.MATERIALS).rarity(Rarity.UNCOMMON)));

    //Armor

    public static final RegistryObject<Item> LEATHER_HELMET = ITEMS_VANILLA.register("leather_helmet", () -> new DyeableArmorItem(OdysseyArmorMaterial.LEATHER, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_CHESTPLATE = ITEMS_VANILLA.register("leather_chestplate", () -> new DyeableArmorItem(OdysseyArmorMaterial.LEATHER, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_LEGGINGS = ITEMS_VANILLA.register("leather_leggings", () -> new DyeableArmorItem(OdysseyArmorMaterial.LEATHER, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_BOOTS = ITEMS_VANILLA.register("leather_boots", () -> new DyeableArmorItem(OdysseyArmorMaterial.LEATHER, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_HORSE_ARMOR = ITEMS_VANILLA.register("leather_horse_armor", () -> new DyeableHorseArmorItem(10, "leather", (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> CHICKEN_HELMET = ITEMS.register("chicken_helmet", () -> new ChickenArmorItem(OdysseyArmorMaterial.CHICKEN, EquipmentSlotType.HEAD, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> CHICKEN_CHESTPLATE = ITEMS.register("chicken_chestplate", () -> new ChickenArmorItem(OdysseyArmorMaterial.CHICKEN, EquipmentSlotType.CHEST, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> CHICKEN_LEGGINGS = ITEMS.register("chicken_leggings", () -> new ChickenArmorItem(OdysseyArmorMaterial.CHICKEN, EquipmentSlotType.LEGS, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> CHICKEN_BOOTS = ITEMS.register("chicken_boots", () -> new ChickenArmorItem(OdysseyArmorMaterial.CHICKEN, EquipmentSlotType.FEET, new Item.Properties().tab(Odyssey.COMBAT)));

    public static final RegistryObject<Item> CHAINMAIL_HELMET = ITEMS_VANILLA.register("chainmail_helmet", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_CHESTPLATE = ITEMS_VANILLA.register("chainmail_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_LEGGINGS = ITEMS_VANILLA.register("chainmail_leggings", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_BOOTS = ITEMS_VANILLA.register("chainmail_boots", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<Item> IRON_HELMET = ITEMS_VANILLA.register("iron_helmet", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_CHESTPLATE = ITEMS_VANILLA.register("iron_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_LEGGINGS = ITEMS_VANILLA.register("iron_leggings", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_BOOTS = ITEMS_VANILLA.register("iron_boots", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_HORSE_ARMOR = ITEMS_VANILLA.register("iron_horse_armor", () -> new HorseArmorItem(24, "iron", (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> TURTLE_HELMET = ITEMS_VANILLA.register("turtle_helmet", () -> new UpperTurtleArmorItem(OdysseyArmorMaterial.TURTLE, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> TURTLE_CHESTPLATE = ITEMS.register("turtle_chestplate", () -> new UpperTurtleArmorItem(OdysseyArmorMaterial.TURTLE, EquipmentSlotType.CHEST, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> TURTLE_LEGGINGS = ITEMS.register("turtle_leggings", () -> new LowerTurtleArmorItem(OdysseyArmorMaterial.TURTLE, EquipmentSlotType.LEGS, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> TURTLE_BOOTS = ITEMS.register("turtle_boots", () -> new LowerTurtleArmorItem(OdysseyArmorMaterial.TURTLE, EquipmentSlotType.FEET, new Item.Properties().tab(Odyssey.COMBAT)));

    public static final RegistryObject<Item> GOLDEN_HELMET = ITEMS_VANILLA.register("golden_helmet", () -> new ArmorItem(OdysseyArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_CHESTPLATE = ITEMS_VANILLA.register("golden_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.GOLD, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_LEGGINGS = ITEMS_VANILLA.register("golden_leggings", () -> new ArmorItem(OdysseyArmorMaterial.GOLD, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_BOOTS = ITEMS_VANILLA.register("golden_boots", () -> new ArmorItem(OdysseyArmorMaterial.GOLD, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_HORSE_ARMOR = ITEMS_VANILLA.register("golden_horse_armor", () -> new HorseArmorItem(28, "gold", (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));
    
    public static final RegistryObject<Item> STERLING_SILVER_HELMET = ITEMS.register("sterling_silver_helmet", () -> new ArmorItem(OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlotType.HEAD, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_CHESTPLATE = ITEMS.register("sterling_silver_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlotType.CHEST, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_LEGGINGS = ITEMS.register("sterling_silver_leggings", () -> new ArmorItem(OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlotType.LEGS, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_BOOTS = ITEMS.register("sterling_silver_boots", () -> new ArmorItem(OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlotType.FEET, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_HORSE_ARMOR = ITEMS.register("sterling_silver_horse_armor", () -> new HorseArmorItem(30, "sterling_silver", (new Item.Properties()).stacksTo(1).tab(Odyssey.COMBAT)));

    public static final RegistryObject<Item> REINFORCED_HELMET = ITEMS.register("reinforced_helmet", () -> new ReinforcedArmorItem(OdysseyArmorMaterial.REINFORCED, EquipmentSlotType.HEAD, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> REINFORCED_CHESTPLATE = ITEMS.register("reinforced_chestplate", () -> new ReinforcedArmorItem(OdysseyArmorMaterial.REINFORCED, EquipmentSlotType.CHEST, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> REINFORCED_LEGGINGS = ITEMS.register("reinforced_leggings", () -> new ReinforcedArmorItem(OdysseyArmorMaterial.REINFORCED, EquipmentSlotType.LEGS, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> REINFORCED_BOOTS = ITEMS.register("reinforced_boots", () -> new ReinforcedArmorItem(OdysseyArmorMaterial.REINFORCED, EquipmentSlotType.FEET, new Item.Properties().tab(Odyssey.COMBAT)));

    public static final RegistryObject<Item> DIAMOND_HELMET = ITEMS_VANILLA.register("diamond_helmet", () -> new ArmorItem(OdysseyArmorMaterial.DIAMOND, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_CHESTPLATE = ITEMS_VANILLA.register("diamond_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.DIAMOND, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_LEGGINGS = ITEMS_VANILLA.register("diamond_leggings", () -> new ArmorItem(OdysseyArmorMaterial.DIAMOND, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_BOOTS = ITEMS_VANILLA.register("diamond_boots", () -> new ArmorItem(OdysseyArmorMaterial.DIAMOND, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_HORSE_ARMOR = ITEMS_VANILLA.register("diamond_horse_armor", () -> new HorseArmorItem(40, "diamond", (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> ARCTIC_HELMET = ITEMS.register("arctic_helmet", () -> new ArcticArmorItem(OdysseyArmorMaterial.ARCTIC, EquipmentSlotType.HEAD, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> ARCTIC_CHESTPLATE = ITEMS.register("arctic_chestplate", () -> new ArcticArmorItem(OdysseyArmorMaterial.ARCTIC, EquipmentSlotType.CHEST, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> ARCTIC_LEGGINGS = ITEMS.register("arctic_leggings", () -> new ArcticArmorItem(OdysseyArmorMaterial.ARCTIC, EquipmentSlotType.LEGS, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> ARCTIC_BOOTS = ITEMS.register("arctic_boots", () -> new ArcticArmorItem(OdysseyArmorMaterial.ARCTIC, EquipmentSlotType.FEET, new Item.Properties().tab(Odyssey.COMBAT)));

    public static final RegistryObject<Item> NETHERITE_HELMET = ITEMS_VANILLA.register("netherite_helmet", () -> new ArmorItem(OdysseyArmorMaterial.NETHERITE, EquipmentSlotType.HEAD, new Item.Properties().fireResistant().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_CHESTPLATE = ITEMS_VANILLA.register("netherite_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.NETHERITE, EquipmentSlotType.CHEST, new Item.Properties().fireResistant().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_LEGGINGS = ITEMS_VANILLA.register("netherite_leggings", () -> new ArmorItem(OdysseyArmorMaterial.NETHERITE, EquipmentSlotType.LEGS, new Item.Properties().fireResistant().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_BOOTS = ITEMS_VANILLA.register("netherite_boots", () -> new ArmorItem(OdysseyArmorMaterial.NETHERITE, EquipmentSlotType.FEET, new Item.Properties().fireResistant().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () -> new HorseArmorItem(44, "netherite", (new Item.Properties()).stacksTo(1).fireResistant().tab(Odyssey.COMBAT)));

    public static final RegistryObject<Item> ZEPHYR_HELMET = ITEMS.register("zephyr_helmet", () -> new ZephyrArmorItem(OdysseyArmorMaterial.ZEPHYR, EquipmentSlotType.HEAD, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> ZEPHYR_CHESTPLATE = ITEMS.register("zephyr_chestplate", () -> new ZephyrArmorItem(OdysseyArmorMaterial.ZEPHYR, EquipmentSlotType.CHEST, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> ZEPHYR_LEGGINGS = ITEMS.register("zephyr_leggings", () -> new ZephyrArmorItem(OdysseyArmorMaterial.ZEPHYR, EquipmentSlotType.LEGS, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> ZEPHYR_BOOTS = ITEMS.register("zephyr_boots", () -> new ZephyrArmorItem(OdysseyArmorMaterial.ZEPHYR, EquipmentSlotType.FEET, new Item.Properties().tab(Odyssey.COMBAT)));

    //Tools

    //Swords
    public static final RegistryObject<Item> WOODEN_SWORD = ITEMS_VANILLA.register("wooden_sword", () -> new SwordItem(OdysseyItemTier.WOOD, 4, -2.4f, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> STONE_SWORD = ITEMS_VANILLA.register("stone_sword", () -> new SwordItem(OdysseyItemTier.STONE, 5, -2.4f, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_SWORD = ITEMS_VANILLA.register("iron_sword", () -> new SwordItem(OdysseyItemTier.IRON, 6, -2.4f, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_SWORD = ITEMS_VANILLA.register("golden_sword", () -> new SwordItem(OdysseyItemTier.GOLD, 5, -1.6f, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_SWORD = ITEMS.register("sterling_silver_sword", () -> new SwordItem(OdysseyItemTier.STERLING_SILVER, 7, -2.4f, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> DIAMOND_SWORD = ITEMS_VANILLA.register("diamond_sword", () -> new SwordItem(OdysseyItemTier.DIAMOND, 8, -2.4f, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_SWORD = ITEMS_VANILLA.register("netherite_sword", () -> new SwordItem(OdysseyItemTier.NETHERITE, 9, -2.4f, new Item.Properties().fireResistant().tab(ItemGroup.TAB_COMBAT)));

    //Hammers
    public static final RegistryObject<Item> WOODEN_BAT = ITEMS.register("wooden_bat", () -> new HammerItem(OdysseyItemTier.WOOD, 6, -3.2f, 1, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> GRANITE_BAT = ITEMS.register("granite_bat", () -> new HammerItem(OdysseyItemTier.GRANITE, 7, -3.2f, 1, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> COPPER_HAMMER = ITEMS.register("copper_hammer", () -> new HammerItem(OdysseyItemTier.COPPER, 8, -3.2f, 2, new Item.Properties().tab(Odyssey.COMBAT)));
    public static final RegistryObject<Item> OBSIDIAN_HAMMER = ITEMS.register("obsidian_hammer", () -> new HammerItem(OdysseyItemTier.OBSIDIAN, 10, -3.2f, 3, new Item.Properties().tab(Odyssey.COMBAT)));

    //Axes
    public static final RegistryObject<Item> WOODEN_AXE = ITEMS_VANILLA.register("wooden_axe", () -> new AxeItem(OdysseyItemTier.WOOD, 6.0f, -3.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_AXE = ITEMS_VANILLA.register("stone_axe", () -> new AxeItem(OdysseyItemTier.STONE, 7.0f, -3.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_AXE = ITEMS_VANILLA.register("iron_axe", () -> new AxeItem(OdysseyItemTier.IRON, 8.0f, -3.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_AXE = ITEMS_VANILLA.register("golden_axe", () -> new AxeItem(OdysseyItemTier.GOLD, 7.0f, -2.5f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_AXE = ITEMS.register("sterling_silver_axe", () -> new AxeItem(OdysseyItemTier.STERLING_SILVER, 9.0f, -3.0f, new Item.Properties().tab(Odyssey.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_AXE = ITEMS_VANILLA.register("diamond_axe", () -> new AxeItem(OdysseyItemTier.DIAMOND, 10.5f, -3.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> NETHERITE_AXE = ITEMS_VANILLA.register("netherite_axe", () -> new AxeItem(OdysseyItemTier.NETHERITE, 12.0f, -3.0f, new Item.Properties().fireResistant().tab(ItemGroup.TAB_TOOLS)));

    //Pickaxes
    public static final RegistryObject<Item> WOODEN_PICKAXE = ITEMS_VANILLA.register("wooden_pickaxe", () -> new PickaxeItem(OdysseyItemTier.WOOD, 2, -2.8f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_PICKAXE = ITEMS_VANILLA.register("stone_pickaxe", () -> new PickaxeItem(OdysseyItemTier.STONE, 3, -2.8f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_PICKAXE = ITEMS_VANILLA.register("iron_pickaxe", () -> new PickaxeItem(OdysseyItemTier.IRON, 4, -2.8f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_PICKAXE = ITEMS_VANILLA.register("golden_pickaxe", () -> new PickaxeItem(OdysseyItemTier.GOLD, 3, -2.2f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_PICKAXE = ITEMS.register("sterling_silver_pickaxe", () -> new PickaxeItem(OdysseyItemTier.STERLING_SILVER, 5, -2.8f, new Item.Properties().tab(Odyssey.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_PICKAXE = ITEMS_VANILLA.register("diamond_pickaxe", () -> new PickaxeItem(OdysseyItemTier.DIAMOND, 6, -2.8f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> NETHERITE_PICKAXE = ITEMS_VANILLA.register("netherite_pickaxe", () -> new PickaxeItem(OdysseyItemTier.NETHERITE, 7, -2.8f, new Item.Properties().fireResistant().tab(ItemGroup.TAB_TOOLS)));

    //Shovels
    public static final RegistryObject<Item> WOODEN_SHOVEL = ITEMS_VANILLA.register("wooden_shovel", () -> new ShovelItem(OdysseyItemTier.WOOD, 1.5f, -2.6f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_SHOVEL = ITEMS_VANILLA.register("stone_shovel", () -> new ShovelItem(OdysseyItemTier.STONE, 2.5f, -2.6f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_SHOVEL = ITEMS_VANILLA.register("iron_shovel", () -> new ShovelItem(OdysseyItemTier.IRON, 3.5f, -2.6f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_SHOVEL = ITEMS_VANILLA.register("golden_shovel", () -> new ShovelItem(OdysseyItemTier.GOLD, 2.5f, -1.9f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_SHOVEL = ITEMS.register("sterling_silver_shovel", () -> new ShovelItem(OdysseyItemTier.STERLING_SILVER, 4.5f, -2.6f, new Item.Properties().tab(Odyssey.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_SHOVEL = ITEMS_VANILLA.register("diamond_shovel", () -> new ShovelItem(OdysseyItemTier.DIAMOND, 5.5f, -2.6f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> NETHERITE_SHOVEL = ITEMS_VANILLA.register("netherite_shovel", () -> new ShovelItem(OdysseyItemTier.NETHERITE, 6.5f, -2.6f, new Item.Properties().fireResistant().tab(ItemGroup.TAB_TOOLS)));

    //Hoes
    public static final RegistryObject<Item> WOODEN_HOE = ITEMS_VANILLA.register("wooden_hoe", () -> new HoeItem(OdysseyItemTier.WOOD, 1, -2.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_HOE = ITEMS_VANILLA.register("stone_hoe", () -> new HoeItem(OdysseyItemTier.STONE, 2, -2.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_HOE = ITEMS_VANILLA.register("iron_hoe", () -> new HoeItem(OdysseyItemTier.IRON, 3, -2.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_HOE = ITEMS_VANILLA.register("golden_hoe", () -> new HoeItem(OdysseyItemTier.GOLD, 2, -1.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_HOE = ITEMS.register("sterling_silver_hoe", () -> new HoeItem(OdysseyItemTier.STERLING_SILVER, 4, -2.0f, new Item.Properties().tab(Odyssey.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_HOE = ITEMS_VANILLA.register("diamond_hoe", () -> new HoeItem(OdysseyItemTier.DIAMOND, 5, -2.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> NETHERITE_HOE = ITEMS_VANILLA.register("netherite_hoe", () -> new HoeItem(OdysseyItemTier.NETHERITE, 6, -2.0f, new Item.Properties().fireResistant().tab(ItemGroup.TAB_TOOLS)));

    //Bows/Quivers/Tridents
    public static final RegistryObject<Item> BOW = ITEMS_VANILLA.register("bow", () -> new OdysseyBowItem((new Item.Properties()).durability(384).tab(ItemGroup.TAB_COMBAT), 0.5D));
    public static final RegistryObject<Item> NETHERITE_BOW = ITEMS.register("netherite_bow", () -> new OdysseyBowItem((new Item.Properties()).durability(1016).tab(Odyssey.COMBAT).fireResistant(), 1.0D));
    public static final RegistryObject<Item> CROSSBOW = ITEMS_VANILLA.register("crossbow", () -> new OdysseyCrossbowItem((new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_COMBAT).durability(326), 0.5D));
    public static final RegistryObject<Item> NETHERITE_CROSSBOW = ITEMS.register("netherite_crossbow", () -> new OdysseyCrossbowItem((new Item.Properties()).stacksTo(1).tab(Odyssey.COMBAT).durability(862).fireResistant(), 1.0D));
    public static final RegistryObject<Item> LEATHER_QUIVER = ITEMS.register("leather_quiver", () -> new QuiverItem((new Item.Properties()).stacksTo(1).tab(Odyssey.COMBAT), 3));
    public static final RegistryObject<Item> SERPENT_QUIVER = ITEMS.register("serpent_quiver", () -> new QuiverItem((new Item.Properties()).stacksTo(1).fireResistant().tab(Odyssey.COMBAT), 5));
    public static final RegistryObject<Item> TRIDENT = ITEMS_VANILLA.register("trident", () -> new OdysseyTridentItem((new Item.Properties()).durability(250).tab(ItemGroup.TAB_COMBAT), 9.0D));
    public static final RegistryObject<Item> SERPENT_TRIDENT = ITEMS.register("serpent_trident", () -> new OdysseyTridentItem((new Item.Properties()).durability(750).fireResistant().tab(Odyssey.COMBAT), 11.0D));

    //Shields
    public static final RegistryObject<Item> SHIELD = ITEMS_VANILLA.register("shield", () -> new OdysseyShieldItem((new Item.Properties()).durability(336).tab(ItemGroup.TAB_COMBAT), 5.0f, () -> {NonNullList<Item> nonNullList = NonNullList.create(); nonNullList.add(Items.OAK_PLANKS); nonNullList.add(Items.BIRCH_PLANKS); nonNullList.add(Items.SPRUCE_PLANKS); nonNullList.add(Items.DARK_OAK_PLANKS); nonNullList.add(Items.JUNGLE_PLANKS); nonNullList.add(Items.ACACIA_PLANKS); return nonNullList;}));
    public static final RegistryObject<Item> SERPENT_SHIELD = ITEMS.register("serpent_shield", () -> new OdysseyShieldItem((new Item.Properties()).durability(1000).fireResistant().tab(Odyssey.COMBAT), 10.0f, () -> {NonNullList<Item> nonNullList = NonNullList.create(); nonNullList.add(SERPENT_SCALE.get()); return nonNullList;}));


    //Spawn Eggs
    public static final RegistryObject<Item> ARCTIHORN_SPAWN_EGG = ITEMS.register("arctihorn_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.ARCTIHORN, 0x6e7d90, 0x8ab5eb, (new Item.Properties()).tab(Odyssey.SPAWN_EGGS)));
    public static final RegistryObject<Item> WEREWOLF_SPAWN_EGG = ITEMS.register("werewolf_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.WEREWOLF, 0x000000, 0x000000, (new Item.Properties()).tab(Odyssey.SPAWN_EGGS)));
}