package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.armor.OdysseyArmorMaterial;
import com.bedmen.odyssey.client.renderer.tileentity.OdysseyItemStackTileEntityRenderer;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.item.OdysseyBoatEntity;
import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.items.equipment.*;
import com.bedmen.odyssey.loot.ChestMaterial;
import com.bedmen.odyssey.tools.*;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.fluid.Fluids;
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
    public static final RegistryObject<Item> ALLOY_FURNACE = ITEMS.register("alloy_furnace", () -> new BlockItem(BlockRegistry.ALLOY_FURNACE.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> ENCHANTING_TABLE = ITEMS_VANILLA.register("enchanting_table", () -> new BlockItem(BlockRegistry.ENCHANTING_TABLE.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BOOKSHELF = ITEMS_VANILLA.register("bookshelf", () -> new BlockItem(BlockRegistry.BOOKSHELF.get(), (new Item.Properties()).tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> RECYCLE_FURNACE = ITEMS.register("recycle_furnace", () -> new BlockItem(BlockRegistry.RECYCLE_FURNACE.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> RESEARCH_TABLE = ITEMS.register("research_table", () -> new BlockItem(BlockRegistry.RESEARCH_TABLE.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> GRINDSTONE = ITEMS_VANILLA.register("grindstone", () -> new BlockItem(BlockRegistry.GRINDSTONE.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> COPPER_ORE = ITEMS.register("copper_ore", () -> new BlockItem(BlockRegistry.COPPER_ORE.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_ORE = ITEMS.register("silver_ore", () -> new BlockItem(BlockRegistry.SILVER_ORE.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> RUBY_ORE = ITEMS.register("ruby_ore", () -> new BlockItem(BlockRegistry.RUBY_ORE.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SAPPHIRE_ORE = ITEMS.register("sapphire_ore", () -> new BlockItem(BlockRegistry.SAPPHIRE_ORE.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_BLOCK = ITEMS.register("silver_block", () -> new BlockItem(BlockRegistry.SILVER_BLOCK.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STERLING_SILVER_BLOCK = ITEMS.register("sterling_silver_block", () -> new BlockItem(BlockRegistry.STERLING_SILVER_BLOCK.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STERLING_SILVER_CHEST = ITEMS.register("sterling_silver_chest", () -> new BlockItem(BlockRegistry.STERLING_SILVER_CHEST.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS).setISTER(() -> OdysseyItemStackTileEntityRenderer.odysseyInstance)));
    public static final RegistryObject<Item> FLUORITE_BLOCK = ITEMS.register("fluorite_block", () -> new BlockItem(BlockRegistry.FLUORITE_BLOCK.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> PERIDOT_BLOCK = ITEMS.register("peridot_block", () -> new BlockItem(BlockRegistry.PERIDOT_BLOCK.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> LEATHER_PILE = ITEMS.register("leather_pile", () -> new BlockItem(BlockRegistry.LEATHER_PILE.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> PERMAFROST_CONDUIT = ITEMS.register("permafrost_conduit", () -> new BlockItem(BlockRegistry.PERMAFROST_CONDUIT.get(), (new Item.Properties()).tab(ItemGroup.TAB_MISC).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> ANVIL = ITEMS_VANILLA.register("anvil", () -> new BlockItem(BlockRegistry.ANVIL.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> CHIPPED_ANVIL = ITEMS_VANILLA.register("chipped_anvil", () -> new BlockItem(BlockRegistry.CHIPPED_ANVIL.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> DAMAGED_ANVIL = ITEMS_VANILLA.register("damaged_anvil", () -> new BlockItem(BlockRegistry.DAMAGED_ANVIL.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> FOG1 = ITEMS.register("fog1", () -> new BlockItem(BlockRegistry.FOG1.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG2 = ITEMS.register("fog2", () -> new BlockItem(BlockRegistry.FOG2.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG3 = ITEMS.register("fog3", () -> new BlockItem(BlockRegistry.FOG3.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG4 = ITEMS.register("fog4", () -> new BlockItem(BlockRegistry.FOG4.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG5 = ITEMS.register("fog5", () -> new BlockItem(BlockRegistry.FOG5.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG6 = ITEMS.register("fog6", () -> new BlockItem(BlockRegistry.FOG6.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG7 = ITEMS.register("fog7", () -> new BlockItem(BlockRegistry.FOG7.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> FOG8 = ITEMS.register("fog8", () -> new BlockItem(BlockRegistry.FOG8.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));

    public static final RegistryObject<Item> AUTUMN_LEAVES_RED = ITEMS.register("autumn_leaves_red", () -> new BlockItem(BlockRegistry.AUTUMN_LEAVES_RED.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> AUTUMN_LEAVES_ORANGE = ITEMS.register("autumn_leaves_orange", () -> new BlockItem(BlockRegistry.AUTUMN_LEAVES_ORANGE.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> AUTUMN_LEAVES_YELLOW = ITEMS.register("autumn_leaves_yellow", () -> new BlockItem(BlockRegistry.AUTUMN_LEAVES_YELLOW.get(), (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<Item> PALM_PLANKS = ITEMS.register("palm_planks", () -> new BlockItem(BlockRegistry.PALM_PLANKS.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> PALM_SAPLING = ITEMS.register("palm_sapling", () -> new BlockItem(BlockRegistry.PALM_SAPLING.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> PALM_LOG = ITEMS.register("palm_log", () -> new BlockItem(BlockRegistry.PALM_LOG.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STRIPPED_PALM_LOG = ITEMS.register("stripped_palm_log", () -> new BlockItem(BlockRegistry.STRIPPED_PALM_LOG.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STRIPPED_PALM_WOOD = ITEMS.register("stripped_palm_wood", () -> new BlockItem(BlockRegistry.STRIPPED_PALM_WOOD.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> PALM_WOOD = ITEMS.register("palm_wood", () -> new BlockItem(BlockRegistry.PALM_WOOD.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> PALM_LEAVES = ITEMS.register("palm_leaves", () -> new BlockItem(BlockRegistry.PALM_LEAVES.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> PALM_SLAB = ITEMS.register("palm_slab", () -> new BlockItem(BlockRegistry.PALM_SLAB.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> PALM_PRESSURE_PLATE = ITEMS.register("palm_pressure_plate", () -> new BlockItem(BlockRegistry.PALM_PRESSURE_PLATE.get(), (new Item.Properties()).tab(OdysseyItemGroup.REDSTONE)));
    public static final RegistryObject<Item> PALM_FENCE = ITEMS.register("palm_fence", () -> new BurnableFenceItem(BlockRegistry.PALM_FENCE.get(), (new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS)));
    public static final RegistryObject<Item> PALM_TRAPDOOR = ITEMS.register("palm_trapdoor", () -> new BlockItem(BlockRegistry.PALM_TRAPDOOR.get(), (new Item.Properties()).tab(OdysseyItemGroup.REDSTONE)));
    public static final RegistryObject<Item> PALM_FENCE_GATE = ITEMS.register("palm_fence_gate", () -> new BurnableFenceItem(BlockRegistry.PALM_FENCE_GATE.get(), (new Item.Properties()).tab(OdysseyItemGroup.REDSTONE)));
    public static final RegistryObject<Item> PALM_BUTTON = ITEMS.register("palm_button", () -> new BlockItem(BlockRegistry.PALM_BUTTON.get(), (new Item.Properties()).tab(OdysseyItemGroup.REDSTONE)));
    public static final RegistryObject<Item> PALM_STAIRS = ITEMS.register("palm_stairs", () -> new BlockItem(BlockRegistry.PALM_STAIRS.get(), (new Item.Properties()).tab(OdysseyItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> PALM_DOOR = ITEMS.register("palm_door", () -> new BlockItem(BlockRegistry.PALM_DOOR.get(), (new Item.Properties()).tab(OdysseyItemGroup.REDSTONE)));
    public static final RegistryObject<Item> PALM_SIGN = ITEMS.register("palm_sign", () -> new SignItem((new Item.Properties()).tab(OdysseyItemGroup.DECORATION_BLOCKS), BlockRegistry.PALM_SIGN.get(), BlockRegistry.PALM_WALL_SIGN.get()));
    public static final RegistryObject<Item> PALM_BOAT = ITEMS.register("palm_boat", () -> new OdysseyBoatItem(OdysseyBoatEntity.Type.PALM, (new Item.Properties()).stacksTo(1).tab(OdysseyItemGroup.TRANSPORTATION)));

    public static final RegistryObject<Item> COCONUT_FLOWER = ITEMS.register("coconut_flower", () -> new BlockNamedItem(BlockRegistry.COCONUT.get(), (new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));

    //Items
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> STERLING_SILVER_NUGGET = ITEMS.register("sterling_silver_nugget", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> STERLING_SILVER_INGOT = ITEMS.register("sterling_silver_ingot", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> FLUORITE = ITEMS.register("fluorite", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> PERIDOT = ITEMS.register("peridot", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> CLOVER = ITEMS.register("clover", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> LEVIATHAN_SCALE = ITEMS.register("leviathan_scale", () -> new Item((new Item.Properties()).fireResistant().tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PEARL = ITEMS.register("pearl", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> NETHERITE_NUGGET = ITEMS.register("netherite_nugget", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> HALLOWED_GOLD_INGOT = ITEMS.register("hallowed_gold_ingot", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> FEATHER_BUNDLE = ITEMS.register("feather_bundle", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> PERMAFROST_SHARD = ITEMS.register("permafrost_shard", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> ARCTIC_HEART = ITEMS.register("arctic_heart", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> RAZOR_CLAW = ITEMS.register("razor_claw", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> ARCTIC_HORN = ITEMS.register("arctic_horn", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> RAVEN_FEATHER = ITEMS.register("raven_feather", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> BEWITCHED_QUILL = ITEMS.register("bewitched_quill", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MAGIC).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> MALEVOLENT_QUILL = ITEMS.register("malevolent_quill", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MAGIC).stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> PURGE_TABLET = ITEMS.register("purge_tablet", () -> new PurgeTabletItem((new Item.Properties()).tab(OdysseyItemGroup.MAGIC).stacksTo(1).rarity(Rarity.RARE)));

    //Food
    public static final RegistryObject<Item> LIFE_FRUIT = ITEMS.register("life_fruit", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MAGIC).food(OdysseyFoods.LIFE_FRUIT)));

    //Keys
    public static final RegistryObject<Item> STERLING_SILVER_KEY = ITEMS.register("sterling_silver_key", () -> new KeyItem(ChestMaterial.STERLING_SILVER, (new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));

    //Equipment Enchants
    public static final LevEnchSup FALL_PROTECTION_1 = new LevEnchSup(() -> Enchantments.FALL_PROTECTION, 1);
    public static final LevEnchSup RESPIRATION_1 = new LevEnchSup(EnchantmentRegistry.RESPIRATION, 1);
    public static final LevEnchSup RESPIRATION_2 = new LevEnchSup(EnchantmentRegistry.RESPIRATION, 2);
    public static final LevEnchSup DEPTH_STRIDER_1 = new LevEnchSup(EnchantmentRegistry.DEPTH_STRIDER, 1);
    public static final LevEnchSup DEPTH_STRIDER_2 = new LevEnchSup(EnchantmentRegistry.DEPTH_STRIDER, 2);
    public static final LevEnchSup BLAST_PROTECTION_1 = new LevEnchSup(() -> Enchantments.BLAST_PROTECTION, 1);
    public static final LevEnchSup FIRE_PROTECTION_1 = new LevEnchSup(() -> Enchantments.FIRE_PROTECTION, 1);
    public static final LevEnchSup FALL_PROTECTION_2 = new LevEnchSup(() -> Enchantments.FALL_PROTECTION, 2);
    public static final LevEnchSup PYROPNEUMATIC_1 = new LevEnchSup(EnchantmentRegistry.PYROPNEUMATIC , 1);
    public static final LevEnchSup VULCAN_STRIDER_1 = new LevEnchSup(EnchantmentRegistry.VULCAN_STRIDER, 1);
    public static final LevEnchSup AQUA_AFFINITY = new LevEnchSup(EnchantmentRegistry.AQUA_AFFINITY);
    public static final LevEnchSup BINDING = new LevEnchSup(() -> Enchantments.BINDING_CURSE);

    //Armor
    public static final RegistryObject<Item> LEATHER_HELMET = ITEMS_VANILLA.register("leather_helmet", () -> new DyeableArmorItem(OdysseyArmorMaterial.LEATHER, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_CHESTPLATE = ITEMS_VANILLA.register("leather_chestplate", () -> new DyeableArmorItem(OdysseyArmorMaterial.LEATHER, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_LEGGINGS = ITEMS_VANILLA.register("leather_leggings", () -> new DyeableArmorItem(OdysseyArmorMaterial.LEATHER, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_BOOTS = ITEMS_VANILLA.register("leather_boots", () -> new DyeableArmorItem(OdysseyArmorMaterial.LEATHER, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> LEATHER_HORSE_ARMOR = ITEMS_VANILLA.register("leather_horse_armor", () -> new DyeableHorseArmorItem(10, "leather", (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> HOLLOW_COCONUT = ITEMS.register("hollow_coconut", () -> new HollowCoconutItem(OdysseyArmorMaterial.COCONUT, EquipmentSlotType.HEAD, new Item.Properties().tab(OdysseyItemGroup.COMBAT), BINDING));

    public static final RegistryObject<Item> CHICKEN_HELMET = ITEMS.register("chicken_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterial.CHICKEN, EquipmentSlotType.HEAD, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FALL_PROTECTION_1));
    public static final RegistryObject<Item> CHICKEN_CHESTPLATE = ITEMS.register("chicken_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterial.CHICKEN, EquipmentSlotType.CHEST, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FALL_PROTECTION_1));
    public static final RegistryObject<Item> CHICKEN_LEGGINGS = ITEMS.register("chicken_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterial.CHICKEN, EquipmentSlotType.LEGS, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FALL_PROTECTION_1));
    public static final RegistryObject<Item> CHICKEN_BOOTS = ITEMS.register("chicken_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterial.CHICKEN, EquipmentSlotType.FEET, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FALL_PROTECTION_1));

    public static final RegistryObject<Item> CHAINMAIL_HELMET = ITEMS_VANILLA.register("chainmail_helmet", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_CHESTPLATE = ITEMS_VANILLA.register("chainmail_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_LEGGINGS = ITEMS_VANILLA.register("chainmail_leggings", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_BOOTS = ITEMS_VANILLA.register("chainmail_boots", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));

    public static final RegistryObject<Item> IRON_HELMET = ITEMS_VANILLA.register("iron_helmet", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_CHESTPLATE = ITEMS_VANILLA.register("iron_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_LEGGINGS = ITEMS_VANILLA.register("iron_leggings", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_BOOTS = ITEMS_VANILLA.register("iron_boots", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_HORSE_ARMOR = ITEMS_VANILLA.register("iron_horse_armor", () -> new HorseArmorItem(24, "iron", (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> TURTLE_HELMET = ITEMS_VANILLA.register("turtle_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterial.TURTLE, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT), RESPIRATION_1));
    public static final RegistryObject<Item> TURTLE_CHESTPLATE = ITEMS.register("turtle_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterial.TURTLE, EquipmentSlotType.CHEST, new Item.Properties().tab(OdysseyItemGroup.COMBAT), RESPIRATION_1));
    public static final RegistryObject<Item> TURTLE_LEGGINGS = ITEMS.register("turtle_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterial.TURTLE, EquipmentSlotType.LEGS, new Item.Properties().tab(OdysseyItemGroup.COMBAT), DEPTH_STRIDER_1));
    public static final RegistryObject<Item> TURTLE_BOOTS = ITEMS.register("turtle_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterial.TURTLE, EquipmentSlotType.FEET, new Item.Properties().tab(OdysseyItemGroup.COMBAT), DEPTH_STRIDER_1));

    public static final RegistryObject<Item> GOLDEN_HELMET = ITEMS_VANILLA.register("golden_helmet", () -> new GoldArmorItem(OdysseyArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_CHESTPLATE = ITEMS_VANILLA.register("golden_chestplate", () -> new GoldArmorItem(OdysseyArmorMaterial.GOLD, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_LEGGINGS = ITEMS_VANILLA.register("golden_leggings", () -> new GoldArmorItem(OdysseyArmorMaterial.GOLD, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_BOOTS = ITEMS_VANILLA.register("golden_boots", () -> new GoldArmorItem(OdysseyArmorMaterial.GOLD, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_HORSE_ARMOR = ITEMS_VANILLA.register("golden_horse_armor", () -> new HorseArmorItem(28, "gold", (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));
    
    public static final RegistryObject<Item> STERLING_SILVER_HELMET = ITEMS.register("sterling_silver_helmet", () -> new ArmorItem(OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlotType.HEAD, new Item.Properties().tab(OdysseyItemGroup.COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_CHESTPLATE = ITEMS.register("sterling_silver_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlotType.CHEST, new Item.Properties().tab(OdysseyItemGroup.COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_LEGGINGS = ITEMS.register("sterling_silver_leggings", () -> new ArmorItem(OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlotType.LEGS, new Item.Properties().tab(OdysseyItemGroup.COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_BOOTS = ITEMS.register("sterling_silver_boots", () -> new ArmorItem(OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlotType.FEET, new Item.Properties().tab(OdysseyItemGroup.COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_HORSE_ARMOR = ITEMS.register("sterling_silver_horse_armor", () -> new HorseArmorItem(30, "sterling_silver", (new Item.Properties()).stacksTo(1).tab(OdysseyItemGroup.COMBAT)));

    public static final RegistryObject<Item> REINFORCED_HELMET = ITEMS.register("reinforced_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterial.REINFORCED, EquipmentSlotType.HEAD, new Item.Properties().tab(OdysseyItemGroup.COMBAT), BLAST_PROTECTION_1));
    public static final RegistryObject<Item> REINFORCED_CHESTPLATE = ITEMS.register("reinforced_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterial.REINFORCED, EquipmentSlotType.CHEST, new Item.Properties().tab(OdysseyItemGroup.COMBAT), BLAST_PROTECTION_1));
    public static final RegistryObject<Item> REINFORCED_LEGGINGS = ITEMS.register("reinforced_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterial.REINFORCED, EquipmentSlotType.LEGS, new Item.Properties().tab(OdysseyItemGroup.COMBAT), BLAST_PROTECTION_1));
    public static final RegistryObject<Item> REINFORCED_BOOTS = ITEMS.register("reinforced_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterial.REINFORCED, EquipmentSlotType.FEET, new Item.Properties().tab(OdysseyItemGroup.COMBAT), BLAST_PROTECTION_1));

    public static final RegistryObject<Item> DIAMOND_HELMET = ITEMS_VANILLA.register("diamond_helmet", () -> new ArmorItem(OdysseyArmorMaterial.DIAMOND, EquipmentSlotType.HEAD, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_CHESTPLATE = ITEMS_VANILLA.register("diamond_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.DIAMOND, EquipmentSlotType.CHEST, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_LEGGINGS = ITEMS_VANILLA.register("diamond_leggings", () -> new ArmorItem(OdysseyArmorMaterial.DIAMOND, EquipmentSlotType.LEGS, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_BOOTS = ITEMS_VANILLA.register("diamond_boots", () -> new ArmorItem(OdysseyArmorMaterial.DIAMOND, EquipmentSlotType.FEET, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_HORSE_ARMOR = ITEMS_VANILLA.register("diamond_horse_armor", () -> new HorseArmorItem(40, "diamond", (new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> ARCTIC_HELMET = ITEMS.register("arctic_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterial.ARCTIC, EquipmentSlotType.HEAD, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FIRE_PROTECTION_1));
    public static final RegistryObject<Item> ARCTIC_CHESTPLATE = ITEMS.register("arctic_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterial.ARCTIC, EquipmentSlotType.CHEST, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FIRE_PROTECTION_1));
    public static final RegistryObject<Item> ARCTIC_LEGGINGS = ITEMS.register("arctic_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterial.ARCTIC, EquipmentSlotType.LEGS, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FIRE_PROTECTION_1));
    public static final RegistryObject<Item> ARCTIC_BOOTS = ITEMS.register("arctic_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterial.ARCTIC, EquipmentSlotType.FEET, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FIRE_PROTECTION_1));

    public static final RegistryObject<Item> MARINE_HELMET = ITEMS.register("marine_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterial.MARINE, EquipmentSlotType.HEAD, new Item.Properties().tab(OdysseyItemGroup.COMBAT), RESPIRATION_2));
    public static final RegistryObject<Item> MARINE_CHESTPLATE = ITEMS.register("marine_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterial.MARINE, EquipmentSlotType.CHEST, new Item.Properties().tab(OdysseyItemGroup.COMBAT), RESPIRATION_2));
    public static final RegistryObject<Item> MARINE_LEGGINGS = ITEMS.register("marine_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterial.MARINE, EquipmentSlotType.LEGS, new Item.Properties().tab(OdysseyItemGroup.COMBAT), DEPTH_STRIDER_2));
    public static final RegistryObject<Item> MARINE_BOOTS = ITEMS.register("marine_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterial.MARINE, EquipmentSlotType.FEET, new Item.Properties().tab(OdysseyItemGroup.COMBAT), DEPTH_STRIDER_2));

    public static final RegistryObject<Item> NETHERITE_HELMET = ITEMS_VANILLA.register("netherite_helmet", () -> new ArmorItem(OdysseyArmorMaterial.NETHERITE, EquipmentSlotType.HEAD, new Item.Properties().fireResistant().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_CHESTPLATE = ITEMS_VANILLA.register("netherite_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.NETHERITE, EquipmentSlotType.CHEST, new Item.Properties().fireResistant().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_LEGGINGS = ITEMS_VANILLA.register("netherite_leggings", () -> new ArmorItem(OdysseyArmorMaterial.NETHERITE, EquipmentSlotType.LEGS, new Item.Properties().fireResistant().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_BOOTS = ITEMS_VANILLA.register("netherite_boots", () -> new ArmorItem(OdysseyArmorMaterial.NETHERITE, EquipmentSlotType.FEET, new Item.Properties().fireResistant().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () -> new HorseArmorItem(44, "netherite", (new Item.Properties()).stacksTo(1).fireResistant().tab(OdysseyItemGroup.COMBAT)));

    public static final RegistryObject<Item> ZEPHYR_HELMET = ITEMS.register("zephyr_helmet", () -> new ZephyrArmorItem(OdysseyArmorMaterial.ZEPHYR, EquipmentSlotType.HEAD, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FALL_PROTECTION_2));
    public static final RegistryObject<Item> ZEPHYR_CHESTPLATE = ITEMS.register("zephyr_chestplate", () -> new ZephyrArmorItem(OdysseyArmorMaterial.ZEPHYR, EquipmentSlotType.CHEST, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FALL_PROTECTION_2));
    public static final RegistryObject<Item> ZEPHYR_LEGGINGS = ITEMS.register("zephyr_leggings", () -> new ZephyrArmorItem(OdysseyArmorMaterial.ZEPHYR, EquipmentSlotType.LEGS, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FALL_PROTECTION_2));
    public static final RegistryObject<Item> ZEPHYR_BOOTS = ITEMS.register("zephyr_boots", () -> new ZephyrArmorItem(OdysseyArmorMaterial.ZEPHYR, EquipmentSlotType.FEET, new Item.Properties().tab(OdysseyItemGroup.COMBAT), FALL_PROTECTION_2));

    public static final RegistryObject<Item> LEVIATHAN_HELMET = ITEMS.register("leviathan_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterial.LEVIATHAN, EquipmentSlotType.HEAD, new Item.Properties().fireResistant().tab(OdysseyItemGroup.COMBAT), PYROPNEUMATIC_1));
    public static final RegistryObject<Item> LEVIATHAN_CHESTPLATE = ITEMS.register("leviathan_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterial.LEVIATHAN, EquipmentSlotType.CHEST, new Item.Properties().fireResistant().tab(OdysseyItemGroup.COMBAT), PYROPNEUMATIC_1));
    public static final RegistryObject<Item> LEVIATHAN_LEGGINGS = ITEMS.register("leviathan_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterial.LEVIATHAN, EquipmentSlotType.LEGS, new Item.Properties().fireResistant().tab(OdysseyItemGroup.COMBAT), VULCAN_STRIDER_1));
    public static final RegistryObject<Item> LEVIATHAN_BOOTS = ITEMS.register("leviathan_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterial.LEVIATHAN, EquipmentSlotType.FEET, new Item.Properties().fireResistant().tab(OdysseyItemGroup.COMBAT), VULCAN_STRIDER_1));

    //Trinkets
    public static final RegistryObject<Item> ARCTIC_AMULET = ITEMS.register("arctic_amulet", () -> new EquipmentTrinketItem(new Item.Properties().stacksTo(1).tab(OdysseyItemGroup.MAGIC), FIRE_PROTECTION_1));
    public static final RegistryObject<Item> GOLDEN_AMULET = ITEMS.register("golden_amulet", () -> new GoldenAmuletItem(new Item.Properties().stacksTo(1).tab(OdysseyItemGroup.MAGIC)));

    //Tools

    //Swords
    public static final RegistryObject<Item> WOODEN_SWORD = ITEMS_VANILLA.register("wooden_sword", () -> new SwordItem(OdysseyItemTier.WOOD, 4, -2.4f, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> STONE_SWORD = ITEMS_VANILLA.register("stone_sword", () -> new SwordItem(OdysseyItemTier.STONE, 5, -2.4f, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_SWORD = ITEMS_VANILLA.register("iron_sword", () -> new SwordItem(OdysseyItemTier.IRON, 6, -2.4f, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_SWORD = ITEMS_VANILLA.register("golden_sword", () -> new SwordItem(OdysseyItemTier.GOLD, 5, -1.6f, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> STERLING_SILVER_SWORD = ITEMS.register("sterling_silver_sword", () -> new SwordItem(OdysseyItemTier.STERLING_SILVER, 7, -2.4f, new Item.Properties().tab(OdysseyItemGroup.COMBAT)));
    public static final RegistryObject<Item> DIAMOND_SWORD = ITEMS_VANILLA.register("diamond_sword", () -> new SwordItem(OdysseyItemTier.DIAMOND, 8, -2.4f, new Item.Properties().tab(ItemGroup.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_SWORD = ITEMS_VANILLA.register("netherite_sword", () -> new SwordItem(OdysseyItemTier.NETHERITE, 9, -2.4f, new Item.Properties().fireResistant().tab(ItemGroup.TAB_COMBAT)));
    
    //Axes
    public static final RegistryObject<Item> WOODEN_AXE = ITEMS_VANILLA.register("wooden_axe", () -> new AxeItem(OdysseyItemTier.WOOD, 6.0f, -3.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_AXE = ITEMS_VANILLA.register("stone_axe", () -> new AxeItem(OdysseyItemTier.STONE, 7.0f, -3.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_AXE = ITEMS_VANILLA.register("iron_axe", () -> new AxeItem(OdysseyItemTier.IRON, 8.0f, -3.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_AXE = ITEMS_VANILLA.register("golden_axe", () -> new AxeItem(OdysseyItemTier.GOLD, 7.0f, -2.5f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_AXE = ITEMS.register("sterling_silver_axe", () -> new AxeItem(OdysseyItemTier.STERLING_SILVER, 9.0f, -3.0f, new Item.Properties().tab(OdysseyItemGroup.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_AXE = ITEMS_VANILLA.register("diamond_axe", () -> new AxeItem(OdysseyItemTier.DIAMOND, 10.5f, -3.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> MARINE_AXE = ITEMS.register("marine_axe", () -> new EquipmentAxeItem(OdysseyItemTier.MARINE, 8.0f, -2.5f, new Item.Properties().tab(OdysseyItemGroup.TOOLS), AQUA_AFFINITY));
    public static final RegistryObject<Item> NETHERITE_AXE = ITEMS_VANILLA.register("netherite_axe", () -> new AxeItem(OdysseyItemTier.NETHERITE, 12.0f, -3.0f, new Item.Properties().fireResistant().tab(ItemGroup.TAB_TOOLS)));

    //Pickaxes
    public static final RegistryObject<Item> WOODEN_PICKAXE = ITEMS_VANILLA.register("wooden_pickaxe", () -> new PickaxeItem(OdysseyItemTier.WOOD, 2, -2.8f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_PICKAXE = ITEMS_VANILLA.register("stone_pickaxe", () -> new PickaxeItem(OdysseyItemTier.STONE, 3, -2.8f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_PICKAXE = ITEMS_VANILLA.register("iron_pickaxe", () -> new PickaxeItem(OdysseyItemTier.IRON, 4, -2.8f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_PICKAXE = ITEMS_VANILLA.register("golden_pickaxe", () -> new PickaxeItem(OdysseyItemTier.GOLD, 3, -2.2f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_PICKAXE = ITEMS.register("sterling_silver_pickaxe", () -> new PickaxeItem(OdysseyItemTier.STERLING_SILVER, 5, -2.8f, new Item.Properties().tab(OdysseyItemGroup.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_PICKAXE = ITEMS_VANILLA.register("diamond_pickaxe", () -> new PickaxeItem(OdysseyItemTier.DIAMOND, 6, -2.8f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> MARINE_PICKAXE = ITEMS.register("marine_pickaxe", () -> new EquipmentPickaxeItem(OdysseyItemTier.MARINE, 4, -2.2f, new Item.Properties().tab(OdysseyItemGroup.TOOLS), AQUA_AFFINITY));
    public static final RegistryObject<Item> NETHERITE_PICKAXE = ITEMS_VANILLA.register("netherite_pickaxe", () -> new PickaxeItem(OdysseyItemTier.NETHERITE, 7, -2.8f, new Item.Properties().fireResistant().tab(ItemGroup.TAB_TOOLS)));

    //Shovels
    public static final RegistryObject<Item> WOODEN_SHOVEL = ITEMS_VANILLA.register("wooden_shovel", () -> new ShovelItem(OdysseyItemTier.WOOD, 1.5f, -2.6f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_SHOVEL = ITEMS_VANILLA.register("stone_shovel", () -> new ShovelItem(OdysseyItemTier.STONE, 2.5f, -2.6f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_SHOVEL = ITEMS_VANILLA.register("iron_shovel", () -> new ShovelItem(OdysseyItemTier.IRON, 3.5f, -2.6f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_SHOVEL = ITEMS_VANILLA.register("golden_shovel", () -> new ShovelItem(OdysseyItemTier.GOLD, 2.5f, -1.9f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_SHOVEL = ITEMS.register("sterling_silver_shovel", () -> new ShovelItem(OdysseyItemTier.STERLING_SILVER, 4.5f, -2.6f, new Item.Properties().tab(OdysseyItemGroup.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_SHOVEL = ITEMS_VANILLA.register("diamond_shovel", () -> new ShovelItem(OdysseyItemTier.DIAMOND, 5.5f, -2.6f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> MARINE_SHOVEL = ITEMS.register("marine_shovel", () -> new EquipmentShovelItem(OdysseyItemTier.MARINE, 3.5f, -1.9f, new Item.Properties().tab(OdysseyItemGroup.TOOLS), AQUA_AFFINITY));
    public static final RegistryObject<Item> NETHERITE_SHOVEL = ITEMS_VANILLA.register("netherite_shovel", () -> new ShovelItem(OdysseyItemTier.NETHERITE, 6.5f, -2.6f, new Item.Properties().fireResistant().tab(ItemGroup.TAB_TOOLS)));

    //Hoes
    public static final RegistryObject<Item> WOODEN_HOE = ITEMS_VANILLA.register("wooden_hoe", () -> new HoeItem(OdysseyItemTier.WOOD, 1, -2.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_HOE = ITEMS_VANILLA.register("stone_hoe", () -> new HoeItem(OdysseyItemTier.STONE, 2, -2.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_HOE = ITEMS_VANILLA.register("iron_hoe", () -> new HoeItem(OdysseyItemTier.IRON, 3, -2.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_HOE = ITEMS_VANILLA.register("golden_hoe", () -> new HoeItem(OdysseyItemTier.GOLD, 2, -1.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_HOE = ITEMS.register("sterling_silver_hoe", () -> new HoeItem(OdysseyItemTier.STERLING_SILVER, 4, -2.0f, new Item.Properties().tab(OdysseyItemGroup.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_HOE = ITEMS_VANILLA.register("diamond_hoe", () -> new HoeItem(OdysseyItemTier.DIAMOND, 5, -2.0f, new Item.Properties().tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> MARINE_HOE = ITEMS.register("marine_hoe", () -> new EquipmentHoeItem(OdysseyItemTier.MARINE, 3, -1.0f, new Item.Properties().tab(OdysseyItemGroup.TOOLS), AQUA_AFFINITY));
    public static final RegistryObject<Item> NETHERITE_HOE = ITEMS_VANILLA.register("netherite_hoe", () -> new HoeItem(OdysseyItemTier.NETHERITE, 6, -2.0f, new Item.Properties().fireResistant().tab(ItemGroup.TAB_TOOLS)));

    //Bats
    public static final RegistryObject<Item> GRANITE_BAT = ITEMS.register("granite_bat", () -> new EquipmentMeleeItem(OdysseyItemTier.GRANITE, 5, -2.7f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.KNOCKBACK, 1)));
    public static final RegistryObject<Item> COPPER_BAT = ITEMS.register("copper_bat", () -> new EquipmentMeleeItem(OdysseyItemTier.COPPER, 6, -2.7f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.KNOCKBACK, 2)));
    public static final RegistryObject<Item> OBSIDIAN_BAT = ITEMS.register("obsidian_bat", () -> new EquipmentMeleeItem(OdysseyItemTier.OBSIDIAN, 8, -2.7f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.KNOCKBACK, 3)));

    //Hammers
    public static final RegistryObject<Item> GRANITE_HAMMER = ITEMS.register("granite_hammer", () -> new EquipmentMeleeItem(OdysseyItemTier.GRANITE, 7, -3.2f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.BANE_OF_ARTHROPODS, 1)));
    public static final RegistryObject<Item> COPPER_HAMMER = ITEMS.register("copper_hammer", () -> new EquipmentMeleeItem(OdysseyItemTier.COPPER, 8, -3.2f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.BANE_OF_ARTHROPODS, 2)));
    public static final RegistryObject<Item> OBSIDIAN_HAMMER = ITEMS.register("obsidian_hammer", () -> new EquipmentMeleeItem(OdysseyItemTier.OBSIDIAN, 10, -3.2f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.BANE_OF_ARTHROPODS, 3)));

    //Sabres
    public static final RegistryObject<Item> FLINT_SABRE = ITEMS.register("flint_sabre", () -> new EquipmentMeleeItem(OdysseyItemTier.FLINT, 4.5f, -2.0f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.SWEEPING_EDGE, 1)));
    public static final RegistryObject<Item> AMETHYST_SABRE = ITEMS.register("amethyst_sabre", () -> new EquipmentMeleeItem(OdysseyItemTier.AMETHYST, 6.5f, -2.0f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.SWEEPING_EDGE, 2)));
    public static final RegistryObject<Item> QUARTZ_SABRE = ITEMS.register("quartz_sabre", () -> new EquipmentMeleeItem(OdysseyItemTier.QUARTZ, 7.5f, -2.0f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.SWEEPING_EDGE, 3)));
    public static final RegistryObject<Item> RAZOR_SABRE = ITEMS.register("razor_sabre", () -> new EquipmentMeleeItem(OdysseyItemTier.RAZOR, 10, -2.0f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.SWEEPING_EDGE, 4)));

    //Hatchets
    public static final RegistryObject<Item> FLINT_HATCHET = ITEMS.register("flint_hatchet", () -> new DualWieldItem(OdysseyItemTier.FLINT, 2.5f, -2.5f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.SMITE, 1)));
    public static final RegistryObject<Item> AMETHYST_HATCHET = ITEMS.register("amethyst_hatchet", () -> new DualWieldItem(OdysseyItemTier.AMETHYST, 3.5f, -2.5f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.SMITE, 2)));
    public static final RegistryObject<Item> QUARTZ_HATCHET = ITEMS.register("quartz_hatchet", () -> new DualWieldItem(OdysseyItemTier.QUARTZ, 4.5f, -2.5f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.SMITE, 3)));
    public static final RegistryObject<Item> RAZOR_HATCHET = ITEMS.register("razor_hatchet", () -> new DualWieldItem(OdysseyItemTier.RAZOR, 5.5f, -2.5f, new Item.Properties().tab(OdysseyItemGroup.COMBAT), new LevEnchSup(() -> Enchantments.SMITE, 4)));

    //Bows
    public static final RegistryObject<Item> BOW = ITEMS_VANILLA.register("bow", () -> new OdysseyBowItem((new Item.Properties()).durability(384).tab(ItemGroup.TAB_COMBAT), 0.5D));
    public static final RegistryObject<Item> NETHERITE_BOW = ITEMS.register("netherite_bow", () -> new OdysseyBowItem((new Item.Properties()).durability(1016).tab(OdysseyItemGroup.COMBAT).fireResistant(), 1.0D));

    //Crossbows
    public static final RegistryObject<Item> CROSSBOW = ITEMS_VANILLA.register("crossbow", () -> new OdysseyCrossbowItem((new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_COMBAT).durability(326), 0.5D));
    public static final RegistryObject<Item> NETHERITE_CROSSBOW = ITEMS.register("netherite_crossbow", () -> new OdysseyCrossbowItem((new Item.Properties()).stacksTo(1).tab(OdysseyItemGroup.COMBAT).durability(862).fireResistant(), 1.0D));

    //Quivers
    public static final RegistryObject<Item> LEATHER_QUIVER = ITEMS.register("leather_quiver", () -> new QuiverItem((new Item.Properties()).stacksTo(1).tab(OdysseyItemGroup.COMBAT), 3));
    public static final RegistryObject<Item> LEVIATHAN_QUIVER = ITEMS.register("leviathan_quiver", () -> new QuiverItem((new Item.Properties()).stacksTo(1).fireResistant().tab(OdysseyItemGroup.COMBAT), 5));

    //Arrows
    public static final RegistryObject<Item> ARROW = ITEMS_VANILLA.register("arrow", () -> new UpgradedArrowItem((new Item.Properties()).tab(ItemGroup.TAB_COMBAT), UpgradedArrowItem.ArrowType.FLINT));
    public static final RegistryObject<Item> AMETHYST_ARROW = ITEMS.register("amethyst_arrow", () -> new UpgradedArrowItem((new Item.Properties()).tab(OdysseyItemGroup.COMBAT), UpgradedArrowItem.ArrowType.AMETHYST));
    public static final RegistryObject<Item> QUARTZ_ARROW = ITEMS.register("quartz_arrow", () -> new UpgradedArrowItem((new Item.Properties()).tab(OdysseyItemGroup.COMBAT), UpgradedArrowItem.ArrowType.QUARTZ));
    public static final RegistryObject<Item> RAZOR_ARROW = ITEMS.register("razor_arrow", () -> new UpgradedArrowItem((new Item.Properties()).tab(OdysseyItemGroup.COMBAT), UpgradedArrowItem.ArrowType.RAZOR));

    //Tridents
    public static final RegistryObject<Item> TRIDENT = ITEMS_VANILLA.register("trident", () -> new OdysseyTridentItem((new Item.Properties()).durability(250).tab(ItemGroup.TAB_COMBAT), OdysseyTridentItem.TridentType.NORMAL));
    public static final RegistryObject<Item> LEVIATHAN_TRIDENT = ITEMS.register("leviathan_trident", () -> new OdysseyTridentItem((new Item.Properties()).durability(750).fireResistant().tab(OdysseyItemGroup.COMBAT).setISTER(() -> OdysseyItemStackTileEntityRenderer.odysseyInstance), OdysseyTridentItem.TridentType.LEVIATHAN));

    //Boomerangs
    public static final RegistryObject<Item> BONE_BOOMERANG = ITEMS.register("bone_boomerang", () -> new BoomerangItem((new Item.Properties()).durability(100).tab(OdysseyItemGroup.COMBAT).setISTER(() -> OdysseyItemStackTileEntityRenderer.odysseyInstance), BoomerangItem.BoomerangType.BONE, new LevEnchSup(EnchantmentRegistry.LOYALTY, 1)));
    public static final RegistryObject<Item> COPPER_BOOMERANG = ITEMS.register("copper_boomerang", () -> new BoomerangItem((new Item.Properties()).durability(250).tab(OdysseyItemGroup.COMBAT).setISTER(() -> OdysseyItemStackTileEntityRenderer.odysseyInstance), BoomerangItem.BoomerangType.COPPER, new LevEnchSup(EnchantmentRegistry.LOYALTY, 2)));

    //Shields
    public static final RegistryObject<Item> SHIELD = ITEMS_VANILLA.register("shield", () -> new OdysseyShieldItem((new Item.Properties()).durability(336).tab(ItemGroup.TAB_COMBAT), 5.0f, 100, () -> {NonNullList<Item> nonNullList = NonNullList.create(); nonNullList.add(Items.OAK_PLANKS); nonNullList.add(Items.BIRCH_PLANKS); nonNullList.add(Items.SPRUCE_PLANKS); nonNullList.add(Items.DARK_OAK_PLANKS); nonNullList.add(Items.JUNGLE_PLANKS); nonNullList.add(Items.ACACIA_PLANKS); return nonNullList;}));
    public static final RegistryObject<Item> LEVIATHAN_SHIELD = ITEMS.register("leviathan_shield", () -> new OdysseyShieldItem((new Item.Properties()).durability(1000).fireResistant().tab(OdysseyItemGroup.COMBAT).setISTER(() -> OdysseyItemStackTileEntityRenderer.odysseyInstance), 10.0f, 100, () -> {NonNullList<Item> nonNullList = NonNullList.create(); nonNullList.add(LEVIATHAN_SCALE.get()); return nonNullList;}));

    //Spawn Eggs
    public static final RegistryObject<Item> ARCTIHORN_SPAWN_EGG = ITEMS.register("arctihorn_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.ARCTIHORN, 0x6e7d90, 0x8ab5eb, (new Item.Properties()).tab(OdysseyItemGroup.SPAWN_EGGS)));
    public static final RegistryObject<Item> LUPINE_SPAWN_EGG = ITEMS.register("lupine_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.LUPINE, 0x000000, 0x000000, (new Item.Properties()).tab(OdysseyItemGroup.SPAWN_EGGS)));
    public static final RegistryObject<Item> BABY_SKELETON_SPAWN_EGG = ITEMS.register("baby_skeleton_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.BABY_SKELETON, 12698049, 4802889, (new Item.Properties()).tab(OdysseyItemGroup.SPAWN_EGGS)));

    // Vanilla

    // Used to stack to 16, now stack to 64
    public static final RegistryObject<Item> ENDER_PEARL = ITEMS_VANILLA.register("ender_pearl", () -> new EnderPearlItem((new Item.Properties()).tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> EGG = ITEMS_VANILLA.register("egg", () -> new EggItem((new Item.Properties()).tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> SNOWBALL = ITEMS_VANILLA.register("snowball", () -> new SnowballItem((new Item.Properties()).tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> HONEY_BOTTLE = ITEMS_VANILLA.register("honey_bottle", () -> new HoneyBottleItem((new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).food(Foods.HONEY_BOTTLE).tab(ItemGroup.TAB_FOOD)));
    public static final RegistryObject<Item> OAK_SIGN = ITEMS_VANILLA.register("oak_sign", () -> new SignItem((new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS), Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN));
    public static final RegistryObject<Item> SPRUCE_SIGN = ITEMS_VANILLA.register("spruce_sign", () -> new SignItem((new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS), Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN));
    public static final RegistryObject<Item> BIRCH_SIGN = ITEMS_VANILLA.register("birch_sign", () -> new SignItem((new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS), Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN));
    public static final RegistryObject<Item> JUNGLE_SIGN = ITEMS_VANILLA.register("jungle_sign", () -> new SignItem((new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS), Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN));
    public static final RegistryObject<Item> ACACIA_SIGN = ITEMS_VANILLA.register("acacia_sign", () -> new SignItem((new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS), Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN));
    public static final RegistryObject<Item> DARK_OAK_SIGN = ITEMS_VANILLA.register("dark_oak_sign", () -> new SignItem((new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS), Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN));
    public static final RegistryObject<Item> CRIMSON_SIGN = ITEMS_VANILLA.register("crimson_sign", () -> new SignItem((new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS), Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN));
    public static final RegistryObject<Item> WARPED_SIGN = ITEMS_VANILLA.register("warped_sign", () -> new SignItem((new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS), Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN));
    public static final RegistryObject<Item> BUCKET = ITEMS_VANILLA.register("bucket", () -> new BucketItem(Fluids.EMPTY, (new Item.Properties()).tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> WRITTEN_BOOK = ITEMS_VANILLA.register("written_book", () -> new WrittenBookItem((new Item.Properties())));
    public static final RegistryObject<Item> ARMOR_STAND = ITEMS_VANILLA.register("armor_stand", () -> new ArmorStandItem((new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> WHITE_BANNER = ITEMS_VANILLA.register("white_banner", () -> new BannerItem(Blocks.WHITE_BANNER, Blocks.WHITE_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> ORANGE_BANNER = ITEMS_VANILLA.register("orange_banner", () -> new BannerItem(Blocks.ORANGE_BANNER, Blocks.ORANGE_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> MAGENTA_BANNER = ITEMS_VANILLA.register("magenta_banner", () -> new BannerItem(Blocks.MAGENTA_BANNER, Blocks.MAGENTA_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> LIGHT_BLUE_BANNER = ITEMS_VANILLA.register("light_blue_banner", () -> new BannerItem(Blocks.LIGHT_BLUE_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> YELLOW_BANNER = ITEMS_VANILLA.register("yellow_banner", () -> new BannerItem(Blocks.YELLOW_BANNER, Blocks.YELLOW_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> LIME_BANNER = ITEMS_VANILLA.register("lime_banner", () -> new BannerItem(Blocks.LIME_BANNER, Blocks.LIME_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> PINK_BANNER = ITEMS_VANILLA.register("pink_banner", () -> new BannerItem(Blocks.PINK_BANNER, Blocks.PINK_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GRAY_BANNER = ITEMS_VANILLA.register("gray_banner", () -> new BannerItem(Blocks.GRAY_BANNER, Blocks.GRAY_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> LIGHT_GRAY_BANNER = ITEMS_VANILLA.register("light_gray_banner", () -> new BannerItem(Blocks.LIGHT_GRAY_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> CYAN_BANNER = ITEMS_VANILLA.register("cyan_banner", () -> new BannerItem(Blocks.CYAN_BANNER, Blocks.CYAN_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> PURPLE_BANNER = ITEMS_VANILLA.register("purple_banner", () -> new BannerItem(Blocks.PURPLE_BANNER, Blocks.PURPLE_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BLUE_BANNER = ITEMS_VANILLA.register("blue_banner", () -> new BannerItem(Blocks.BLUE_BANNER, Blocks.BLUE_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BROWN_BANNER = ITEMS_VANILLA.register("brown_banner", () -> new BannerItem(Blocks.BROWN_BANNER, Blocks.BROWN_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GREEN_BANNER = ITEMS_VANILLA.register("green_banner", () -> new BannerItem(Blocks.GREEN_BANNER, Blocks.GREEN_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> RED_BANNER = ITEMS_VANILLA.register("red_banner", () -> new BannerItem(Blocks.RED_BANNER, Blocks.RED_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BLACK_BANNER = ITEMS_VANILLA.register("black_banner", () -> new BannerItem(Blocks.BLACK_BANNER, Blocks.BLACK_WALL_BANNER, (new Item.Properties()).tab(ItemGroup.TAB_DECORATIONS)));

    //Potion Overwrites
    public static final RegistryObject<Item> POTION = ITEMS_VANILLA.register("potion", () -> new OdysseyPotionItem((new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_BREWING)));
    public static final RegistryObject<Item> SPLASH_POTION = ITEMS_VANILLA.register("splash_potion", () -> new OdysseySplashPotionItem((new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_BREWING)));
    public static final RegistryObject<Item> LINGERING_POTION = ITEMS_VANILLA.register("lingering_potion", () -> new OdysseyLingeringPotionItem((new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_BREWING)));

    // 1.17 Materials
    public static final RegistryObject<Item> RAW_COPPER = ITEMS.register("raw_copper", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> RAW_IRON = ITEMS.register("raw_iron", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> RAW_GOLD = ITEMS.register("raw_gold", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
    public static final RegistryObject<Item> AMETHYST_SHARD = ITEMS.register("amethyst_shard", () -> new Item((new Item.Properties()).tab(OdysseyItemGroup.MATERIALS)));
}