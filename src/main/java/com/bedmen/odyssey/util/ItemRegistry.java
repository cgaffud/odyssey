package com.bedmen.odyssey.util;

import com.bedmen.odyssey.armor.ModArmorMaterial;
import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.Odyssey;
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
    public static final RegistryObject<Item> BEACON = ITEMS_VANILLA.register("beacon", () -> new BlockItem(BlockRegistry.BEACON.get(), (new Item.Properties()).group(ItemGroup.MISC).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> SMITHING_TABLE = ITEMS_VANILLA.register("smithing_table", () -> new BlockItem(BlockRegistry.SMITHING_TABLE.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> ALLOY_FURNACE = ITEMS.register("alloy_furnace", () -> new BlockItem(BlockRegistry.ALLOY_FURNACE.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> CAULDRON = ITEMS_VANILLA.register("cauldron", () -> new BlockItem(BlockRegistry.CAULDRON.get(), (new Item.Properties()).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> INFUSER = ITEMS.register("infuser", () -> new BlockItem(BlockRegistry.INFUSER.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> ENCHANTING_TABLE = ITEMS_VANILLA.register("enchanting_table", () -> new BlockItem(BlockRegistry.ENCHANTING_TABLE.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> BOOKSHELF = ITEMS_VANILLA.register("bookshelf", () -> new BlockItem(BlockRegistry.BOOKSHELF.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> FLETCHING_TABLE = ITEMS_VANILLA.register("fletching_table", () -> new BlockItem(BlockRegistry.FLETCHING_TABLE.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> RECYCLE_FURNACE = ITEMS.register("recycle_furnace", () -> new BlockItem(BlockRegistry.RECYCLE_FURNACE.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> COPPER_ORE = ITEMS.register("copper_ore", () -> new BlockItem(BlockRegistry.COPPER_ORE.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_ORE = ITEMS.register("silver_ore", () -> new BlockItem(BlockRegistry.SILVER_ORE.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_BLOCK = ITEMS.register("silver_block", () -> new BlockItem(BlockRegistry.SILVER_BLOCK.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STERLING_SILVER_BLOCK = ITEMS.register("sterling_silver_block", () -> new BlockItem(BlockRegistry.STERLING_SILVER_BLOCK.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> LEATHER_PILE = ITEMS.register("leather_pile", () -> new BlockItem(BlockRegistry.LEATHER_PILE.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

    public static final RegistryObject<Item> FOG1 = ITEMS.register("fog1", () -> new BlockItem(BlockRegistry.FOG1.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> FOG2 = ITEMS.register("fog2", () -> new BlockItem(BlockRegistry.FOG2.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> FOG3 = ITEMS.register("fog3", () -> new BlockItem(BlockRegistry.FOG3.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> FOG4 = ITEMS.register("fog4", () -> new BlockItem(BlockRegistry.FOG4.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> FOG5 = ITEMS.register("fog5", () -> new BlockItem(BlockRegistry.FOG5.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> FOG6 = ITEMS.register("fog6", () -> new BlockItem(BlockRegistry.FOG6.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> FOG7 = ITEMS.register("fog7", () -> new BlockItem(BlockRegistry.FOG7.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> FOG8 = ITEMS.register("fog8", () -> new BlockItem(BlockRegistry.FOG8.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS)));

    //Items
    public static final RegistryObject<Item> POTION = ITEMS_VANILLA.register("potion", () -> new NewPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> SPLASH_POTION = ITEMS_VANILLA.register("splash_potion", () -> new NewSplashPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> LINGERING_POTION = ITEMS_VANILLA.register("lingering_potion", () -> new NewLingeringPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> GLASS_SHARD = ITEMS.register("glass_shard", () -> new Item((new Item.Properties()).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> BIG_GLASS_BOTTLE = ITEMS.register("big_glass_bottle", () -> new BigGlassBottleItem((new Item.Properties()).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> BIG_POTION = ITEMS.register("big_potion", () -> new BigPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> DIAMOND_SHARD = ITEMS.register("diamond_shard", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> RAW_IRON = ITEMS.register("raw_iron", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> RAW_COPPER = ITEMS.register("raw_copper", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> RAW_GOLD = ITEMS.register("raw_gold", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> STERLING_SILVER_NUGGET = ITEMS.register("sterling_silver_nugget", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> STERLING_SILVER_INGOT = ITEMS.register("sterling_silver_ingot", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SILVER_STRING = ITEMS.register("silver_string", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> FLAME_STRING = ITEMS.register("flame_string", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> CLOVER = ITEMS.register("clover", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> MULTICITE = ITEMS.register("multicite", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SERPENT_SCALE = ITEMS.register("serpent_scale", () -> new Item((new Item.Properties()).isImmuneToFire().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", () -> new LensItem((new Item.Properties()).group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire", () -> new LensItem((new Item.Properties()).group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PEARL = ITEMS.register("pearl", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> NETHERITE_NUGGET = ITEMS.register("netherite_nugget", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> HALLOWED_GOLD_INGOT = ITEMS.register("hallowed_gold_ingot", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> FEATHER_BUNDLE = ITEMS.register("feather_bundle", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> FROZEN_SHARD = ITEMS.register("frozen_shard", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> FROZEN_HEART = ITEMS.register("frozen_heart", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)));

    //Armor
    public static final RegistryObject<Item> TURTLE_HELMET = ITEMS_VANILLA.register("turtle_helmet", () -> new ArmorItem(ModArmorMaterial.TURTLE, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> FROZEN_CHESTPLATE = ITEMS.register("frozen_chestplate", () -> new ArmorItem(ModArmorMaterial.FROZEN, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> STURDY_LEGGINGS = ITEMS.register("sturdy_leggings", () -> new ArmorItem(ModArmorMaterial.STURDY, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> ZEPHYR_BOOTS = ITEMS.register("zephyr_boots", () -> new ArmorItem(ModArmorMaterial.ZEPHYR, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));

    public static final RegistryObject<Item> LEATHER_HELMET = ITEMS_VANILLA.register("leather_helmet", () -> new DyeableArmorItem(ModArmorMaterial.LEATHER, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> LEATHER_CHESTPLATE = ITEMS_VANILLA.register("leather_chestplate", () -> new DyeableArmorItem(ModArmorMaterial.LEATHER, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> LEATHER_LEGGINGS = ITEMS_VANILLA.register("leather_leggings", () -> new DyeableArmorItem(ModArmorMaterial.LEATHER, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> LEATHER_BOOTS = ITEMS_VANILLA.register("leather_boots", () -> new DyeableArmorItem(ModArmorMaterial.LEATHER, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> LEATHER_HORSE_ARMOR = ITEMS_VANILLA.register("leather_horse_armor", () -> new DyeableHorseArmorItem(10, "leather", (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));

    public static final RegistryObject<Item> CHAINMAIL_HELMET = ITEMS_VANILLA.register("chainmail_helmet", () -> new ArmorItem(ModArmorMaterial.CHAIN, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_CHESTPLATE = ITEMS_VANILLA.register("chainmail_chestplate", () -> new ArmorItem(ModArmorMaterial.CHAIN, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_LEGGINGS = ITEMS_VANILLA.register("chainmail_leggings", () -> new ArmorItem(ModArmorMaterial.CHAIN, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_BOOTS = ITEMS_VANILLA.register("chainmail_boots", () -> new ArmorItem(ModArmorMaterial.CHAIN, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));

    public static final RegistryObject<Item> IRON_HELMET = ITEMS_VANILLA.register("iron_helmet", () -> new ArmorItem(ModArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> IRON_CHESTPLATE = ITEMS_VANILLA.register("iron_chestplate", () -> new ArmorItem(ModArmorMaterial.IRON, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> IRON_LEGGINGS = ITEMS_VANILLA.register("iron_leggings", () -> new ArmorItem(ModArmorMaterial.IRON, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> IRON_BOOTS = ITEMS_VANILLA.register("iron_boots", () -> new ArmorItem(ModArmorMaterial.IRON, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> IRON_HORSE_ARMOR = ITEMS_VANILLA.register("iron_horse_armor", () -> new HorseArmorItem(24, "iron", (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));

    public static final RegistryObject<Item> GOLDEN_HELMET = ITEMS_VANILLA.register("golden_helmet", () -> new ArmorItem(ModArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> GOLDEN_CHESTPLATE = ITEMS_VANILLA.register("golden_chestplate", () -> new ArmorItem(ModArmorMaterial.GOLD, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> GOLDEN_LEGGINGS = ITEMS_VANILLA.register("golden_leggings", () -> new ArmorItem(ModArmorMaterial.GOLD, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> GOLDEN_BOOTS = ITEMS_VANILLA.register("golden_boots", () -> new ArmorItem(ModArmorMaterial.GOLD, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> GOLDEN_HORSE_ARMOR = ITEMS_VANILLA.register("golden_horse_armor", () -> new HorseArmorItem(28, "gold", (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));
    
    public static final RegistryObject<Item> SILVER_HELMET = ITEMS.register("sterling_silver_helmet", () -> new ArmorItem(ModArmorMaterial.STERLING_SILVER, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> SILVER_CHESTPLATE = ITEMS.register("sterling_silver_chestplate", () -> new ArmorItem(ModArmorMaterial.STERLING_SILVER, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> SILVER_LEGGINGS = ITEMS.register("sterling_silver_leggings", () -> new ArmorItem(ModArmorMaterial.STERLING_SILVER, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> SILVER_BOOTS = ITEMS.register("sterling_silver_boots", () -> new ArmorItem(ModArmorMaterial.STERLING_SILVER, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_HORSE_ARMOR = ITEMS.register("sterling_silver_horse_armor", () -> new HorseArmorItem(30, "sterling_silver", (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));

    public static final RegistryObject<Item> DIAMOND_HELMET = ITEMS_VANILLA.register("diamond_helmet", () -> new ArmorItem(ModArmorMaterial.DIAMOND, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> DIAMOND_CHESTPLATE = ITEMS_VANILLA.register("diamond_chestplate", () -> new ArmorItem(ModArmorMaterial.DIAMOND, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> DIAMOND_LEGGINGS = ITEMS_VANILLA.register("diamond_leggings", () -> new ArmorItem(ModArmorMaterial.DIAMOND, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> DIAMOND_BOOTS = ITEMS_VANILLA.register("diamond_boots", () -> new ArmorItem(ModArmorMaterial.DIAMOND, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> DIAMOND_HORSE_ARMOR = ITEMS_VANILLA.register("diamond_horse_armor", () -> new HorseArmorItem(40, "diamond", (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));

    public static final RegistryObject<Item> NETHERITE_HELMET = ITEMS_VANILLA.register("netherite_helmet", () -> new ArmorItem(ModArmorMaterial.NETHERITE, EquipmentSlotType.HEAD, new Item.Properties().isImmuneToFire().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> NETHERITE_CHESTPLATE = ITEMS_VANILLA.register("netherite_chestplate", () -> new ArmorItem(ModArmorMaterial.NETHERITE, EquipmentSlotType.CHEST, new Item.Properties().isImmuneToFire().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> NETHERITE_LEGGINGS = ITEMS_VANILLA.register("netherite_leggings", () -> new ArmorItem(ModArmorMaterial.NETHERITE, EquipmentSlotType.LEGS, new Item.Properties().isImmuneToFire().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> NETHERITE_BOOTS = ITEMS_VANILLA.register("netherite_boots", () -> new ArmorItem(ModArmorMaterial.NETHERITE, EquipmentSlotType.FEET, new Item.Properties().isImmuneToFire().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () -> new HorseArmorItem(44, "netherite", (new Item.Properties()).maxStackSize(1).isImmuneToFire().group(ItemGroup.MISC)));

    //Tools

    //Swords
    public static final RegistryObject<Item> WOODEN_SWORD = ITEMS_VANILLA.register("wooden_sword", () -> new SwordItem(ModItemTier.WOOD, 4, -2.4f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> STONE_SWORD = ITEMS_VANILLA.register("stone_sword", () -> new SwordItem(ModItemTier.STONE, 5, -2.4f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> IRON_SWORD = ITEMS_VANILLA.register("iron_sword", () -> new SwordItem(ModItemTier.IRON, 6, -2.4f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> GOLDEN_SWORD = ITEMS_VANILLA.register("golden_sword", () -> new SwordItem(ModItemTier.GOLD, 5, -1.6f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_SWORD = ITEMS.register("sterling_silver_sword", () -> new SwordItem(ModItemTier.STERLING_SILVER, 7, -2.4f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> DIAMOND_SWORD = ITEMS_VANILLA.register("diamond_sword", () -> new SwordItem(ModItemTier.DIAMOND, 8, -2.4f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<Item> NETHERITE_SWORD = ITEMS_VANILLA.register("netherite_sword", () -> new SwordItem(ModItemTier.NETHERITE, 9, -2.4f, new Item.Properties().isImmuneToFire().group(ItemGroup.COMBAT)));

    //Axes
    public static final RegistryObject<Item> WOODEN_AXE = ITEMS_VANILLA.register("wooden_axe", () -> new AxeItem(ModItemTier.WOOD, 6.0f, -3.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> STONE_AXE = ITEMS_VANILLA.register("stone_axe", () -> new AxeItem(ModItemTier.STONE, 7.0f, -3.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> IRON_AXE = ITEMS_VANILLA.register("iron_axe", () -> new AxeItem(ModItemTier.IRON, 8.0f, -3.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> GOLDEN_AXE = ITEMS_VANILLA.register("golden_axe", () -> new AxeItem(ModItemTier.GOLD, 7.0f, -2.5f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_AXE = ITEMS.register("sterling_silver_axe", () -> new AxeItem(ModItemTier.STERLING_SILVER, 9.0f, -3.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_AXE = ITEMS_VANILLA.register("diamond_axe", () -> new AxeItem(ModItemTier.DIAMOND, 10.5f, -3.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> NETHERITE_AXE = ITEMS_VANILLA.register("netherite_axe", () -> new AxeItem(ModItemTier.NETHERITE, 12.0f, -3.0f, new Item.Properties().isImmuneToFire().group(ItemGroup.TOOLS)));

    //Pickaxes
    public static final RegistryObject<Item> WOODEN_PICKAXE = ITEMS_VANILLA.register("wooden_pickaxe", () -> new PickaxeItem(ModItemTier.WOOD, 2, -2.8f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> STONE_PICKAXE = ITEMS_VANILLA.register("stone_pickaxe", () -> new PickaxeItem(ModItemTier.STONE, 3, -2.8f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> IRON_PICKAXE = ITEMS_VANILLA.register("iron_pickaxe", () -> new PickaxeItem(ModItemTier.IRON, 4, -2.8f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> GOLDEN_PICKAXE = ITEMS_VANILLA.register("golden_pickaxe", () -> new PickaxeItem(ModItemTier.GOLD, 3, -2.2f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_PICKAXE = ITEMS.register("sterling_silver_pickaxe", () -> new PickaxeItem(ModItemTier.STERLING_SILVER, 5, -2.8f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_PICKAXE = ITEMS_VANILLA.register("diamond_pickaxe", () -> new PickaxeItem(ModItemTier.DIAMOND, 6, -2.8f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> NETHERITE_PICKAXE = ITEMS_VANILLA.register("netherite_pickaxe", () -> new PickaxeItem(ModItemTier.NETHERITE, 7, -2.8f, new Item.Properties().isImmuneToFire().group(ItemGroup.TOOLS)));

    //Shovels
    public static final RegistryObject<Item> WOODEN_SHOVEL = ITEMS_VANILLA.register("wooden_shovel", () -> new ShovelItem(ModItemTier.WOOD, 1.5f, -2.6f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> STONE_SHOVEL = ITEMS_VANILLA.register("stone_shovel", () -> new ShovelItem(ModItemTier.STONE, 2.5f, -2.6f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> IRON_SHOVEL = ITEMS_VANILLA.register("iron_shovel", () -> new ShovelItem(ModItemTier.IRON, 3.5f, -2.6f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> GOLDEN_SHOVEL = ITEMS_VANILLA.register("golden_shovel", () -> new ShovelItem(ModItemTier.GOLD, 2.5f, -1.9f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_SHOVEL = ITEMS.register("sterling_silver_shovel", () -> new ShovelItem(ModItemTier.STERLING_SILVER, 4.5f, -2.6f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_SHOVEL = ITEMS_VANILLA.register("diamond_shovel", () -> new ShovelItem(ModItemTier.DIAMOND, 5.5f, -2.6f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> NETHERITE_SHOVEL = ITEMS_VANILLA.register("netherite_shovel", () -> new ShovelItem(ModItemTier.NETHERITE, 6.5f, -2.6f, new Item.Properties().isImmuneToFire().group(ItemGroup.TOOLS)));

    //Hoes
    public static final RegistryObject<Item> WOODEN_HOE = ITEMS_VANILLA.register("wooden_hoe", () -> new HoeItem(ModItemTier.WOOD, 1, -2.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> STONE_HOE = ITEMS_VANILLA.register("stone_hoe", () -> new HoeItem(ModItemTier.STONE, 2, -2.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> IRON_HOE = ITEMS_VANILLA.register("iron_hoe", () -> new HoeItem(ModItemTier.IRON, 3, -2.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> GOLDEN_HOE = ITEMS_VANILLA.register("golden_hoe", () -> new HoeItem(ModItemTier.GOLD, 2, -1.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_HOE = ITEMS.register("sterling_silver_hoe", () -> new HoeItem(ModItemTier.STERLING_SILVER, 4, -2.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_HOE = ITEMS_VANILLA.register("diamond_hoe", () -> new HoeItem(ModItemTier.DIAMOND, 5, -2.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> NETHERITE_HOE = ITEMS_VANILLA.register("netherite_hoe", () -> new HoeItem(ModItemTier.NETHERITE, 6, -2.0f, new Item.Properties().isImmuneToFire().group(ItemGroup.TOOLS)));

    //Bows/Quivers/Tridents
    public static final RegistryObject<Item> BOW = ITEMS_VANILLA.register("bow", () -> new NewBowItem((new Item.Properties()).maxDamage(384).group(ItemGroup.COMBAT), 0.5D));
    public static final RegistryObject<Item> NETHERITE_BOW = ITEMS.register("netherite_bow", () -> new NewBowItem((new Item.Properties()).maxDamage(1016).group(ItemGroup.COMBAT).isImmuneToFire(), 1.0D));
    public static final RegistryObject<Item> CROSSBOW = ITEMS_VANILLA.register("crossbow", () -> new NewCrossbowItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.COMBAT).maxDamage(326), 0.5D));
    public static final RegistryObject<Item> NETHERITE_CROSSBOW = ITEMS.register("netherite_crossbow", () -> new NewCrossbowItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.COMBAT).maxDamage(862).isImmuneToFire(), 1.0D));
    public static final RegistryObject<Item> LEATHER_QUIVER = ITEMS.register("leather_quiver", () -> new QuiverItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.COMBAT), 3));
    public static final RegistryObject<Item> SERPENT_QUIVER = ITEMS.register("serpent_quiver", () -> new QuiverItem((new Item.Properties()).maxStackSize(1).isImmuneToFire().group(ItemGroup.COMBAT), 5));
    public static final RegistryObject<Item> TRIDENT = ITEMS_VANILLA.register("trident", () -> new NewTridentItem((new Item.Properties()).maxDamage(250).group(ItemGroup.COMBAT), 9.0D));
    public static final RegistryObject<Item> SERPENT_TRIDENT = ITEMS.register("serpent_trident", () -> new NewTridentItem((new Item.Properties()).maxDamage(750).isImmuneToFire().group(ItemGroup.COMBAT), 11.0D));

    //Shields
    public static final RegistryObject<Item> SHIELD = ITEMS_VANILLA.register("shield", () -> new NewShieldItem((new Item.Properties()).maxDamage(336).group(ItemGroup.COMBAT), 5.0f, () -> {NonNullList<Item> nonNullList = NonNullList.create(); nonNullList.add(Items.OAK_PLANKS); nonNullList.add(Items.BIRCH_PLANKS); nonNullList.add(Items.SPRUCE_PLANKS); nonNullList.add(Items.DARK_OAK_PLANKS); nonNullList.add(Items.JUNGLE_PLANKS); nonNullList.add(Items.ACACIA_PLANKS); return nonNullList;}));
    public static final RegistryObject<Item> SERPENT_SHIELD = ITEMS.register("serpent_shield", () -> new NewShieldItem((new Item.Properties()).maxDamage(1000).isImmuneToFire().group(ItemGroup.COMBAT), 10.0f, () -> {NonNullList<Item> nonNullList = NonNullList.create(); nonNullList.add(SERPENT_SCALE.get()); return nonNullList;}));


    //Spawn Eggs
    //public static final RegistryObject<Item> NETHER_CREEPER_SPAWN_EGG = ITEMS.register("nether_creeper_spawn_egg", () -> new ModSpawnEggItem(EntityTypeRegistry.NETHER_CREEPER, 0x511515, 0xff7f27, (new Item.Properties()).group(ItemGroup.MISC)));
}