package com.bedmen.odyssey.util;

import com.bedmen.odyssey.armor.ModArmorMaterial;
import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.tools.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.Items;

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
    public static final RegistryObject<Item> FORTUNELESS_IRON_ORE = ITEMS.register("fortuneless_iron_ore", () -> new BlockItem(BlockRegistry.FORTUNELESS_IRON_ORE.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> FORTUNELESS_GOLD_ORE = ITEMS.register("fortuneless_gold_ore", () -> new BlockItem(BlockRegistry.FORTUNELESS_GOLD_ORE.get(), (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS)));

    //Items
    public static final RegistryObject<Item> POTION = ITEMS_VANILLA.register("potion", () -> new NewPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> SPLASH_POTION = ITEMS_VANILLA.register("splash_potion", () -> new NewSplashPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> LINGERING_POTION = ITEMS_VANILLA.register("lingering_potion", () -> new NewLingeringPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> GLASS_SHARD = ITEMS.register("glass_shard", () -> new Item((new Item.Properties()).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> BIG_GLASS_BOTTLE = ITEMS.register("big_glass_bottle", () -> new BigGlassBottleItem((new Item.Properties()).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> BIG_POTION = ITEMS.register("big_potion", () -> new BigPotionItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.BREWING)));
    public static final RegistryObject<Item> FLINT_AND_STEEL = ITEMS_VANILLA.register("flint_and_steel", () -> new NewFlintAndSteelItem((new Item.Properties()).maxDamage(64).group(ItemGroup.TOOLS)));
    public static final RegistryObject<Item> DIAMOND_SHARD = ITEMS.register("diamond_shard", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> SILVER_DUST = ITEMS.register("silver_dust", () -> new Item((new Item.Properties()).group(ItemGroup.MATERIALS)));

    //Armor
    public static final RegistryObject<ArmorItem> SILVER_HELMET = ITEMS.register("silver_helmet", () -> new ArmorItem(ModArmorMaterial.SILVER, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> SILVER_CHESTPLATE = ITEMS.register("silver_chestplate", () -> new ArmorItem(ModArmorMaterial.SILVER, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> SILVER_LEGGINGS = ITEMS.register("silver_leggings", () -> new ArmorItem(ModArmorMaterial.SILVER, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> SILVER_BOOTS = ITEMS.register("silver_boots", () -> new ArmorItem(ModArmorMaterial.SILVER, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));

    //Tools

    //Swords
    public static final RegistryObject<ModSwordItem> WOODEN_SWORD = ITEMS_VANILLA.register("wooden_sword", () -> new ModSwordItem(ModItemTier.WOOD, 4.0f, 1.6f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ModSwordItem> STONE_SWORD = ITEMS_VANILLA.register("stone_sword", () -> new ModSwordItem(ModItemTier.STONE, 5.0f, 1.6f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ModSwordItem> IRON_SWORD = ITEMS_VANILLA.register("iron_sword", () -> new ModSwordItem(ModItemTier.IRON, 6.0f, 1.6f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ModSwordItem> GOLDEN_SWORD = ITEMS_VANILLA.register("golden_sword", () -> new ModSwordItem(ModItemTier.GOLD, 4.5f, 2.4f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ModSwordItem> SILVER_SWORD = ITEMS.register("silver_sword", () -> new ModSwordItem(ModItemTier.SILVER, 7.0f, 1.6f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ModSwordItem> DIAMOND_SWORD = ITEMS_VANILLA.register("diamond_sword", () -> new ModSwordItem(ModItemTier.DIAMOND, 8.0f, 1.6f, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ModSwordItem> NETHERITE_SWORD = ITEMS_VANILLA.register("netherite_sword", () -> new ModSwordItem(ModItemTier.NETHERITE, 9.0f, 1.6f, new Item.Properties().group(ItemGroup.COMBAT)));

    //Axes
    public static final RegistryObject<ModAxeItem> WOODEN_AXE = ITEMS_VANILLA.register("wooden_axe", () -> new ModAxeItem(ModItemTier.WOOD, 6.0f, 1.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModAxeItem> STONE_AXE = ITEMS_VANILLA.register("stone_axe", () -> new ModAxeItem(ModItemTier.STONE, 7.0f, 1.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModAxeItem> IRON_AXE = ITEMS_VANILLA.register("iron_axe", () -> new ModAxeItem(ModItemTier.IRON, 8.0f, 1.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModAxeItem> GOLDEN_AXE = ITEMS_VANILLA.register("golden_axe", () -> new ModAxeItem(ModItemTier.GOLD, 6.0f, 1.5f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModAxeItem> SILVER_AXE = ITEMS.register("silver_axe", () -> new ModAxeItem(ModItemTier.SILVER, 9.0f, 1.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModAxeItem> DIAMOND_AXE = ITEMS_VANILLA.register("diamond_axe", () -> new ModAxeItem(ModItemTier.DIAMOND, 10.5f, 1.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModAxeItem> NETHERITE_AXE = ITEMS_VANILLA.register("netherite_axe", () -> new ModAxeItem(ModItemTier.NETHERITE, 12.0f, 1.0f, new Item.Properties().group(ItemGroup.TOOLS)));

    //Pickaxes
    public static final RegistryObject<ModPickaxeItem> WOODEN_PICKAXE = ITEMS_VANILLA.register("wooden_pickaxe", () -> new ModPickaxeItem(ModItemTier.WOOD, 2.0f, 1.2f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModPickaxeItem> STONE_PICKAXE = ITEMS_VANILLA.register("stone_pickaxe", () -> new ModPickaxeItem(ModItemTier.STONE, 3.0f, 1.2f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModPickaxeItem> IRON_PICKAXE = ITEMS_VANILLA.register("iron_pickaxe", () -> new ModPickaxeItem(ModItemTier.IRON, 4.0f, 1.2f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModPickaxeItem> GOLDEN_PICKAXE = ITEMS_VANILLA.register("golden_pickaxe", () -> new ModPickaxeItem(ModItemTier.GOLD, 3.0f, 1.8f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModPickaxeItem> SILVER_PICKAXE = ITEMS.register("silver_pickaxe", () -> new ModPickaxeItem(ModItemTier.SILVER, 5.0f, 1.2f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModPickaxeItem> DIAMOND_PICKAXE = ITEMS_VANILLA.register("diamond_pickaxe", () -> new ModPickaxeItem(ModItemTier.DIAMOND, 6.0f, 1.2f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModPickaxeItem> NETHERITE_PICKAXE = ITEMS_VANILLA.register("netherite_pickaxe", () -> new ModPickaxeItem(ModItemTier.NETHERITE, 7.0f, 1.2f, new Item.Properties().group(ItemGroup.TOOLS)));

    //Shovels
    public static final RegistryObject<ModShovelItem> WOODEN_SHOVEL = ITEMS_VANILLA.register("wooden_shovel", () -> new ModShovelItem(ModItemTier.WOOD, 1.5f, 1.4f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModShovelItem> STONE_SHOVEL = ITEMS_VANILLA.register("stone_shovel", () -> new ModShovelItem(ModItemTier.STONE, 2.5f, 1.4f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModShovelItem> IRON_SHOVEL = ITEMS_VANILLA.register("iron_shovel", () -> new ModShovelItem(ModItemTier.IRON, 3.5f, 1.4f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModShovelItem> GOLDEN_SHOVEL = ITEMS_VANILLA.register("golden_shovel", () -> new ModShovelItem(ModItemTier.GOLD, 2.75f, 2.1f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModShovelItem> SILVER_SHOVEL = ITEMS.register("silver_shovel", () -> new ModShovelItem(ModItemTier.SILVER, 4.5f, 1.4f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModShovelItem> DIAMOND_SHOVEL = ITEMS_VANILLA.register("diamond_shovel", () -> new ModShovelItem(ModItemTier.DIAMOND, 5.5f, 1.4f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModShovelItem> NETHERITE_SHOVEL = ITEMS_VANILLA.register("netherite_shovel", () -> new ModShovelItem(ModItemTier.NETHERITE, 6.5f, 1.4f, new Item.Properties().group(ItemGroup.TOOLS)));

    //Hoes
    public static final RegistryObject<ModHoeItem> WOODEN_HOE = ITEMS_VANILLA.register("wooden_hoe", () -> new ModHoeItem(ModItemTier.WOOD, 1.0f, 2.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModHoeItem> STONE_HOE = ITEMS_VANILLA.register("stone_hoe", () -> new ModHoeItem(ModItemTier.STONE, 2.0f, 2.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModHoeItem> IRON_HOE = ITEMS_VANILLA.register("iron_hoe", () -> new ModHoeItem(ModItemTier.IRON, 3.0f, 2.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModHoeItem> GOLDEN_HOE = ITEMS_VANILLA.register("golden_hoe", () -> new ModHoeItem(ModItemTier.GOLD, 2.25f, 3.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModHoeItem> SILVER_HOE = ITEMS.register("silver_hoe", () -> new ModHoeItem(ModItemTier.SILVER, 4.0f, 2.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModHoeItem> DIAMOND_HOE = ITEMS_VANILLA.register("diamond_hoe", () -> new ModHoeItem(ModItemTier.DIAMOND, 5.0f, 2.0f, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ModHoeItem> NETHERITE_HOE = ITEMS_VANILLA.register("netherite_hoe", () -> new ModHoeItem(ModItemTier.NETHERITE, 6.0f, 2.0f, new Item.Properties().group(ItemGroup.TOOLS)));

    //Spawn Eggs
    //public static final RegistryObject<Item> NETHER_CREEPER_SPAWN_EGG = ITEMS.register("nether_creeper_spawn_egg", () -> new ModSpawnEggItem(EntityTypeRegistry.NETHER_CREEPER, 0x511515, 0xff7f27, (new Item.Properties()).group(ItemGroup.MISC)));
}