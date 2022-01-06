package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.armor.OdysseyArmorMaterials;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.entity.projectile.Boomerang;
import com.bedmen.odyssey.entity.projectile.OdysseyArrow;
import com.bedmen.odyssey.entity.vehicle.OdysseyBoat;
import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.items.equipment.*;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.tools.OdysseyTiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Odyssey.MOD_ID);
    public static DeferredRegister<Item> ITEMS_VANILLA = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Building Blocks
    public static final RegistryObject<Item> LEATHER_PILE = ITEMS.register("leather_pile", () -> new BlockItem(BlockRegistry.LEATHER_PILE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> COPPER_COBWEB = ITEMS.register("copper_cobweb", () -> new BlockItem(BlockRegistry.COPPER_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_ORE = ITEMS.register("silver_ore", () -> new BlockItem(BlockRegistry.SILVER_ORE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> DEEPSLATE_SILVER_ORE = ITEMS.register("deepslate_silver_ore", () -> new BlockItem(BlockRegistry.DEEPSLATE_SILVER_ORE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> RAW_SILVER_BLOCK = ITEMS.register("raw_silver_block", () -> new BlockItem(BlockRegistry.RAW_SILVER_BLOCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_COBWEB = ITEMS.register("silver_cobweb", () -> new BlockItem(BlockRegistry.SILVER_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_BLOCK = ITEMS.register("silver_block", () -> new BlockItem(BlockRegistry.SILVER_BLOCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> GOLDEN_COBWEB = ITEMS.register("golden_cobweb", () -> new BlockItem(BlockRegistry.GOLDEN_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> ALLOY_FURNACE = ITEMS.register("alloy_furnace", () -> new BlockItem(BlockRegistry.ALLOY_FURNACE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STERLING_SILVER_COBWEB = ITEMS.register("sterling_silver_cobweb", () -> new BlockItem(BlockRegistry.STERLING_SILVER_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STERLING_SILVER_CHEST = ITEMS.register("sterling_silver_chest", () -> new BEWLRBlockItem(BlockRegistry.STERLING_SILVER_CHEST.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STERLING_SILVER_BLOCK = ITEMS.register("sterling_silver_block", () -> new BlockItem(BlockRegistry.STERLING_SILVER_BLOCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> ELECTRUM_COBWEB = ITEMS.register("electrum_cobweb", () -> new BlockItem(BlockRegistry.ELECTRUM_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> ELECTRUM_BLOCK = ITEMS.register("electrum_block", () -> new BlockItem(BlockRegistry.ELECTRUM_BLOCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> CLOVER_STONE = ITEMS.register("clover_stone", () -> new BlockItem(BlockRegistry.CLOVER_STONE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));

    //Wood and Plants
    public static final RegistryObject<Item> PALM_PLANKS = ITEMS.register("palm_planks", () -> new BlockItem(BlockRegistry.PALM_PLANKS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_SAPLING = ITEMS.register("palm_sapling", () -> new BlockItem(BlockRegistry.PALM_SAPLING.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_LOG = ITEMS.register("palm_log", () -> new BlockItem(BlockRegistry.PALM_LOG.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> STRIPPED_PALM_LOG = ITEMS.register("stripped_palm_log", () -> new BlockItem(BlockRegistry.STRIPPED_PALM_LOG.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> STRIPPED_PALM_WOOD = ITEMS.register("stripped_palm_wood", () -> new BlockItem(BlockRegistry.STRIPPED_PALM_WOOD.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_WOOD = ITEMS.register("palm_wood", () -> new BlockItem(BlockRegistry.PALM_WOOD.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_LEAVES = ITEMS.register("palm_leaves", () -> new BlockItem(BlockRegistry.PALM_LEAVES.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_CORNER_LEAVES = ITEMS.register("palm_corner_leaves", () -> new BlockItem(BlockRegistry.PALM_CORNER_LEAVES.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_SLAB = ITEMS.register("palm_slab", () -> new BlockItem(BlockRegistry.PALM_SLAB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_PRESSURE_PLATE = ITEMS.register("palm_pressure_plate", () -> new BlockItem(BlockRegistry.PALM_PRESSURE_PLATE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_FENCE = ITEMS.register("palm_fence", () -> new BurnableFenceItem(BlockRegistry.PALM_FENCE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_TRAPDOOR = ITEMS.register("palm_trapdoor", () -> new BlockItem(BlockRegistry.PALM_TRAPDOOR.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_FENCE_GATE = ITEMS.register("palm_fence_gate", () -> new BurnableFenceItem(BlockRegistry.PALM_FENCE_GATE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_BUTTON = ITEMS.register("palm_button", () -> new BlockItem(BlockRegistry.PALM_BUTTON.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_STAIRS = ITEMS.register("palm_stairs", () -> new BlockItem(BlockRegistry.PALM_STAIRS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_DOOR = ITEMS.register("palm_door", () -> new BlockItem(BlockRegistry.PALM_DOOR.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_SIGN = ITEMS.register("palm_sign", () -> new SignItem((new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD), BlockRegistry.PALM_SIGN.get(), BlockRegistry.PALM_WALL_SIGN.get()));
    public static final RegistryObject<Item> PALM_BOAT = ITEMS.register("palm_boat", () -> new OdysseyBoatItem(OdysseyBoat.Type.PALM, (new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.WOOD)));

    //Materials
    public static final RegistryObject<Item> COCONUT_FLOWER = ITEMS.register("coconut_flower", () -> new ItemNameBlockItem(BlockRegistry.COCONUT.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> FEATHER_BUNDLE = ITEMS.register("feather_bundle", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> POLAR_BEAR_FUR = ITEMS.register("polar_bear_fur", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> COPPER_FIBER = ITEMS.register("copper_fiber", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> SILVER_FIBER = ITEMS.register("silver_fiber", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> GOLDEN_FIBER = ITEMS.register("golden_fiber", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> TRIWEAVE_FIBER = ITEMS.register("triweave_fiber", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> STERLING_SILVER_NUGGET = ITEMS.register("sterling_silver_nugget", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> STERLING_SILVER_INGOT = ITEMS.register("sterling_silver_ingot", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> STERLING_SILVER_FIBER = ITEMS.register("sterling_silver_fiber", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> ELECTRUM_NUGGET = ITEMS.register("electrum_nugget", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> ELECTRUM_INGOT = ITEMS.register("electrum_ingot", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> ELECTRUM_FIBER = ITEMS.register("electrum_fiber", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> PEARL = ITEMS.register("pearl", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> PERMAFROST_SHARD = ITEMS.register("permafrost_shard", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> NETHERITE_NUGGET = ITEMS.register("netherite_nugget", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));

    // LevEnchSups for Equipment
    public static final LevEnchSup ICE_PROTECTION_1 = new LevEnchSup(EnchantmentRegistry.ICE_PROTECTION, 1);
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
    public static final LevEnchSup FORTUNE_1 = new LevEnchSup(() -> Enchantments.BLOCK_FORTUNE, 1);
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
    public static final LevEnchSup LOYALTY_3 = new LevEnchSup(EnchantmentRegistry.LOYALTY, 3);

    //Tools
    public static final RegistryObject<Item> CLOVER_STONE_AXE = ITEMS.register("clover_stone_axe", () -> new EquipmentAxeItem(OdysseyTiers.CLOVER_STONE, 9f, -3.0f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), FORTUNE_1));
    public static final RegistryObject<Item> CLOVER_STONE_PICKAXE = ITEMS.register("clover_stone_pickaxe", () -> new EquipmentPickaxeItem(OdysseyTiers.CLOVER_STONE, 5, -2.8f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), FORTUNE_1));
    public static final RegistryObject<Item> CLOVER_STONE_SHOVEL = ITEMS.register("clover_stone_shovel", () -> new EquipmentShovelItem(OdysseyTiers.CLOVER_STONE, 4.5f, -2.6f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), FORTUNE_1));
    public static final RegistryObject<Item> CLOVER_STONE_HOE = ITEMS.register("clover_stone_hoe", () -> new EquipmentHoeItem(OdysseyTiers.CLOVER_STONE, 4, -2f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), FORTUNE_1));
    
    public static final RegistryObject<Item> STERLING_SILVER_AXE = ITEMS.register("sterling_silver_axe", () -> new AxeItem(OdysseyTiers.STERLING_SILVER, 9.0f, -3.0f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_PICKAXE = ITEMS.register("sterling_silver_pickaxe", () -> new PickaxeItem(OdysseyTiers.STERLING_SILVER, 5, -2.8f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_SHOVEL = ITEMS.register("sterling_silver_shovel", () -> new ShovelItem(OdysseyTiers.STERLING_SILVER, 4.5f, -2.6f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_HOE = ITEMS.register("sterling_silver_hoe", () -> new HoeItem(OdysseyTiers.STERLING_SILVER, 4, -2f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS)));
    public static final RegistryObject<Item> STERLING_SILVER_KEY = ITEMS.register("sterling_silver_key", () -> new KeyItem(TreasureChestMaterial.STERLING_SILVER, (new Item.Properties()).tab(OdysseyCreativeModeTab.TOOLS)));

    public static final RegistryObject<Item> MARINE_AXE = ITEMS.register("marine_axe", () -> new EquipmentAxeItem(OdysseyTiers.MARINE, 8.0f, -2.5f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), AQUA_AFFINITY));
    public static final RegistryObject<Item> MARINE_PICKAXE = ITEMS.register("marine_pickaxe", () -> new EquipmentPickaxeItem(OdysseyTiers.MARINE, 4, -2.2f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), AQUA_AFFINITY));
    public static final RegistryObject<Item> MARINE_SHOVEL = ITEMS.register("marine_shovel", () -> new EquipmentShovelItem(OdysseyTiers.MARINE, 3.5f, -1.9f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), AQUA_AFFINITY));
    public static final RegistryObject<Item> MARINE_HOE = ITEMS.register("marine_hoe", () -> new EquipmentHoeItem(OdysseyTiers.MARINE, 3, -1.0f, new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), AQUA_AFFINITY));

    //Melee Weapons
    public static final RegistryObject<Item> FLINT_HATCHET = ITEMS.register("flint_hatchet", () -> new DualWieldItem(OdysseyTiers.FLINT, 4.5f, -2.5f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SMITE_1));
    public static final RegistryObject<Item> FLINT_SABRE = ITEMS.register("flint_sabre", () -> new EquipmentMeleeItem(OdysseyTiers.FLINT, 5f, -2.0f, true, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SWEEPING_EDGE_1));

    public static final RegistryObject<Item> COPPER_HAMMER = ITEMS.register("copper_hammer", () -> new EquipmentMeleeItem(OdysseyTiers.COPPER, 8f, -3.2f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), BANE_OF_ARTHROPODS_1));
    public static final RegistryObject<Item> COPPER_BAT = ITEMS.register("copper_bat", () -> new EquipmentMeleeItem(OdysseyTiers.COPPER, 6f, -2.7f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), KNOCKBACK_1));
    public static final RegistryObject<Item> COPPER_BATTLE_AXE = ITEMS.register("copper_battle_axe", () -> new EquipmentMeleeItem(OdysseyTiers.COPPER, 8f, -3.1f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SHATTERING_1));

    public static final RegistryObject<Item> CLOVER_STONE_SWORD = ITEMS.register("clover_stone_sword", () -> new EquipmentMeleeItem(OdysseyTiers.CLOVER_STONE, 7f, -2.4f, true, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), LOOTING_1));
    public static final RegistryObject<Item> CLOVER_STONE_HATCHET = ITEMS.register("clover_stone_hatchet", () -> new DualWieldItem(OdysseyTiers.CLOVER_STONE, 5f, -2.5f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SMITE_1, LOOTING_1));
    public static final RegistryObject<Item> CLOVER_STONE_SABRE = ITEMS.register("clover_stone_sabre", () -> new EquipmentMeleeItem(OdysseyTiers.CLOVER_STONE, 6f, -2.0f, true, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SWEEPING_EDGE_1, LOOTING_1));
    public static final RegistryObject<Item> CLOVER_STONE_HAMMER = ITEMS.register("clover_stone_hammer", () -> new EquipmentMeleeItem(OdysseyTiers.CLOVER_STONE, 9f, -3.2f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), BANE_OF_ARTHROPODS_1, LOOTING_1));
    public static final RegistryObject<Item> CLOVER_STONE_BAT = ITEMS.register("clover_stone_bat", () -> new EquipmentMeleeItem(OdysseyTiers.CLOVER_STONE, 7f, -2.7f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), KNOCKBACK_1, LOOTING_1));
    public static final RegistryObject<Item> CLOVER_STONE_BATTLE_AXE = ITEMS.register("clover_stone_battle_axe", () -> new EquipmentMeleeItem(OdysseyTiers.CLOVER_STONE, 9f, -3.1f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SHATTERING_1, LOOTING_1));
    
    public static final RegistryObject<Item> STERLING_SILVER_SWORD = ITEMS.register("sterling_silver_sword", () -> new SwordItem(OdysseyTiers.STERLING_SILVER, 7, -2.4f, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE)));

    public static final RegistryObject<Item> AMETHYST_HATCHET = ITEMS.register("amethyst_hatchet", () -> new DualWieldItem(OdysseyTiers.AMETHYST, 5.5f, -2.5f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SMITE_2));
    public static final RegistryObject<Item> AMETHYST_SABRE = ITEMS.register("amethyst_sabre", () -> new EquipmentMeleeItem(OdysseyTiers.AMETHYST, 7f, -2.0f, true, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SWEEPING_EDGE_2));

    public static final RegistryObject<Item> OBSIDIAN_HAMMER = ITEMS.register("obsidian_hammer", () -> new EquipmentMeleeItem(OdysseyTiers.OBSIDIAN, 10f, -3.2f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), BANE_OF_ARTHROPODS_2));
    public static final RegistryObject<Item> OBSIDIAN_BAT = ITEMS.register("obsidian_bat", () -> new EquipmentMeleeItem(OdysseyTiers.OBSIDIAN, 8f, -2.7f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), KNOCKBACK_2));
    public static final RegistryObject<Item> OBSIDIAN_BATTLE_AXE = ITEMS.register("obsidian_battle_axe", () -> new EquipmentMeleeItem(OdysseyTiers.OBSIDIAN, 10f, -3.1f, false, new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), SHATTERING_2));

    //Ranged Weapons
    public static final RegistryObject<Item> WOODEN_BOOMERANG = ITEMS.register("wooden_boomerang", () -> new BoomerangItem((new Item.Properties()).durability(OdysseyTiers.WOOD.getUses()).tab(OdysseyCreativeModeTab.RANGED), Boomerang.BoomerangType.WOOD, LOYALTY_1));

    public static final RegistryObject<Item> RABBIT_HIDE_QUIVER = ITEMS.register("rabbit_hide_quiver", () -> new QuiverItem((new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.RANGED), QuiverItem.QuiverType.RABBIT_HIDE_QUIVER));
    public static final RegistryObject<Item> RABBIT_HIDE_ROCKET_BAG = ITEMS.register("rabbit_hide_rocket_bag", () -> new QuiverItem((new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.RANGED), QuiverItem.QuiverType.RABBIT_HIDE_ROCKET_BAG));

    public static final RegistryObject<Item> BONE_BOOMERANG = ITEMS.register("bone_boomerang", () -> new BoomerangItem((new Item.Properties()).durability(OdysseyTiers.BONE.getUses()).tab(OdysseyCreativeModeTab.RANGED), Boomerang.BoomerangType.BONE, LOYALTY_1));
    public static final RegistryObject<Item> BONERANG = ITEMS.register("bonerang", () -> new BoomerangItem((new Item.Properties()).durability(OdysseyTiers.BONE.getUses()).tab(OdysseyCreativeModeTab.RANGED), Boomerang.BoomerangType.BONERANG, LOYALTY_3));
    public static final RegistryObject<Item> BONE_LONG_BOW = ITEMS.register("bone_long_bow", () -> new EquipmentBowItem((new Item.Properties()).durability(OdysseyTiers.BONE.getUses()).tab(OdysseyCreativeModeTab.RANGED),1.2f, 20, PIERCING_1));
    public static final RegistryObject<Item> BONE_REPEATER = ITEMS.register("bone_repeater", () -> new RepeaterItem((new Item.Properties()).durability(OdysseyTiers.BONE.getUses()).tab(OdysseyCreativeModeTab.RANGED),1.0f, 20, QUICK_CHARGE_1));
    public static final RegistryObject<Item> BONE_SLUG_BOW = ITEMS.register("bone_slug_bow", () -> new EquipmentCrossbowItem((new Item.Properties()).durability(OdysseyTiers.BONE.getUses()).tab(OdysseyCreativeModeTab.RANGED),1.5f, 25, PUNCH_1));
    public static final RegistryObject<Item> BOWN = ITEMS.register("bown", () -> new RepeaterItem((new Item.Properties()).durability(OdysseyTiers.BONE.getUses()).tab(OdysseyCreativeModeTab.RANGED),1.2f, 15, QUICK_CHARGE_1, PUNCH_1, PIERCING_1));

    public static final RegistryObject<Item> CLOVER_STONE_BOOMERANG = ITEMS.register("clover_stone_boomerang", () -> new BoomerangItem((new Item.Properties()).durability(OdysseyTiers.CLOVER_STONE.getUses()).tab(OdysseyCreativeModeTab.RANGED), Boomerang.BoomerangType.CLOVER_STONE, LOYALTY_1, LOOTING_1));
    public static final RegistryObject<Item> CLOVER_STONE_ARROW = ITEMS.register("clover_stone_arrow", () -> new OdysseyArrowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyArrow.ArrowType.CLOVER_STONE));

    public static final RegistryObject<Item> AMETHYST_ARROW = ITEMS.register("amethyst_arrow", () -> new OdysseyArrowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyArrow.ArrowType.AMETHYST));

    //Armors
    public static final RegistryObject<Item> HOLLOW_COCONUT = ITEMS.register("hollow_coconut", () -> new HollowCoconutItem(OdysseyArmorMaterials.COCONUT, EquipmentSlot.HEAD, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), BINDING));

    public static final RegistryObject<Item> CHICKEN_HELMET = ITEMS.register("chicken_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterials.CHICKEN, EquipmentSlot.HEAD, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FALL_PROTECTION_1));
    public static final RegistryObject<Item> CHICKEN_CHESTPLATE = ITEMS.register("chicken_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterials.CHICKEN, EquipmentSlot.CHEST, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FALL_PROTECTION_1));
    public static final RegistryObject<Item> CHICKEN_LEGGINGS = ITEMS.register("chicken_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterials.CHICKEN, EquipmentSlot.LEGS, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FALL_PROTECTION_1));
    public static final RegistryObject<Item> CHICKEN_BOOTS = ITEMS.register("chicken_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterials.CHICKEN, EquipmentSlot.FEET, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FALL_PROTECTION_1));

    public static final RegistryObject<Item> FUR_HELMET = ITEMS.register("fur_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterials.FUR, EquipmentSlot.HEAD, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), ICE_PROTECTION_1));
    public static final RegistryObject<Item> FUR_CHESTPLATE = ITEMS.register("fur_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterials.FUR, EquipmentSlot.CHEST, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), ICE_PROTECTION_1));
    public static final RegistryObject<Item> FUR_LEGGINGS = ITEMS.register("fur_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterials.FUR, EquipmentSlot.LEGS, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), ICE_PROTECTION_1));
    public static final RegistryObject<Item> FUR_BOOTS = ITEMS.register("fur_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterials.FUR, EquipmentSlot.FEET, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), ICE_PROTECTION_1));
    
    public static final RegistryObject<Item> TURTLE_HELMET = ITEMS.register("turtle_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterials.TURTLE, EquipmentSlot.HEAD, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), RESPIRATION_1));
    public static final RegistryObject<Item> TURTLE_CHESTPLATE = ITEMS.register("turtle_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterials.TURTLE, EquipmentSlot.CHEST, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), RESPIRATION_1));
    public static final RegistryObject<Item> TURTLE_LEGGINGS = ITEMS.register("turtle_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterials.TURTLE, EquipmentSlot.LEGS, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), DEPTH_STRIDER_1));
    public static final RegistryObject<Item> TURTLE_BOOTS = ITEMS.register("turtle_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterials.TURTLE, EquipmentSlot.FEET, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), DEPTH_STRIDER_1));

    public static final RegistryObject<Item> STERLING_SILVER_HORSE_ARMOR = ITEMS.register("sterling_silver_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterials.STERLING_SILVER.getTotalDefense(), new ResourceLocation(Odyssey.MOD_ID, "textures/entity/horse/armor/horse_armor_sterling_silver.png"), (new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.ARMOR)));
    public static final RegistryObject<Item> STERLING_SILVER_HELMET = ITEMS.register("sterling_silver_helmet", () -> new ArmorItem(OdysseyArmorMaterials.STERLING_SILVER, EquipmentSlot.HEAD, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR)));
    public static final RegistryObject<Item> STERLING_SILVER_CHESTPLATE = ITEMS.register("sterling_silver_chestplate", () -> new ArmorItem(OdysseyArmorMaterials.STERLING_SILVER, EquipmentSlot.CHEST, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR)));
    public static final RegistryObject<Item> STERLING_SILVER_LEGGINGS = ITEMS.register("sterling_silver_leggings", () -> new ArmorItem(OdysseyArmorMaterials.STERLING_SILVER, EquipmentSlot.LEGS, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR)));
    public static final RegistryObject<Item> STERLING_SILVER_BOOTS = ITEMS.register("sterling_silver_boots", () -> new ArmorItem(OdysseyArmorMaterials.STERLING_SILVER, EquipmentSlot.FEET, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR)));

    public static final RegistryObject<Item> ARCTIC_HELMET = ITEMS.register("arctic_helmet", () -> new EquipmentArmorItem(OdysseyArmorMaterials.ARCTIC, EquipmentSlot.HEAD, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FIRE_PROTECTION_1));
    public static final RegistryObject<Item> ARCTIC_CHESTPLATE = ITEMS.register("arctic_chestplate", () -> new EquipmentArmorItem(OdysseyArmorMaterials.ARCTIC, EquipmentSlot.CHEST, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FIRE_PROTECTION_1));
    public static final RegistryObject<Item> ARCTIC_LEGGINGS = ITEMS.register("arctic_leggings", () -> new EquipmentArmorItem(OdysseyArmorMaterials.ARCTIC, EquipmentSlot.LEGS, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FIRE_PROTECTION_1));
    public static final RegistryObject<Item> ARCTIC_BOOTS = ITEMS.register("arctic_boots", () -> new EquipmentArmorItem(OdysseyArmorMaterials.ARCTIC, EquipmentSlot.FEET, new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), FIRE_PROTECTION_1));

    public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterials.NETHERITE.getTotalDefense(), new ResourceLocation(Odyssey.MOD_ID, "textures/entity/horse/armor/horse_armor_netherite.png"), (new Item.Properties()).stacksTo(1).fireResistant().tab(OdysseyCreativeModeTab.ARMOR)));

    //Spawning
    public static final RegistryObject<Item> BABY_SKELETON_SPAWN_EGG = ITEMS.register("baby_skeleton_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.BABY_SKELETON, 12698049, 4802889, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> BABY_CREEPER_SPAWN_EGG = ITEMS.register("baby_creeper_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.BABY_CREEPER, 894731, 0, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> CAMO_CREEPER_SPAWN_EGG = ITEMS.register("camo_creeper_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.CAMO_CREEPER, 0x50692c, 0x79553a, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> WEAVER_SPAWN_EGG = ITEMS.register("weaver_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.WEAVER, 0x442512, 0xff4444, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> PASSIVE_WEAVER_SPAWN_EGG = ITEMS.register("passive_weaver_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.PASSIVE_WEAVER, 0x442512, 0x00b0ff, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> BABY_LEVIATHAN_SPAWN_EGG = ITEMS.register("baby_leviathan_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.BABY_LEVIATHAN, 0x2f2f37, 0x646464, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> BABY_LEVIATHAN_BUCKET = ITEMS.register("baby_leviathan_bucket", () -> new BabyLeviathanBucket((new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> WEAVER_EGG = ITEMS.register("weaver_egg", () -> new WeaverEggItem((new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));

    /////////////////////////////////////////////////////////////////////////

    //Vanilla Overrides
    
    public static final RegistryObject<Item> WOODEN_SWORD = ITEMS_VANILLA.register("wooden_sword", () -> new SwordItem(OdysseyTiers.WOOD, 4, -2.4f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> STONE_SWORD = ITEMS_VANILLA.register("stone_sword", () -> new SwordItem(OdysseyTiers.STONE, 5, -2.4f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_SWORD = ITEMS_VANILLA.register("iron_sword", () -> new SwordItem(OdysseyTiers.IRON, 6, -2.4f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> GOLDEN_SWORD = ITEMS_VANILLA.register("golden_sword", () -> new SwordItem(OdysseyTiers.GOLD, 5, -1.6f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> DIAMOND_SWORD = ITEMS_VANILLA.register("diamond_sword", () -> new SwordItem(OdysseyTiers.DIAMOND, 8, -2.4f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> NETHERITE_SWORD = ITEMS_VANILLA.register("netherite_sword", () -> new SwordItem(OdysseyTiers.NETHERITE, 9, -2.4f, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistryObject<Item> WOODEN_AXE = ITEMS_VANILLA.register("wooden_axe", () -> new AxeItem(OdysseyTiers.WOOD, 6.0f, -3.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_AXE = ITEMS_VANILLA.register("stone_axe", () -> new AxeItem(OdysseyTiers.STONE, 7.0f, -3.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_AXE = ITEMS_VANILLA.register("iron_axe", () -> new AxeItem(OdysseyTiers.IRON, 8.0f, -3.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_AXE = ITEMS_VANILLA.register("golden_axe", () -> new AxeItem(OdysseyTiers.GOLD, 7.0f, -2.5f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> DIAMOND_AXE = ITEMS_VANILLA.register("diamond_axe", () -> new AxeItem(OdysseyTiers.DIAMOND, 10.5f, -3.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> NETHERITE_AXE = ITEMS_VANILLA.register("netherite_axe", () -> new AxeItem(OdysseyTiers.NETHERITE, 12.0f, -3.0f, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_TOOLS)));

    public static final RegistryObject<Item> WOODEN_PICKAXE = ITEMS_VANILLA.register("wooden_pickaxe", () -> new PickaxeItem(OdysseyTiers.WOOD, 2, -2.8f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_PICKAXE = ITEMS_VANILLA.register("stone_pickaxe", () -> new PickaxeItem(OdysseyTiers.STONE, 3, -2.8f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_PICKAXE = ITEMS_VANILLA.register("iron_pickaxe", () -> new PickaxeItem(OdysseyTiers.IRON, 4, -2.8f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_PICKAXE = ITEMS_VANILLA.register("golden_pickaxe", () -> new PickaxeItem(OdysseyTiers.GOLD, 3, -2.2f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> DIAMOND_PICKAXE = ITEMS_VANILLA.register("diamond_pickaxe", () -> new PickaxeItem(OdysseyTiers.DIAMOND, 6, -2.8f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> NETHERITE_PICKAXE = ITEMS_VANILLA.register("netherite_pickaxe", () -> new PickaxeItem(OdysseyTiers.NETHERITE, 7, -2.8f, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_TOOLS)));

    public static final RegistryObject<Item> WOODEN_SHOVEL = ITEMS_VANILLA.register("wooden_shovel", () -> new ShovelItem(OdysseyTiers.WOOD, 1.5f, -2.6f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_SHOVEL = ITEMS_VANILLA.register("stone_shovel", () -> new ShovelItem(OdysseyTiers.STONE, 2.5f, -2.6f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_SHOVEL = ITEMS_VANILLA.register("iron_shovel", () -> new ShovelItem(OdysseyTiers.IRON, 3.5f, -2.6f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_SHOVEL = ITEMS_VANILLA.register("golden_shovel", () -> new ShovelItem(OdysseyTiers.GOLD, 2.5f, -1.9f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> DIAMOND_SHOVEL = ITEMS_VANILLA.register("diamond_shovel", () -> new ShovelItem(OdysseyTiers.DIAMOND, 5.5f, -2.6f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> NETHERITE_SHOVEL = ITEMS_VANILLA.register("netherite_shovel", () -> new ShovelItem(OdysseyTiers.NETHERITE, 6.5f, -2.6f, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_TOOLS)));

    public static final RegistryObject<Item> WOODEN_HOE = ITEMS_VANILLA.register("wooden_hoe", () -> new HoeItem(OdysseyTiers.WOOD, 1, -2.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> STONE_HOE = ITEMS_VANILLA.register("stone_hoe", () -> new HoeItem(OdysseyTiers.STONE, 2, -2.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> IRON_HOE = ITEMS_VANILLA.register("iron_hoe", () -> new HoeItem(OdysseyTiers.IRON, 3, -2.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> GOLDEN_HOE = ITEMS_VANILLA.register("golden_hoe", () -> new HoeItem(OdysseyTiers.GOLD, 2, -1.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> DIAMOND_HOE = ITEMS_VANILLA.register("diamond_hoe", () -> new HoeItem(OdysseyTiers.DIAMOND, 5, -2.0f, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> NETHERITE_HOE = ITEMS_VANILLA.register("netherite_hoe", () -> new HoeItem(OdysseyTiers.NETHERITE, 6, -2.0f, new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_TOOLS)));

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

    public static final RegistryObject<Item> BOW = ITEMS_VANILLA.register("bow", () -> new OdysseyBowItem((new Item.Properties()).durability(125).tab(CreativeModeTab.TAB_COMBAT), 1.0f, 20));
    public static final RegistryObject<Item> CROSSBOW = ITEMS_VANILLA.register("crossbow", () -> new OdysseyCrossbowItem((new Item.Properties()).durability(250).tab(CreativeModeTab.TAB_COMBAT), 1.25f, 25));
    public static final RegistryObject<Item> ARROW = ITEMS_VANILLA.register("arrow", () -> new OdysseyArrowItem((new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT), OdysseyArrow.ArrowType.FLINT));

    // Used to stack to 16, now stack to 64
    public static final RegistryObject<Item> ENDER_PEARL = ITEMS_VANILLA.register("ender_pearl", () -> new EnderpearlItem((new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> EGG = ITEMS_VANILLA.register("egg", () -> new EggItem((new Item.Properties()).tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> SNOWBALL = ITEMS_VANILLA.register("snowball", () -> new SnowballItem((new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> HONEY_BOTTLE = ITEMS_VANILLA.register("honey_bottle", () -> new HoneyBottleItem((new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).food(Foods.HONEY_BOTTLE).tab(CreativeModeTab.TAB_FOOD)));
    public static final RegistryObject<Item> OAK_SIGN = ITEMS_VANILLA.register("oak_sign", () -> new SignItem((new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS), Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN));
    public static final RegistryObject<Item> SPRUCE_SIGN = ITEMS_VANILLA.register("spruce_sign", () -> new SignItem((new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS), Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN));
    public static final RegistryObject<Item> BIRCH_SIGN = ITEMS_VANILLA.register("birch_sign", () -> new SignItem((new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS), Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN));
    public static final RegistryObject<Item> JUNGLE_SIGN = ITEMS_VANILLA.register("jungle_sign", () -> new SignItem((new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS), Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN));
    public static final RegistryObject<Item> ACACIA_SIGN = ITEMS_VANILLA.register("acacia_sign", () -> new SignItem((new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS), Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN));
    public static final RegistryObject<Item> DARK_OAK_SIGN = ITEMS_VANILLA.register("dark_oak_sign", () -> new SignItem((new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS), Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN));
    public static final RegistryObject<Item> CRIMSON_SIGN = ITEMS_VANILLA.register("crimson_sign", () -> new SignItem((new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS), Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN));
    public static final RegistryObject<Item> WARPED_SIGN = ITEMS_VANILLA.register("warped_sign", () -> new SignItem((new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS), Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN));
    public static final RegistryObject<Item> BUCKET = ITEMS_VANILLA.register("bucket", () -> new BucketItem(Fluids.EMPTY, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> WRITTEN_BOOK = ITEMS_VANILLA.register("written_book", () -> new WrittenBookItem((new Item.Properties())));
    public static final RegistryObject<Item> ARMOR_STAND = ITEMS_VANILLA.register("armor_stand", () -> new ArmorStandItem((new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> WHITE_BANNER = ITEMS_VANILLA.register("white_banner", () -> new BannerItem(Blocks.WHITE_BANNER, Blocks.WHITE_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> ORANGE_BANNER = ITEMS_VANILLA.register("orange_banner", () -> new BannerItem(Blocks.ORANGE_BANNER, Blocks.ORANGE_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> MAGENTA_BANNER = ITEMS_VANILLA.register("magenta_banner", () -> new BannerItem(Blocks.MAGENTA_BANNER, Blocks.MAGENTA_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> LIGHT_BLUE_BANNER = ITEMS_VANILLA.register("light_blue_banner", () -> new BannerItem(Blocks.LIGHT_BLUE_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> YELLOW_BANNER = ITEMS_VANILLA.register("yellow_banner", () -> new BannerItem(Blocks.YELLOW_BANNER, Blocks.YELLOW_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> LIME_BANNER = ITEMS_VANILLA.register("lime_banner", () -> new BannerItem(Blocks.LIME_BANNER, Blocks.LIME_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> PINK_BANNER = ITEMS_VANILLA.register("pink_banner", () -> new BannerItem(Blocks.PINK_BANNER, Blocks.PINK_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GRAY_BANNER = ITEMS_VANILLA.register("gray_banner", () -> new BannerItem(Blocks.GRAY_BANNER, Blocks.GRAY_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> LIGHT_GRAY_BANNER = ITEMS_VANILLA.register("light_gray_banner", () -> new BannerItem(Blocks.LIGHT_GRAY_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> CYAN_BANNER = ITEMS_VANILLA.register("cyan_banner", () -> new BannerItem(Blocks.CYAN_BANNER, Blocks.CYAN_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> PURPLE_BANNER = ITEMS_VANILLA.register("purple_banner", () -> new BannerItem(Blocks.PURPLE_BANNER, Blocks.PURPLE_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BLUE_BANNER = ITEMS_VANILLA.register("blue_banner", () -> new BannerItem(Blocks.BLUE_BANNER, Blocks.BLUE_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BROWN_BANNER = ITEMS_VANILLA.register("brown_banner", () -> new BannerItem(Blocks.BROWN_BANNER, Blocks.BROWN_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> GREEN_BANNER = ITEMS_VANILLA.register("green_banner", () -> new BannerItem(Blocks.GREEN_BANNER, Blocks.GREEN_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> RED_BANNER = ITEMS_VANILLA.register("red_banner", () -> new BannerItem(Blocks.RED_BANNER, Blocks.RED_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BLACK_BANNER = ITEMS_VANILLA.register("black_banner", () -> new BannerItem(Blocks.BLACK_BANNER, Blocks.BLACK_WALL_BANNER, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
}