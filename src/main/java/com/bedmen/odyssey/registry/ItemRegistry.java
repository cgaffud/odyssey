package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipDisplaySetting;
import com.bedmen.odyssey.combat.*;
import com.bedmen.odyssey.entity.projectile.SonicBoom;
import com.bedmen.odyssey.entity.vehicle.OdysseyBoat;
import com.bedmen.odyssey.items.*;
import com.bedmen.odyssey.items.aspect_items.*;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyBoatItem;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyCreativeModeTab;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyFood;
import com.bedmen.odyssey.items.odyssey_versions.OdysseySpawnEggItem;
import com.bedmen.odyssey.lock.LockableDoorType;
import com.bedmen.odyssey.lock.TreasureChestType;
import com.bedmen.odyssey.tier.OdysseyTiers;
import com.bedmen.odyssey.util.BiomeUtil;
import com.bedmen.odyssey.util.OdysseyRarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ItemRegistry {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Odyssey.MOD_ID);
    public static DeferredRegister<Item> ITEMS_VANILLA = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // # Building Blocks
    public static final RegistryObject<Item> LEATHER_BUNDLE = ITEMS.register("leather_bundle", () -> new BlockItem(BlockRegistry.LEATHER_BUNDLE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> COPPER_CHEST = ITEMS.register("copper_chest", () -> new BEWLRBlockItem(BlockRegistry.COPPER_CHEST.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> RECYCLING_FURNACE = ITEMS.register("recycling_furnace", () -> new BlockItem(BlockRegistry.RECYCLING_FURNACE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> COPPER_COBWEB = ITEMS.register("copper_cobweb", () -> new BlockItem(BlockRegistry.COPPER_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> IRON_COBWEB = ITEMS.register("iron_cobweb", () -> new BlockItem(BlockRegistry.IRON_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STITICHING_TABLE = ITEMS.register("stitching_table", () -> new BlockItem(BlockRegistry.STITCHING_TABLE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_ORE = ITEMS.register("silver_ore", () -> new BlockItem(BlockRegistry.SILVER_ORE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> DEEPSLATE_SILVER_ORE = ITEMS.register("deepslate_silver_ore", () -> new BlockItem(BlockRegistry.DEEPSLATE_SILVER_ORE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> RAW_SILVER_BLOCK = ITEMS.register("raw_silver_block", () -> new BlockItem(BlockRegistry.RAW_SILVER_BLOCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_COBWEB = ITEMS.register("silver_cobweb", () -> new BlockItem(BlockRegistry.SILVER_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> SILVER_BLOCK = ITEMS.register("silver_block", () -> new BlockItem(BlockRegistry.SILVER_BLOCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> GOLDEN_COBWEB = ITEMS.register("golden_cobweb", () -> new BlockItem(BlockRegistry.GOLDEN_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> ALLOY_FURNACE = ITEMS.register("alloy_furnace", () -> new BlockItem(BlockRegistry.ALLOY_FURNACE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> RUBY_ORE = ITEMS.register("ruby_ore", () -> new BlockItem(BlockRegistry.RUBY_ORE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> DEEPSLATE_RUBY_ORE = ITEMS.register("deepslate_ruby_ore", () -> new BlockItem(BlockRegistry.DEEPSLATE_RUBY_ORE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STERLING_SILVER_COBWEB = ITEMS.register("sterling_silver_cobweb", () -> new BlockItem(BlockRegistry.STERLING_SILVER_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STERLING_SILVER_CHEST = ITEMS.register("sterling_silver_chest", () -> new BEWLRBlockItem(BlockRegistry.STERLING_SILVER_CHEST.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> STERLING_SILVER_BLOCK = ITEMS.register("sterling_silver_block", () -> new BlockItem(BlockRegistry.STERLING_SILVER_BLOCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> INFUSION_PEDESTAL = ITEMS.register("infusion_pedestal", () -> new BlockItem(BlockRegistry.INFUSION_PEDESTAL.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> INFUSER = ITEMS.register("infuser", () -> new BlockItem(BlockRegistry.INFUSER.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> ELECTRUM_COBWEB = ITEMS.register("electrum_cobweb", () -> new BlockItem(BlockRegistry.ELECTRUM_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> ELECTRUM_BLOCK = ITEMS.register("electrum_block", () -> new BlockItem(BlockRegistry.ELECTRUM_BLOCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> DIAMOND_COBWEB = ITEMS.register("diamond_cobweb", () -> new BlockItem(BlockRegistry.DIAMOND_COBWEB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> CLOVER_STONE = ITEMS.register("clover_stone", () -> new BlockItem(BlockRegistry.CLOVER_STONE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MOONROCK = ITEMS.register("moonrock", () -> new BlockItem(BlockRegistry.MOONROCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> POLISHED_MOONROCK = ITEMS.register("polished_moonrock", () -> new BlockItem(BlockRegistry.POLISHED_MOONROCK.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MOONROCK_STAIRS = ITEMS.register("moonrock_stairs", () -> new BlockItem(BlockRegistry.MOONROCK_STAIRS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> POLISHED_MOONROCK_STAIRS = ITEMS.register("polished_moonrock_stairs", () -> new BlockItem(BlockRegistry.POLISHED_MOONROCK_STAIRS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MOONROCK_SLAB = ITEMS.register("moonrock_slab", () -> new BlockItem(BlockRegistry.MOONROCK_SLAB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> POLISHED_MOONROCK_SLAB = ITEMS.register("polished_moonrock_slab", () -> new BlockItem(BlockRegistry.POLISHED_MOONROCK_SLAB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> MOONROCK_WALL = ITEMS.register("moonrock_wall", () -> new BlockItem(BlockRegistry.MOONROCK_WALL.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> TRANSCENDENTAL_OBSIDIAN = ITEMS.register("transcendental_obsidian", () -> new BlockItem(BlockRegistry.TRANSCENDENTAL_OBSIDIAN.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> TRANSCENDENTAL_TRANSMUTER = ITEMS.register("transcendental_transmuter", () -> new BlockItem(BlockRegistry.TRANSCENDENTAL_TRANSMUTER.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));
    public static final RegistryObject<Item> ARCANE_GRINDSTONE = ITEMS.register("arcane_grindstone", () -> new BlockItem(BlockRegistry.ARCANE_GRINDSTONE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.BUILDING_BLOCKS)));

    // # Wood and Plants
    public static final RegistryObject<Item> PRAIRIE_GRASS = ITEMS.register("prairie_grass", () -> new TripleHighBlockItem(BlockRegistry.PRAIRIE_GRASS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));

    public static final RegistryObject<Item> PALM_PLANKS = ITEMS.register("palm_planks", () -> new BlockItem(BlockRegistry.PALM_PLANKS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_SAPLING = ITEMS.register("palm_sapling", () -> new BlockItem(BlockRegistry.PALM_SAPLING.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_LOG = ITEMS.register("palm_log", () -> new BlockItem(BlockRegistry.PALM_LOG.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> STRIPPED_PALM_LOG = ITEMS.register("stripped_palm_log", () -> new BlockItem(BlockRegistry.STRIPPED_PALM_LOG.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> STRIPPED_PALM_WOOD = ITEMS.register("stripped_palm_wood", () -> new BlockItem(BlockRegistry.STRIPPED_PALM_WOOD.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_WOOD = ITEMS.register("palm_wood", () -> new BlockItem(BlockRegistry.PALM_WOOD.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_LEAVES = ITEMS.register("palm_leaves", () -> new BlockItem(BlockRegistry.PALM_LEAVES.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_CORNER_LEAVES = ITEMS.register("palm_corner_leaves", () -> new BlockItem(BlockRegistry.PALM_CORNER_LEAVES.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_SLAB = ITEMS.register("palm_slab", () -> new BlockItem(BlockRegistry.PALM_SLAB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_FENCE = ITEMS.register("palm_fence", () -> new BurnableFenceItem(BlockRegistry.PALM_FENCE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_STAIRS = ITEMS.register("palm_stairs", () -> new BlockItem(BlockRegistry.PALM_STAIRS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_BUTTON = ITEMS.register("palm_button", () -> new BlockItem(BlockRegistry.PALM_BUTTON.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_PRESSURE_PLATE = ITEMS.register("palm_pressure_plate", () -> new BlockItem(BlockRegistry.PALM_PRESSURE_PLATE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_DOOR = ITEMS.register("palm_door", () -> new DoubleHighBlockItem(BlockRegistry.PALM_DOOR.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_TRAPDOOR = ITEMS.register("palm_trapdoor", () -> new BlockItem(BlockRegistry.PALM_TRAPDOOR.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_FENCE_GATE = ITEMS.register("palm_fence_gate", () -> new BurnableFenceItem(BlockRegistry.PALM_FENCE_GATE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_BOAT = ITEMS.register("palm_boat", () -> new OdysseyBoatItem(OdysseyBoat.Type.PALM, (new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> PALM_SIGN = ITEMS.register("palm_sign", () -> new SignItem((new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD), BlockRegistry.PALM_SIGN.get(), BlockRegistry.PALM_WALL_SIGN.get()));

    public static final RegistryObject<Item> COVEN_DOOR = ITEMS.register("coven_door", () -> new DoubleHighBlockItem(BlockRegistry.COVEN_DOOR.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> COVEN_ROOTS = ITEMS.register("coven_roots", () -> new BlockItem(BlockRegistry.COVEN_ROOTS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));

    public static final RegistryObject<Item> GREATWOOD_PLANKS = ITEMS.register("greatwood_planks", () -> new BlockItem(BlockRegistry.GREATWOOD_PLANKS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_SAPLING = ITEMS.register("greatwood_sapling", () -> new BlockItem(BlockRegistry.GREATWOOD_SAPLING.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_LOG = ITEMS.register("greatwood_log", () -> new BlockItem(BlockRegistry.GREATWOOD_LOG.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> STRIPPED_GREATWOOD_LOG = ITEMS.register("stripped_greatwood_log", () -> new BlockItem(BlockRegistry.STRIPPED_GREATWOOD_LOG.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> STRIPPED_GREATWOOD_WOOD = ITEMS.register("stripped_greatwood_wood", () -> new BlockItem(BlockRegistry.STRIPPED_GREATWOOD_WOOD.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_WOOD = ITEMS.register("greatwood_wood", () -> new BlockItem(BlockRegistry.GREATWOOD_WOOD.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_LEAVES = ITEMS.register("greatwood_leaves", () -> new BlockItem(BlockRegistry.GREATWOOD_LEAVES.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATROOTS = ITEMS.register("greatroots", () -> new BlockItem(BlockRegistry.GREATROOTS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_SLAB = ITEMS.register("greatwood_slab", () -> new BlockItem(BlockRegistry.GREATWOOD_SLAB.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_FENCE = ITEMS.register("greatwood_fence", () -> new BurnableFenceItem(BlockRegistry.GREATWOOD_FENCE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_STAIRS = ITEMS.register("greatwood_stairs", () -> new BlockItem(BlockRegistry.GREATWOOD_STAIRS.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_BUTTON = ITEMS.register("greatwood_button", () -> new BlockItem(BlockRegistry.GREATWOOD_BUTTON.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_PRESSURE_PLATE = ITEMS.register("greatwood_pressure_plate", () -> new BlockItem(BlockRegistry.GREATWOOD_PRESSURE_PLATE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_DOOR = ITEMS.register("greatwood_door", () -> new DoubleHighBlockItem(BlockRegistry.GREATWOOD_DOOR.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_TRAPDOOR = ITEMS.register("greatwood_trapdoor", () -> new BlockItem(BlockRegistry.GREATWOOD_TRAPDOOR.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_FENCE_GATE = ITEMS.register("greatwood_fence_gate", () -> new BurnableFenceItem(BlockRegistry.GREATWOOD_FENCE_GATE.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_BOAT = ITEMS.register("greatwood_boat", () -> new OdysseyBoatItem(OdysseyBoat.Type.GREATWOOD, (new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.WOOD)));
    public static final RegistryObject<Item> GREATWOOD_SIGN = ITEMS.register("greatwood_sign", () -> new SignItem((new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD), BlockRegistry.GREATWOOD_SIGN.get(), BlockRegistry.GREATWOOD_WALL_SIGN.get()));
    public static final RegistryObject<Item> GREATROOT = ITEMS.register("greatroot", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.WOOD)));

    // # Materials
    public static final RegistryObject<Item> COCONUT_FLOWER = ITEMS.register("coconut_flower", () -> new ItemNameBlockItem(BlockRegistry.COCONUT.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> FEATHER_BUNDLE = ITEMS.register("feather_bundle", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> POLAR_BEAR_FUR = ITEMS.register("polar_bear_fur", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> COPPER_FIBER = ITEMS.register("copper_fiber", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> SPIDER_FANG = ITEMS.register("spider_fang", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> WEAVER_FANG = ITEMS.register("weaver_fang", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> CHAINMAIL = ITEMS.register("chainmail", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> IRON_FIBER = ITEMS.register("iron_fiber", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> RUSTY_ARM = ITEMS.register("rusty_arm", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
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
    public static final RegistryObject<Item> ECTOPLASM = ITEMS.register("ectoplasm", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> AURIC_DUST = ITEMS.register("auric_dust", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> DIAMOND_FIBER = ITEMS.register("diamond_fiber", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> PEARL = ITEMS.register("pearl", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> PERMAFROST_SHARD = ITEMS.register("permafrost_shard", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> NETHERITE_NUGGET = ITEMS.register("netherite_nugget", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> SUNSTONE = ITEMS.register("sunstone", ()-> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> MOONSTONE = ITEMS.register("moonstone", ()-> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    public static final RegistryObject<Item> STRAW_HEXDOLL = ITEMS.register("straw_hexdoll", () -> new BurnToSummonItem((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS), EntityTypeRegistry.COVEN_MASTER::get));
    public static final RegistryObject<Item> AERIAL_FABRIC = ITEMS.register("aerial_fabric", ()-> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.MATERIALS)));
    // # Food
    public static final RegistryObject<Item> COCONUT_COOKIE = ITEMS.register("coconut_cookie", () -> new Item((new Item.Properties()).tab(OdysseyCreativeModeTab.FOOD).food(OdysseyFood.COCONUT_COOKIE)));
    public static final RegistryObject<Item> MINERAL_FRUIT = ITEMS.register("mineral_fruit", () -> new PermabuffFoodItem((new Item.Properties()).tab(OdysseyCreativeModeTab.FOOD), List.of(new AspectInstance(Aspects.ADDITIONAL_MOB_HARVEST_LEVEL, 1)), player -> AspectUtil.getPermabuffAspectStrength(player, Aspects.ADDITIONAL_MOB_HARVEST_LEVEL) < 1));

    // # Tools
    public static final RegistryObject<Item> COPPER_KEY = ITEMS.register("copper_key", () -> new KeyItem((new Item.Properties()).tab(OdysseyCreativeModeTab.TOOLS), TreasureChestType.COPPER));

    public static final RegistryObject<Item> CLOVER_STONE_SHOVEL = ITEMS.register("clover_stone_shovel", () -> new AspectShovelItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.SHOVEL, 4.5f, List.of(), List.of(new AspectInstance(Aspects.FORTUNE, 1))));
    public static final RegistryObject<Item> CLOVER_STONE_PICKAXE = ITEMS.register("clover_stone_pickaxe", () -> new AspectPickaxeItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.PICKAXE, 5.0f, List.of(), List.of(new AspectInstance(Aspects.FORTUNE, 1))));
    public static final RegistryObject<Item> CLOVER_STONE_AXE = ITEMS.register("clover_stone_axe", () -> new AspectAxeItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.AXE, 6.0f, List.of(), List.of(new AspectInstance(Aspects.FORTUNE, 1))));
    public static final RegistryObject<Item> CLOVER_STONE_HOE = ITEMS.register("clover_stone_hoe", () -> new AspectHoeItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.HOE, 3.0f, List.of(), List.of(new AspectInstance(Aspects.FORTUNE, 1))));

    public static final RegistryObject<Item> STERLING_SILVER_SHOVEL = ITEMS.register("sterling_silver_shovel", () -> new AspectShovelItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.STERLING_SILVER, MeleeWeaponClass.SHOVEL, 4.5f, List.of(), List.of()));
    public static final RegistryObject<Item> STERLING_SILVER_PICKAXE = ITEMS.register("sterling_silver_pickaxe", () -> new AspectPickaxeItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.STERLING_SILVER, MeleeWeaponClass.PICKAXE, 5.0f, List.of(), List.of()));
    public static final RegistryObject<Item> STERLING_SILVER_AXE = ITEMS.register("sterling_silver_axe", () -> new AspectAxeItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.STERLING_SILVER, MeleeWeaponClass.AXE, 6.0f, List.of(), List.of()));
    public static final RegistryObject<Item> STERLING_SILVER_HOE = ITEMS.register("sterling_silver_hoe", () -> new AspectHoeItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.STERLING_SILVER, MeleeWeaponClass.HOE, 4.0f, List.of(), List.of()));
    public static final RegistryObject<Item> STERLING_SILVER_KEY = ITEMS.register("sterling_silver_key", () -> new KeyItem((new Item.Properties()).tab(OdysseyCreativeModeTab.TOOLS), TreasureChestType.STERLING_SILVER));

    public static final RegistryObject<Item> COVEN_HUT_KEY = ITEMS.register("coven_hut_key", () -> new KeyItem((new Item.Properties()).tab(OdysseyCreativeModeTab.TOOLS), LockableDoorType.COVEN_HUT));

    public static final List<AspectInstance> MARINE_TOOL_INNATE_MODIFIER_LIST = List.of(new AspectInstance(Aspects.AQUA_AFFINITY));
    public static final RegistryObject<Item> MARINE_SHOVEL = ITEMS.register("marine_shovel", () -> new AspectShovelItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.MARINE, MeleeWeaponClass.SHOVEL.withBetterAttackSpeed(), 3.5f, List.of(), MARINE_TOOL_INNATE_MODIFIER_LIST));
    public static final RegistryObject<Item> MARINE_PICKAXE = ITEMS.register("marine_pickaxe", () -> new AspectPickaxeItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.MARINE, MeleeWeaponClass.PICKAXE.withBetterAttackSpeed(), 4.0f, List.of(), MARINE_TOOL_INNATE_MODIFIER_LIST));
    public static final RegistryObject<Item> MARINE_AXE = ITEMS.register("marine_axe", () -> new AspectAxeItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.MARINE, MeleeWeaponClass.AXE.withBetterAttackSpeed(), 5.0f, List.of(), MARINE_TOOL_INNATE_MODIFIER_LIST));
    public static final RegistryObject<Item> MARINE_HOE = ITEMS.register("marine_hoe", () -> new AspectHoeItem(new Item.Properties().tab(OdysseyCreativeModeTab.TOOLS), OdysseyTiers.MARINE, MeleeWeaponClass.HOE.withBetterAttackSpeed(), 2.0f, List.of(), MARINE_TOOL_INNATE_MODIFIER_LIST));

    // # Magic
    public static final RegistryObject<Item> WARP_TOTEM = ITEMS.register("warp_totem", () -> new WarpTotemItem((new Item.Properties()).tab(OdysseyCreativeModeTab.MAGIC).stacksTo(1), false));
    public static final RegistryObject<Item> CRACKED_WARP_TOTEM = ITEMS.register("cracked_warp_totem", () -> new WarpTotemItem((new Item.Properties()).tab(OdysseyCreativeModeTab.MAGIC).durability(5), true));
    public static final RegistryObject<Item> PURIFICATION_TABLET = ITEMS.register("purification_tablet", () -> new PurificationTabletItem((new Item.Properties()).tab(OdysseyCreativeModeTab.MAGIC)));

    // # Miscellaneous
    public static final RegistryObject<Item> GREATWOOD_MINERAL_WATER_BOTTLE = ITEMS.register("greatwood_mineral_water_bottle", () -> new GreatSaplingHelperItem((new Item.Properties()).tab(OdysseyCreativeModeTab.MISC), BlockRegistry.GREATWOOD_SAPLING::get, true));
    public static final RegistryObject<Item> GREATWOOD_FERTILIZER = ITEMS.register("greatwood_fertilizer", () -> new GreatSaplingHelperItem((new Item.Properties()).tab(OdysseyCreativeModeTab.MISC), BlockRegistry.GREATWOOD_SAPLING::get, false));
    public static final RegistryObject<Item> MUSIC_DISC_MESA = ITEMS.register("music_disc_mesa", () -> new RecordItem(14, SoundEventRegistry.MUSIC_DISC_MESA, (new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.MISC).rarity(Rarity.RARE)));


    // # Melee Weapons

    // ## Tier 1
    public static final RegistryObject<Item> WOODEN_MACE = ITEMS.register("wooden_mace", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.WOOD, MeleeWeaponClass.MACE, 5f, List.of(), List.of()));
    public static final RegistryObject<Item> STONE_MACE = ITEMS.register("stone_mace", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.STONE, MeleeWeaponClass.MACE, 6f, List.of(), List.of()));

    public static final RegistryObject<Item> COPPER_HAMMER = ITEMS.register("copper_hammer", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.COPPER, MeleeWeaponClass.HAMMER, 7f, List.of(), List.of(new AspectInstance(Aspects.DAMAGE_ON_ARTHROPOD, 0.75f))));
    public static final RegistryObject<Item> COPPER_BATTLE_AXE = ITEMS.register("copper_battle_axe", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.COPPER, MeleeWeaponClass.BATTLE_AXE, 6.5f, List.of(), List.of(new AspectInstance(Aspects.FATAL_HIT, 7.5f))));
    public static final RegistryObject<Item> COPPER_BAT = ITEMS.register("copper_bat", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.COPPER, MeleeWeaponClass.BAT, 5.5f, List.of(), List.of(new AspectInstance(Aspects.KNOCKBACK, 0.5f))));
    public static final RegistryObject<Item> FLINT_SABRE = ITEMS.register("flint_sabre", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.FLINT, MeleeWeaponClass.SABRE, 5f, List.of(), List.of(new AspectInstance(Aspects.ADDITIONAL_SWEEP_DAMAGE, 1.0f))));
    public static final RegistryObject<Item> FLINT_HATCHET = ITEMS.register("flint_hatchet", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.FLINT, MeleeWeaponClass.HATCHET, 4.5f, List.of(), List.of(new AspectInstance(Aspects.SMITE_DAMAGE, 0.75f))));
    public static final RegistryObject<Item> FLINT_SPEAR = ITEMS.register("flint_spear", () -> new SpearItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.FLINT, MeleeWeaponClass.SPEAR, ThrowableTypes.FLINT_SPEAR));

    public static final RegistryObject<Item> SPIDER_FANG_DAGGER = ITEMS.register("spider_fang_dagger", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.SPIDER_FANG, MeleeWeaponClass.DAGGER, 5f, List.of(), List.of(new AspectInstance(Aspects.POISON_DAMAGE, 2))));
    public static final RegistryObject<Item> WEAVER_FANG_DAGGER = ITEMS.register("weaver_fang_dagger", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.WEAVER_FANG, MeleeWeaponClass.DAGGER, 6f, List.of(), List.of(new AspectInstance(Aspects.COBWEB_CHANCE, 0.2f))));

    public static final RegistryObject<Item> IRON_MACE = ITEMS.register("iron_mace", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.IRON, MeleeWeaponClass.MACE, 7.5f, List.of(), List.of()));
    public static final RegistryObject<Item> GOLDEN_MACE = ITEMS.register("golden_mace", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.GOLD, MeleeWeaponClass.MACE.withAttackSpeedMultiplier(1.5f), 6f, List.of(), List.of()));

    public static final RegistryObject<Item> RUSTY_PADDLE = ITEMS.register("rusty_paddle", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.RUSTY_IRON, MeleeWeaponClass.PADDLE, 6f, List.of(), List.of()));
    public static final RegistryObject<Item> BATTLE_PICKAXE = ITEMS.register("battle_pickaxe", () -> new AspectPickaxeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE).rarity(OdysseyRarity.UNCRAFTABLE_EQUIPMENT), OdysseyTiers.UNCRAFTABLE_1, MeleeWeaponClass.BATTLE_AXE,8f, List.of(), List.of(new AspectInstance(Aspects.FATAL_HIT, 7.5f))));
    public static final RegistryObject<Item> SLIME_BAT = ITEMS.register("slime_bat", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE).rarity(OdysseyRarity.UNCRAFTABLE_EQUIPMENT), OdysseyTiers.UNCRAFTABLE_1, MeleeWeaponClass.BAT, 2f, List.of(), List.of(new AspectInstance(Aspects.KNOCKBACK, 2.0f))));
    public static final RegistryObject<Item> MINI_HAMMER = ITEMS.register("mini_hammer", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE).rarity(OdysseyRarity.UNCRAFTABLE_EQUIPMENT), OdysseyTiers.UNCRAFTABLE_1, MeleeWeaponClass.HATCHET, 5f, List.of(), List.of(new AspectInstance(Aspects.DAMAGE_ON_ARTHROPOD, 0.75f))));
    public static final RegistryObject<Item> SLEDGEHAMMER = ITEMS.register("sledgehammer", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE).rarity(OdysseyRarity.UNCRAFTABLE_EQUIPMENT), OdysseyTiers.UNCRAFTABLE_1, MeleeWeaponClass.HAMMER, 8f, List.of(), List.of(new AspectInstance(Aspects.SMITE_DAMAGE, 0.75f))));
    public static final RegistryObject<Item> SWIFT_SABRE = ITEMS.register("swift_sabre", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE).rarity(OdysseyRarity.UNCRAFTABLE_EQUIPMENT), OdysseyTiers.UNCRAFTABLE_1, MeleeWeaponClass.SABRE, 6.0f, List.of(new AspectInstance(Aspects.MOVEMENT_SPEED, 0.2f)), List.of(new AspectInstance(Aspects.ADDITIONAL_SWEEP_DAMAGE, 1.0f))));
    public static final RegistryObject<Item> SUN_SWORD = ITEMS.register("sun_sword", () -> new ConditionalAmpMeleeItem.NumericalItem(new Item.Properties().rarity(OdysseyRarity.ULTRA_EQUIPMENT).tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.ULTRA_1, MeleeWeaponClass.SWORD,  6f, List.of(), List.of(new AspectInstance(Aspects.SOLAR_STRENGTH, 0.5f)), 2));

    // ## Tier 2
    public static final RegistryObject<Item> BANDIT_DAGGER = ITEMS.register("bandit_dagger", () -> new AspectMeleeItem(new Item.Properties().rarity(OdysseyRarity.UNCRAFTABLE_EQUIPMENT).tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.UNCRAFTABLE_2, MeleeWeaponClass.DAGGER, 6.5f, List.of(), List.of(new AspectInstance(Aspects.LARCENY_CHANCE, 0.1f))));

    public static final RegistryObject<Item> CLOVER_STONE_SWORD = ITEMS.register("clover_stone_sword", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.SWORD, 7f, List.of(), List.of(new AspectInstance(Aspects.LOOTING_LUCK, 1))));
    public static final RegistryObject<Item> CLOVER_STONE_MACE = ITEMS.register("clover_stone_mace", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.STERLING_SILVER, MeleeWeaponClass.MACE, 9f, List.of(), List.of(new AspectInstance(Aspects.LOOTING_LUCK, 1))));
    public static final RegistryObject<Item> CLOVER_STONE_HAMMER = ITEMS.register("clover_stone_hammer", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.HAMMER, 8f, List.of(), List.of(new AspectInstance(Aspects.LOOTING_LUCK, 1), new AspectInstance(Aspects.DAMAGE_ON_ARTHROPOD, 0.75f))));
    public static final RegistryObject<Item> CLOVER_STONE_BATTLE_AXE = ITEMS.register("clover_stone_battle_axe", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.BATTLE_AXE, 7.5f, List.of(), List.of(new AspectInstance(Aspects.LOOTING_LUCK, 1), new AspectInstance(Aspects.FATAL_HIT, 7.5f))));
    public static final RegistryObject<Item> CLOVER_STONE_BAT = ITEMS.register("clover_stone_bat", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.BAT, 6.5f, List.of(), List.of(new AspectInstance(Aspects.LOOTING_LUCK, 1), new AspectInstance(Aspects.KNOCKBACK, 0.5f))));
    public static final RegistryObject<Item> CLOVER_STONE_SABRE = ITEMS.register("clover_stone_sabre", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.SABRE, 6f, List.of(), List.of(new AspectInstance(Aspects.LOOTING_LUCK, 1), new AspectInstance(Aspects.ADDITIONAL_SWEEP_DAMAGE, 1.0f))));
    public static final RegistryObject<Item> CLOVER_STONE_HATCHET = ITEMS.register("clover_stone_hatchet", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.HATCHET, 5f, List.of(), List.of(new AspectInstance(Aspects.LOOTING_LUCK, 1), new AspectInstance(Aspects.SMITE_DAMAGE, 0.75f))));
    public static final RegistryObject<Item> CLOVER_STONE_SPEAR = ITEMS.register("clover_stone_spear", () -> new SpearItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.CLOVER_STONE, MeleeWeaponClass.SPEAR, ThrowableTypes.CLOVER_STONE_SPEAR));
    
    public static final RegistryObject<Item> STERLING_SILVER_SWORD = ITEMS.register("sterling_silver_sword", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.STERLING_SILVER, MeleeWeaponClass.SWORD, 7f, List.of(), List.of()));
    public static final RegistryObject<Item> STERLING_SILVER_MACE = ITEMS.register("sterling_silver_mace", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.STERLING_SILVER, MeleeWeaponClass.MACE, 9f, List.of(), List.of()));

    public static final RegistryObject<Item> OBSIDIAN_HAMMER = ITEMS.register("obsidian_hammer", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.OBSIDIAN, MeleeWeaponClass.HAMMER, 9f, List.of(), List.of(new AspectInstance(Aspects.DAMAGE_ON_ARTHROPOD, 1.0f))));
    public static final RegistryObject<Item> OBSIDIAN_BATTLE_AXE = ITEMS.register("obsidian_battle_axe", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.OBSIDIAN, MeleeWeaponClass.BATTLE_AXE, 8.5f, List.of(), List.of(new AspectInstance(Aspects.FATAL_HIT, 10.0f))));
    public static final RegistryObject<Item> OBSIDIAN_BAT = ITEMS.register("obsidian_bat", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.OBSIDIAN, MeleeWeaponClass.BAT, 7.5f, List.of(), List.of(new AspectInstance(Aspects.KNOCKBACK, 1.0f))));
    public static final RegistryObject<Item> AMETHYST_SABRE = ITEMS.register("amethyst_sabre", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.AMETHYST, MeleeWeaponClass.SABRE, 7f, List.of(), List.of(new AspectInstance(Aspects.ADDITIONAL_SWEEP_DAMAGE, 2.0f))));
    public static final RegistryObject<Item> AMETHYST_HATCHET = ITEMS.register("amethyst_hatchet", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.AMETHYST, MeleeWeaponClass.HATCHET, 6f, List.of(), List.of(new AspectInstance(Aspects.SMITE_DAMAGE, 1.0f))));
    public static final RegistryObject<Item> AMETHYST_SPEAR = ITEMS.register("amethyst_spear", () -> new SpearItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.AMETHYST, MeleeWeaponClass.SPEAR, ThrowableTypes.AMETHYST_SPEAR));

    public static final RegistryObject<Item> DIAMOND_MACE = ITEMS.register("diamond_mace", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.DIAMOND, MeleeWeaponClass.MACE, 10f, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));

    public static final RegistryObject<Item> SMACKIN_SHOVEL = ITEMS.register("smackin_shovel", () -> new AspectShovelItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE).rarity(OdysseyRarity.UNCRAFTABLE_EQUIPMENT), OdysseyTiers.UNCRAFTABLE_2, MeleeWeaponClass.BAT, 7f, List.of(new AspectInstance(Aspects.SMACK)), List.of(new AspectInstance(Aspects.KNOCKBACK, 2.0f))));
    public static final RegistryObject<Item> SLEDGEAXE = ITEMS.register("sledgeaxe", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE).rarity(OdysseyRarity.UNCRAFTABLE_EQUIPMENT), OdysseyTiers.UNCRAFTABLE_2, MeleeWeaponClass.BATTLE_AXE,  9.5f, List.of(), List.of(new AspectInstance(Aspects.FATAL_HIT, 10.0f), new AspectInstance(Aspects.SMITE_DAMAGE, 1.0f))));
    public static final RegistryObject<Item> BLUNT_SABRE = ITEMS.register("blunt_sabre", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE).rarity(OdysseyRarity.UNCRAFTABLE_EQUIPMENT), OdysseyTiers.UNCRAFTABLE_2, MeleeWeaponClass.SABRE, 8f, List.of(), List.of(new AspectInstance(Aspects.ADDITIONAL_SWEEP_DAMAGE, 1.5f), new AspectInstance(Aspects.DAMAGE_ON_ARTHROPOD, 1.0f))));
    public static final RegistryObject<Item> MOON_SWORD = ITEMS.register("moon_sword", () -> new ConditionalAmpMeleeItem.NumericalItem(new Item.Properties().rarity(OdysseyRarity.ULTRA_EQUIPMENT).tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.ULTRA_2, MeleeWeaponClass.SWORD, 7f, List.of(), List.of(new AspectInstance(Aspects.LUNAR_STRENGTH, 1.0f)), 2));
    public static final RegistryObject<Item> RAIN_SWORD = ITEMS.register("rain_sword", () -> new ConditionalAmpMeleeItem.GradientItem(new Item.Properties().rarity(OdysseyRarity.ULTRA_EQUIPMENT).tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.ULTRA_2, MeleeWeaponClass.SWORD, 7f, List.of(), List.of(new AspectInstance(Aspects.BOTANICAL_STRENGTH, 1.0f)), BiomeUtil::getFoliageColor, 9551193));
    public static final RegistryObject<Item> ARID_MACE = ITEMS.register("arid_mace", () -> new ConditionalAmpMeleeItem.GradientItem(new Item.Properties().rarity(OdysseyRarity.ULTRA_EQUIPMENT).tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.ULTRA_2, MeleeWeaponClass.MACE, 9f, List.of(), List.of(new AspectInstance(Aspects.SCORCHED_STRENGTH, 1.0f)), BiomeUtil::getAridColor, 0xFF8B33));
    public static final RegistryObject<Item> ICE_DAGGER = ITEMS.register("ice_dagger", () -> new ConditionalAmpMeleeItem.GradientItem(new Item.Properties().rarity(OdysseyRarity.ULTRA_EQUIPMENT).tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.ULTRA_2, MeleeWeaponClass.DAGGER, 6f, List.of(), List.of(new AspectInstance(Aspects.WINTERY_STRENGTH, 1.0f)), BiomeUtil::getColdColor, 0x66A6FF));
    public static final RegistryObject<Item> VOID_SWORD = ITEMS.register("void_sword", () -> new ConditionalAmpMeleeItem.NumericalItem(new Item.Properties().rarity(OdysseyRarity.ULTRA_EQUIPMENT).tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.ULTRA_2, MeleeWeaponClass.SWORD, 7f, List.of(), List.of(new AspectInstance(Aspects.VOID_STRENGTH, 1.0f)), 9));
    public static final RegistryObject<Item> BROKEN_SEA_SWORD = ITEMS.register("broken_sea_sword", () -> new AspectMeleeItem(new Item.Properties().rarity(OdysseyRarity.ULTRA_EQUIPMENT).tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.ULTRA_1, MeleeWeaponClass.DAGGER, 7f, List.of(), List.of(new AspectInstance(Aspects.HYDRO_DAMAGE, 3.0f))));

    // ## Tier 3
    public static final RegistryObject<Item> NETHERITE_MACE = ITEMS.register("netherite_mace", () -> new AspectMeleeItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.NETHERITE, MeleeWeaponClass.MACE, 11f, List.of(), List.of(new AspectInstance(Aspects.BURN_PROOF))));
    public static final RegistryObject<Item> SKY_SWORD = ITEMS.register("sky_sword", () -> new ConditionalAmpMeleeItem.NumericalItem(new Item.Properties().rarity(OdysseyRarity.ULTRA_EQUIPMENT).tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.ULTRA_3, MeleeWeaponClass.SWORD,  9f, List.of(), List.of(new AspectInstance(Aspects.SKY_STRENGTH, 1.5f)), 2));

    // # Ranged Weapons

    // ## Tier 1
    public static final RegistryObject<Item> WOODEN_BOOMERANG = ITEMS.register("wooden_boomerang", () -> new BoomerangItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.WOOD, ThrowableTypes.WOODEN_BOOMERANG));

    public static final RegistryObject<Item> BONE_BOOMERANG = ITEMS.register("bone_boomerang", () -> new BoomerangItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.BONE, ThrowableTypes.BONE_BOOMERANG));
    public static final RegistryObject<Item> COPPER_DISC = ITEMS.register("copper_disc", () -> new BoomerangItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.COPPER, ThrowableTypes.COPPER_DISC));
    public static final RegistryObject<Item> FLINT_DISC = ITEMS.register("flint_disc", () -> new BoomerangItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.FLINT, ThrowableTypes.FLINT_DISC));
    public static final RegistryObject<Item> BONERANG = ITEMS.register("bonerang", () -> new BoomerangItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.BONE, ThrowableTypes.BONERANG));
    public static final RegistryObject<Item> BONE_LONG_BOW = ITEMS.register("bone_long_bow", () -> new AspectBowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.BONE, 1.2f, 20, List.of(), List.of(new AspectInstance(Aspects.PIERCING, 1.0f))));
    public static final RegistryObject<Item> BONE_REPEATER = ITEMS.register("bone_repeater", () -> new AspectBowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.BONE, 1.1f, 16, List.of(new AspectInstance(Aspects.REPEAT)), List.of()));
    public static final RegistryObject<Item> BONE_SLUG_BOW = ITEMS.register("bone_slug_bow", () -> new AspectCrossbowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.BONE,1.5f, 25, List.of(), List.of(new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 0.5f))));
    public static final RegistryObject<Item> BOWN = ITEMS.register("bown", () -> new AspectBowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.BONE, 1.2f, 20, List.of(new AspectInstance(Aspects.REPEAT)), List.of(new AspectInstance(Aspects.PIERCING, 1.0f), new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 0.5f))));
    public static final RegistryObject<Item> VOID_BOW = ITEMS.register("void_bow", () -> new ConditionalAmpBowItem.NumericalItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.ULTRA_2, 1.35f, 20, List.of(), List.of(new AspectInstance(Aspects.VOID_STRENGTH, 0.4f)), 9));

    public static final RegistryObject<Item> RUSTY_SONIC_FORK = ITEMS.register("rusty_sonic_fork", () -> new ProjectileLaunchItem(new Item.Properties().durability(OdysseyTiers.RUSTY_IRON.getUses()).tab(OdysseyCreativeModeTab.RANGED), 1.2f, SonicBoom::new));

    // ## Tier 2
    public static final RegistryObject<Item> BANDIT_CROSSBOW = ITEMS.register("bandit_crossbow", () -> new AspectCrossbowItem(new Item.Properties().rarity(OdysseyRarity.UNCRAFTABLE_EQUIPMENT).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.UNCRAFTABLE_1, 1.5f, 20, List.of(), List.of(new AspectInstance(Aspects.PROJECTILE_LARCENY_CHANCE, 0.1f))));
    public static final RegistryObject<Item> CLOVER_STONE_BOOMERANG = ITEMS.register("clover_stone_boomerang", () -> new BoomerangItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.CLOVER_STONE, ThrowableTypes.CLOVER_STONE_BOOMERANG));

    public static final RegistryObject<Item> OBSIDIAN_DISC = ITEMS.register("obsidian_disc", () -> new BoomerangItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.OBSIDIAN, ThrowableTypes.OBSIDIAN_DISC));
    public static final RegistryObject<Item> AMETHYST_DISC = ITEMS.register("amethyst_disc", () -> new BoomerangItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.AMETHYST, ThrowableTypes.AMETHYST_DISC));
    public static final RegistryObject<Item> DIAMOND_DISC = ITEMS.register("diamond_disc", () -> new BoomerangItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.DIAMOND, ThrowableTypes.DIAMOND_DISC));
    public static final RegistryObject<Item> GREATROOT_BOOMERANG = ITEMS.register("greatroot_boomerang", () -> new BoomerangItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.GREATROOT, ThrowableTypes.GREATROOT_BOOMERANG));
    public static final RegistryObject<Item> GREATROOT_LONG_BOW = ITEMS.register("greatroot_long_bow", () -> new AspectBowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.GREATROOT,1.5f, 20, List.of(), List.of(new AspectInstance(Aspects.PIERCING, 2.0f))));
    public static final RegistryObject<Item> GREATROOT_REPEATER = ITEMS.register("greatroot_repeater", () -> new AspectBowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.GREATROOT,1.2f, 14, List.of(new AspectInstance(Aspects.REPEAT)), List.of()));
    public static final RegistryObject<Item> GREATROOT_SNIPER_BOW = ITEMS.register("greatroot_sniper_bow", () -> new AspectBowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.GREATROOT,2.4f, 30, List.of(new AspectInstance(Aspects.SPYGLASS)), List.of(new AspectInstance(Aspects.ACCURACY, 4.0f))));
    public static final RegistryObject<Item> GREATROOT_SLUG_BOW = ITEMS.register("greatroot_slug_bow", () -> new AspectCrossbowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.GREATROOT,1.85f, 25, List.of(), List.of(new AspectInstance(Aspects.PROJECTILE_KNOCKBACK, 1.0f))));
    public static final RegistryObject<Item> GREATROOT_SHOT_BOW = ITEMS.register("greatroot_shot_bow", () -> new AspectCrossbowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.GREATROOT,1.65f, 22, List.of(), List.of(new AspectInstance(Aspects.MULTISHOT, 2.0f))));
    public static final RegistryObject<Item> GREATROOT_CROSSBOW = ITEMS.register("greatroot_crossbow", () -> new AspectCrossbowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), OdysseyTiers.GREATROOT,1.9f, 25, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));

    // ## Tier 3

    public static final RegistryObject<Item> MARINE_TRIDENT = ITEMS.register("marine_trident", () -> new SpearItem(new Item.Properties().tab(OdysseyCreativeModeTab.MELEE), OdysseyTiers.MARINE, MeleeWeaponClass.SPEAR, ThrowableTypes.MARINE_TRIDENT));

    // ## Quivers
    public static final RegistryObject<Item> RABBIT_HIDE_QUIVER = ITEMS.register("rabbit_hide_quiver", () -> new QuiverItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), QuiverType.RABBIT_HIDE_QUIVER));
    public static final RegistryObject<Item> RABBIT_HIDE_ROCKET_BAG = ITEMS.register("rabbit_hide_rocket_bag", () -> new QuiverItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), QuiverType.RABBIT_HIDE_ROCKET_BAG));

    // ## Arrows
    public static final RegistryObject<Item> SPIDER_FANG_ARROW = ITEMS.register("spider_fang_arrow", () -> new AspectArrowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), ArrowType.SPIDER_FANG));
    public static final RegistryObject<Item> WEAVER_FANG_ARROW = ITEMS.register("weaver_fang_arrow", () -> new AspectArrowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), ArrowType.WEAVER_FANG));
    public static final RegistryObject<Item> CLOVER_STONE_ARROW = ITEMS.register("clover_stone_arrow", () -> new AspectArrowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), ArrowType.CLOVER_STONE));
    public static final RegistryObject<Item> AMETHYST_ARROW = ITEMS.register("amethyst_arrow", () -> new AspectArrowItem((new Item.Properties()).tab(OdysseyCreativeModeTab.RANGED), ArrowType.AMETHYST));

    // # Armors

    // ## Tier 1
    public static final RegistryObject<Item> HOLLOW_COCONUT = ITEMS.register("hollow_coconut", () -> new HollowCoconutItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.COCONUT, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.BINDING).withDisplaySetting(AspectTooltipDisplaySetting.NEVER))));

    public static final RegistryObject<Item> CACTUS_HELMET = ITEMS.register("cactus_helmet", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.CACTUS, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.THORNS, 1.0f))));
    public static final RegistryObject<Item> CACTUS_CHESTPLATE = ITEMS.register("cactus_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.CACTUS, EquipmentSlot.CHEST, List.of(), List.of(new AspectInstance(Aspects.THORNS, 1.0f))));
    public static final RegistryObject<Item> CACTUS_LEGGINGS = ITEMS.register("cactus_leggings", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.CACTUS, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.THORNS, 1.0f))));
    public static final RegistryObject<Item> CACTUS_BOOTS = ITEMS.register("cactus_boots", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.CACTUS, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.THORNS, 1.0f))));

    public static final RegistryObject<Item> CHICKEN_HELMET = ITEMS.register("chicken_helmet", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.CHICKEN, EquipmentSlot.HEAD, List.of(), List.of()));
    public static final RegistryObject<Item> CHICKEN_CHESTPLATE = ITEMS.register("chicken_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.CHICKEN, EquipmentSlot.CHEST, List.of(), List.of()));
    public static final RegistryObject<Item> CHICKEN_LEGGINGS = ITEMS.register("chicken_leggings", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.CHICKEN, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.FEATHER_FALLING, 2.0f))));
    public static final RegistryObject<Item> CHICKEN_BOOTS = ITEMS.register("chicken_boots", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.CHICKEN, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.FEATHER_FALLING, 2.0f))));

    public static final RegistryObject<Item> FUR_HELMET = ITEMS.register("fur_helmet", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.FUR, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.ICE_PROTECTION, 1.0f), new AspectInstance(Aspects.FREEZE_IMMUNITY, 1))));
    public static final RegistryObject<Item> FUR_CHESTPLATE = ITEMS.register("fur_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.FUR, EquipmentSlot.CHEST, List.of(), List.of(new AspectInstance(Aspects.ICE_PROTECTION, 1.0f), new AspectInstance(Aspects.FREEZE_IMMUNITY, 1))));
    public static final RegistryObject<Item> FUR_LEGGINGS = ITEMS.register("fur_leggings", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.FUR, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.ICE_PROTECTION, 1.0f), new AspectInstance(Aspects.FREEZE_IMMUNITY, 1))));
    public static final RegistryObject<Item> FUR_BOOTS = ITEMS.register("fur_boots", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.FUR, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.ICE_PROTECTION, 1.0f), new AspectInstance(Aspects.FREEZE_IMMUNITY, 1))));

    public static final RegistryObject<Item> GLIDER_HELMET = ITEMS.register("glider_helmet", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.GLIDER, EquipmentSlot.HEAD, List.of(), List.of()));
    public static final RegistryObject<Item> GLIDER_CHESTPLATE = ITEMS.register("glider_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.GLIDER, EquipmentSlot.CHEST, List.of(), List.of()));
    public static final RegistryObject<Item> GLIDER_LEGGINGS = ITEMS.register("glider_leggings", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.GLIDER, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.FEATHER_FALLING, 3.0f))));
    public static final RegistryObject<Item> GLIDER_BOOTS = ITEMS.register("glider_boots", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.GLIDER, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.FEATHER_FALLING, 3.0f))));

    public static final RegistryObject<Item> TURTLE_HELMET = ITEMS.register("turtle_helmet", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.TURTLE, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.RESPIRATION, 1.0f))));
    public static final RegistryObject<Item> TURTLE_CHESTPLATE = ITEMS.register("turtle_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.TURTLE, EquipmentSlot.CHEST, List.of(), List.of(new AspectInstance(Aspects.RESPIRATION, 1.0f), new AspectInstance(Aspects.SWIM_SPEED, 0.25f))));
    public static final RegistryObject<Item> TURTLE_LEGGINGS = ITEMS.register("turtle_leggings", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.TURTLE, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.SWIM_SPEED, 0.25f))));
    public static final RegistryObject<Item> TURTLE_BOOTS = ITEMS.register("turtle_boots", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.TURTLE, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.SWIM_SPEED, 0.25f))));
    
    // ## Tier 2
    public static final RegistryObject<Item> THORNMAIL_HELMET = ITEMS.register("thornmail_helmet", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.THORNMAIL, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.THORNS, 1.5f))));
    public static final RegistryObject<Item> THORNMAIL_CHESTPLATE = ITEMS.register("thornmail_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.THORNMAIL, EquipmentSlot.CHEST, List.of(), List.of(new AspectInstance(Aspects.THORNS, 1.5f))));
    public static final RegistryObject<Item> THORNMAIL_LEGGINGS = ITEMS.register("thornmail_leggings", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.THORNMAIL, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.THORNS, 1.5f))));
    public static final RegistryObject<Item> THORNMAIL_BOOTS = ITEMS.register("thornmail_boots", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.THORNMAIL, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.THORNS, 1.5f))));
    
    public static final RegistryObject<Item> PARKA_HELMET = ITEMS.register("parka_helmet", () -> new DyeableAspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.PARKA, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.ICE_PROTECTION, 1.5f), new AspectInstance(Aspects.FREEZE_IMMUNITY, 1))));
    public static final RegistryObject<Item> PARKA_CHESTPLATE = ITEMS.register("parka_chestplate", () -> new DyeableAspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.PARKA, EquipmentSlot.CHEST, List.of(), List.of(new AspectInstance(Aspects.ICE_PROTECTION, 1.5f), new AspectInstance(Aspects.FREEZE_IMMUNITY, 1))));
    public static final RegistryObject<Item> PARKA_LEGGINGS = ITEMS.register("parka_leggings", () -> new DyeableAspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.PARKA, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.ICE_PROTECTION, 1.5f), new AspectInstance(Aspects.FREEZE_IMMUNITY, 1))));
    public static final RegistryObject<Item> PARKA_BOOTS = ITEMS.register("parka_boots", () -> new DyeableAspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.PARKA, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.ICE_PROTECTION, 1.5f), new AspectInstance(Aspects.FREEZE_IMMUNITY, 1), new AspectInstance(Aspects.SNOWSHOE))));
    
    public static final RegistryObject<Item> ZEPHYR_HELMET = ITEMS.register("zephyr_helmet", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.ZEPHYR, EquipmentSlot.HEAD, List.of(), List.of()));
    public static final RegistryObject<Item> ZEPHYR_CHESTPLATE = ITEMS.register("zephyr_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.ZEPHYR, EquipmentSlot.CHEST, List.of(), List.of()));
    public static final RegistryObject<Item> ZEPHYR_LEGGINGS = ITEMS.register("zephyr_leggings", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.ZEPHYR, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.FEATHER_FALLING, 4.0f))));
    public static final RegistryObject<Item> ZEPHYR_BOOTS = ITEMS.register("zephyr_boots", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.ZEPHYR, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.FEATHER_FALLING, 4.0f))));
    
    public static final RegistryObject<Item> STERLING_SILVER_HELMET = ITEMS.register("sterling_silver_helmet", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlot.HEAD, List.of(), List.of()));
    public static final RegistryObject<Item> STERLING_SILVER_CHESTPLATE = ITEMS.register("sterling_silver_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlot.CHEST, List.of(), List.of()));
    public static final RegistryObject<Item> STERLING_SILVER_LEGGINGS = ITEMS.register("sterling_silver_leggings", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlot.LEGS, List.of(), List.of()));
    public static final RegistryObject<Item> STERLING_SILVER_BOOTS = ITEMS.register("sterling_silver_boots", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.STERLING_SILVER, EquipmentSlot.FEET, List.of(), List.of()));

    public static final RegistryObject<Item> REINFORCED_HELMET = ITEMS.register("reinforced_helmet", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.REINFORCED, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.BLAST_PROTECTION, 1.0f))));
    public static final RegistryObject<Item> REINFORCED_CHESTPLATE = ITEMS.register("reinforced_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.REINFORCED, EquipmentSlot.CHEST, List.of(), List.of(new AspectInstance(Aspects.BLAST_PROTECTION, 1.0f))));
    public static final RegistryObject<Item> REINFORCED_LEGGINGS = ITEMS.register("reinforced_leggings", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.REINFORCED, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.BLAST_PROTECTION, 1.0f))));
    public static final RegistryObject<Item> REINFORCED_BOOTS = ITEMS.register("reinforced_boots", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.REINFORCED, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.BLAST_PROTECTION, 1.0f))));
    
    public static final RegistryObject<Item> ARCTIC_HELMET = ITEMS.register("arctic_helmet", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.ARCTIC, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.FIRE_PROTECTION, 1.0f))));
    public static final RegistryObject<Item> ARCTIC_CHESTPLATE = ITEMS.register("arctic_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.ARCTIC, EquipmentSlot.CHEST, List.of(), List.of(new AspectInstance(Aspects.FIRE_PROTECTION, 1.0f))));
    public static final RegistryObject<Item> ARCTIC_LEGGINGS = ITEMS.register("arctic_leggings", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.ARCTIC, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.FIRE_PROTECTION, 1.0f))));
    public static final RegistryObject<Item> ARCTIC_BOOTS = ITEMS.register("arctic_boots", () -> new AspectArmorItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), OdysseyArmorMaterial.ARCTIC, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.FIRE_PROTECTION, 1.0f))));

    // ## Horse Armor
    public static final RegistryObject<Item> STERLING_SILVER_HORSE_ARMOR = ITEMS.register("sterling_silver_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterial.STERLING_SILVER.getTotalDefense(), new ResourceLocation(Odyssey.MOD_ID, "textures/entity/horse/armor/horse_armor_sterling_silver.png"), (new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.ARMOR)));
    public static final RegistryObject<Item> NETHERITE_HORSE_ARMOR = ITEMS.register("netherite_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterial.NETHERITE.getTotalDefense(), new ResourceLocation(Odyssey.MOD_ID, "textures/entity/horse/armor/horse_armor_netherite.png"), (new Item.Properties()).stacksTo(1).fireResistant().tab(OdysseyCreativeModeTab.ARMOR)));

    // ## Shields
    public static final RegistryObject<Item> WOODEN_SHIELD = ITEMS.register("wooden_shield", () -> new AspectShieldItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), ShieldType.WOODEN));
    public static final RegistryObject<Item> COPPER_SHIELD = ITEMS.register("copper_shield", () -> new AspectShieldItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), ShieldType.COPPER));
    public static final RegistryObject<Item> RUSTY_SHIELD = ITEMS.register("rusty_shield", () -> new AspectShieldItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), ShieldType.RUSTY));
    public static final RegistryObject<Item> GOLDEN_SHIELD = ITEMS.register("golden_shield", () -> new AspectShieldItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), ShieldType.GOLDEN));
    public static final RegistryObject<Item> REINFORCED_SHIELD = ITEMS.register("reinforced_shield", () -> new AspectShieldItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), ShieldType.REINFORCED));
    public static final RegistryObject<Item> DIAMOND_SHIELD = ITEMS.register("diamond_shield", () -> new AspectShieldItem(new Item.Properties().tab(OdysseyCreativeModeTab.ARMOR), ShieldType.DIAMOND));

    // # Spawning
    public static final RegistryObject<Item> MOON_TOWER_ZOMBIE_SPAWN_EGG = ITEMS.register("moon_tower_zombie_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.MOON_TOWER_ZOMBIE, 0x4C7289, 0x394140, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> ODYSSEY_SKELETON_SPAWN_EGG = ITEMS.register("odyssey_skeleton_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.SKELETON, 12698049, 4802889, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> MOON_TOWER_SKELETON_SPAWN_EGG = ITEMS.register("moon_tower_skeleton_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.MOON_TOWER_SKELETON, 0x9E9EBD, 0x565664, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> BABY_CREEPER_SPAWN_EGG = ITEMS.register("baby_creeper_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.BABY_CREEPER, 894731, 0, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> CAMO_CREEPER_SPAWN_EGG = ITEMS.register("camo_creeper_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.CAMO_CREEPER, 0x50692c, 0x79553a, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> WEAVER_SPAWN_EGG = ITEMS.register("weaver_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.WEAVER, 0x442512, 0xff4444, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> PASSIVE_WEAVER_SPAWN_EGG = ITEMS.register("passive_weaver_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.PASSIVE_WEAVER, 0x442512, 0x00b0ff, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> ABANDONED_IRON_GOLEM = ITEMS.register("abandoned_iron_golem", () -> new DoubleHighBlockItem(BlockRegistry.ABANDONED_IRON_GOLEM.get(), (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> BABY_LEVIATHAN_SPAWN_EGG = ITEMS.register("baby_leviathan_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.BABY_LEVIATHAN, 0x2f2f37, 0x646464, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> BABY_LEVIATHAN_BUCKET = ITEMS.register("baby_leviathan_bucket", () -> new BabyLeviathanBucket((new Item.Properties()).stacksTo(1).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> WEAVER_EGG = ITEMS.register("weaver_egg", () -> new WeaverEggItem((new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> ODYSSEY_POLAR_BEAR_SPAWN_EGG = ITEMS.register("polar_bear_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.POLAR_BEAR, 15921906, 9803152, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> ZOMBIE_BRUTE_SPAWN_EGG = ITEMS.register("zombie_brute_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.ZOMBIE_BRUTE, 0x6464, 7969893, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> BARN_SPIDER_SPAWN_EGG = ITEMS.register("barn_spider_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.BARN_SPIDER, 0x8A8A45, 11013646, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> BANDIT_SPAWN_EGG = ITEMS.register("bandit_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.BANDIT, 0x503213, 0x282828, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));
    public static final RegistryObject<Item> WRAITH_SPAWN_EGG = ITEMS.register("wraith_spawn_egg", () -> new OdysseySpawnEggItem(EntityTypeRegistry.WRAITH, 0xF3F4F5, 0xA8ACAE, (new Item.Properties()).tab(OdysseyCreativeModeTab.SPAWNING)));

    /////////////////////////////////////////////////////////////////////////

    //Vanilla Overrides
    
    public static final RegistryObject<Item> WOODEN_SWORD = ITEMS_VANILLA.register("wooden_sword", () -> new AspectMeleeItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyTiers.WOOD, MeleeWeaponClass.SWORD, 4f, List.of(), List.of()));
    public static final RegistryObject<Item> STONE_SWORD = ITEMS_VANILLA.register("stone_sword", () -> new AspectMeleeItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyTiers.STONE, MeleeWeaponClass.SWORD, 5f, List.of(), List.of()));
    public static final RegistryObject<Item> IRON_SWORD = ITEMS_VANILLA.register("iron_sword", () -> new AspectMeleeItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyTiers.IRON, MeleeWeaponClass.SWORD, 6f, List.of(), List.of()));
    public static final RegistryObject<Item> GOLDEN_SWORD = ITEMS_VANILLA.register("golden_sword", () -> new AspectMeleeItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyTiers.GOLD, MeleeWeaponClass.SWORD.withBetterAttackSpeed(), 5f, List.of(), List.of()));
    public static final RegistryObject<Item> DIAMOND_SWORD = ITEMS_VANILLA.register("diamond_sword", () -> new AspectMeleeItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyTiers.DIAMOND, MeleeWeaponClass.SWORD, 8f, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));
    public static final RegistryObject<Item> NETHERITE_SWORD = ITEMS_VANILLA.register("netherite_sword", () -> new AspectMeleeItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyTiers.NETHERITE, MeleeWeaponClass.SWORD, 9f, List.of(), List.of(new AspectInstance(Aspects.BURN_PROOF))));

    public static final RegistryObject<Item> WOODEN_AXE = ITEMS_VANILLA.register("wooden_axe", () -> new AspectAxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.WOOD, MeleeWeaponClass.AXE, 3.0f, List.of(), List.of()));
    public static final RegistryObject<Item> STONE_AXE = ITEMS_VANILLA.register("stone_axe", () -> new AspectAxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.STONE, MeleeWeaponClass.AXE, 4.0f, List.of(), List.of()));
    public static final RegistryObject<Item> IRON_AXE = ITEMS_VANILLA.register("iron_axe", () -> new AspectAxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.IRON, MeleeWeaponClass.AXE, 5.0f, List.of(), List.of()));
    public static final RegistryObject<Item> GOLDEN_AXE = ITEMS_VANILLA.register("golden_axe", () -> new AspectAxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.GOLD, MeleeWeaponClass.AXE.withBetterAttackSpeed(), 4.0f, List.of(), List.of(new AspectInstance(Aspects.EFFICIENCY, 0.5f))));
    public static final RegistryObject<Item> DIAMOND_AXE = ITEMS_VANILLA.register("diamond_axe", () -> new AspectAxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.DIAMOND, MeleeWeaponClass.AXE, 7.0f, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));
    public static final RegistryObject<Item> NETHERITE_AXE = ITEMS_VANILLA.register("netherite_axe", () -> new AspectAxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.NETHERITE, MeleeWeaponClass.AXE, 8.0f, List.of(), List.of(new AspectInstance(Aspects.BURN_PROOF))));

    public static final RegistryObject<Item> WOODEN_PICKAXE = ITEMS_VANILLA.register("wooden_pickaxe", () -> new AspectPickaxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.WOOD, MeleeWeaponClass.PICKAXE, 2.0f, List.of(), List.of()));
    public static final RegistryObject<Item> STONE_PICKAXE = ITEMS_VANILLA.register("stone_pickaxe", () -> new AspectPickaxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.STONE, MeleeWeaponClass.PICKAXE, 3.0f, List.of(), List.of()));
    public static final RegistryObject<Item> IRON_PICKAXE = ITEMS_VANILLA.register("iron_pickaxe", () -> new AspectPickaxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.IRON, MeleeWeaponClass.PICKAXE, 4.0f, List.of(), List.of()));
    public static final RegistryObject<Item> GOLDEN_PICKAXE = ITEMS_VANILLA.register("golden_pickaxe", () -> new AspectPickaxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.GOLD, MeleeWeaponClass.PICKAXE.withBetterAttackSpeed(), 3.0f, List.of(), List.of(new AspectInstance(Aspects.EFFICIENCY, 0.5f))));
    public static final RegistryObject<Item> DIAMOND_PICKAXE = ITEMS_VANILLA.register("diamond_pickaxe", () -> new AspectPickaxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.DIAMOND, MeleeWeaponClass.PICKAXE, 6.0f, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));
    public static final RegistryObject<Item> NETHERITE_PICKAXE = ITEMS_VANILLA.register("netherite_pickaxe", () -> new AspectPickaxeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.NETHERITE, MeleeWeaponClass.PICKAXE, 7.0f, List.of(), List.of(new AspectInstance(Aspects.BURN_PROOF))));

    public static final RegistryObject<Item> WOODEN_SHOVEL = ITEMS_VANILLA.register("wooden_shovel", () -> new AspectShovelItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.WOOD, MeleeWeaponClass.SHOVEL, 1.5f, List.of(), List.of()));
    public static final RegistryObject<Item> STONE_SHOVEL = ITEMS_VANILLA.register("stone_shovel", () -> new AspectShovelItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.STONE, MeleeWeaponClass.SHOVEL, 2.5f, List.of(), List.of()));
    public static final RegistryObject<Item> IRON_SHOVEL = ITEMS_VANILLA.register("iron_shovel", () -> new AspectShovelItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.IRON, MeleeWeaponClass.SHOVEL, 3.5f, List.of(), List.of()));
    public static final RegistryObject<Item> GOLDEN_SHOVEL = ITEMS_VANILLA.register("golden_shovel", () -> new AspectShovelItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.GOLD, MeleeWeaponClass.SHOVEL.withBetterAttackSpeed(), 2.5f, List.of(), List.of(new AspectInstance(Aspects.EFFICIENCY, 0.5f))));
    public static final RegistryObject<Item> DIAMOND_SHOVEL = ITEMS_VANILLA.register("diamond_shovel", () -> new AspectShovelItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.DIAMOND, MeleeWeaponClass.SHOVEL, 5.5f, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));
    public static final RegistryObject<Item> NETHERITE_SHOVEL = ITEMS_VANILLA.register("netherite_shovel", () -> new AspectShovelItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.NETHERITE, MeleeWeaponClass.SHOVEL, 6.5f, List.of(), List.of(new AspectInstance(Aspects.BURN_PROOF))));

    public static final RegistryObject<Item> WOODEN_HOE = ITEMS_VANILLA.register("wooden_hoe", () -> new AspectHoeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.WOOD, MeleeWeaponClass.HOE, 1.0f, List.of(), List.of()));
    public static final RegistryObject<Item> STONE_HOE = ITEMS_VANILLA.register("stone_hoe", () -> new AspectHoeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.STONE, MeleeWeaponClass.HOE, 2.0f, List.of(), List.of()));
    public static final RegistryObject<Item> IRON_HOE = ITEMS_VANILLA.register("iron_hoe", () -> new AspectHoeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.IRON, MeleeWeaponClass.HOE, 3.0f, List.of(), List.of()));
    public static final RegistryObject<Item> GOLDEN_HOE = ITEMS_VANILLA.register("golden_hoe", () -> new AspectHoeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.GOLD, MeleeWeaponClass.HOE.withBetterAttackSpeed(), 2.0f, List.of(), List.of(new AspectInstance(Aspects.EFFICIENCY, 0.5f))));
    public static final RegistryObject<Item> DIAMOND_HOE = ITEMS_VANILLA.register("diamond_hoe", () -> new AspectHoeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.DIAMOND, MeleeWeaponClass.HOE, 5.0f, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));
    public static final RegistryObject<Item> NETHERITE_HOE = ITEMS_VANILLA.register("netherite_hoe", () -> new AspectHoeItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS), OdysseyTiers.NETHERITE, MeleeWeaponClass.HOE, 6.0f, List.of(), List.of(new AspectInstance(Aspects.BURN_PROOF))));

    public static final RegistryObject<Item> LEATHER_HELMET = ITEMS_VANILLA.register("leather_helmet", () -> new DyeableAspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.LEATHER, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.FREEZE_IMMUNITY, 1))));
    public static final RegistryObject<Item> LEATHER_CHESTPLATE = ITEMS_VANILLA.register("leather_chestplate", () -> new DyeableAspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.LEATHER, EquipmentSlot.CHEST, List.of(), List.of(new AspectInstance(Aspects.FREEZE_IMMUNITY, 1))));
    public static final RegistryObject<Item> LEATHER_LEGGINGS = ITEMS_VANILLA.register("leather_leggings", () -> new DyeableAspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.LEATHER, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.FREEZE_IMMUNITY, 1))));
    public static final RegistryObject<Item> LEATHER_BOOTS = ITEMS_VANILLA.register("leather_boots", () -> new DyeableAspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.LEATHER, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.FREEZE_IMMUNITY, 1), new AspectInstance(Aspects.SNOWSHOE))));
    public static final RegistryObject<Item> LEATHER_HORSE_ARMOR = ITEMS_VANILLA.register("leather_horse_armor", () -> new DyeableHorseArmorItem(OdysseyArmorMaterial.LEATHER.getTotalDefense(), "leather", (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> CHAINMAIL_HELMET = ITEMS_VANILLA.register("chainmail_helmet", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_CHESTPLATE = ITEMS_VANILLA.register("chainmail_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_LEGGINGS = ITEMS_VANILLA.register("chainmail_leggings", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> CHAINMAIL_BOOTS = ITEMS_VANILLA.register("chainmail_boots", () -> new ArmorItem(OdysseyArmorMaterial.CHAIN, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistryObject<Item> IRON_HELMET = ITEMS_VANILLA.register("iron_helmet", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_CHESTPLATE = ITEMS_VANILLA.register("iron_chestplate", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlot.CHEST, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_LEGGINGS = ITEMS_VANILLA.register("iron_leggings", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlot.LEGS, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_BOOTS = ITEMS_VANILLA.register("iron_boots", () -> new ArmorItem(OdysseyArmorMaterial.IRON, EquipmentSlot.FEET, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> IRON_HORSE_ARMOR = ITEMS_VANILLA.register("iron_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterial.IRON.getTotalDefense(), "iron", (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> GOLDEN_HELMET = ITEMS_VANILLA.register("golden_helmet", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.GOLD, EquipmentSlot.HEAD, List.of(new AspectInstance(Aspects.PIGLIN_NEUTRAL)), List.of()));
    public static final RegistryObject<Item> GOLDEN_CHESTPLATE = ITEMS_VANILLA.register("golden_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.GOLD, EquipmentSlot.CHEST, List.of(new AspectInstance(Aspects.PIGLIN_NEUTRAL)), List.of()));
    public static final RegistryObject<Item> GOLDEN_LEGGINGS = ITEMS_VANILLA.register("golden_leggings", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.GOLD, EquipmentSlot.LEGS, List.of(new AspectInstance(Aspects.PIGLIN_NEUTRAL)), List.of()));
    public static final RegistryObject<Item> GOLDEN_BOOTS = ITEMS_VANILLA.register("golden_boots", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.GOLD, EquipmentSlot.FEET, List.of(new AspectInstance(Aspects.PIGLIN_NEUTRAL)), List.of()));
    public static final RegistryObject<Item> GOLDEN_HORSE_ARMOR = ITEMS_VANILLA.register("golden_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterial.GOLD.getTotalDefense(), "gold", (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> DIAMOND_HELMET = ITEMS_VANILLA.register("diamond_helmet", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.DIAMOND, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));
    public static final RegistryObject<Item> DIAMOND_CHESTPLATE = ITEMS_VANILLA.register("diamond_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.DIAMOND, EquipmentSlot.CHEST, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));
    public static final RegistryObject<Item> DIAMOND_LEGGINGS = ITEMS_VANILLA.register("diamond_leggings", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.DIAMOND, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));
    public static final RegistryObject<Item> DIAMOND_BOOTS = ITEMS_VANILLA.register("diamond_boots", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.DIAMOND, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.DURABILITY, 2.0f))));
    public static final RegistryObject<Item> DIAMOND_HORSE_ARMOR = ITEMS_VANILLA.register("diamond_horse_armor", () -> new HorseArmorItem(OdysseyArmorMaterial.DIAMOND.getTotalDefense(), "diamond", (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> NETHERITE_HELMET = ITEMS_VANILLA.register("netherite_helmet", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.NETHERITE, EquipmentSlot.HEAD, List.of(), List.of(new AspectInstance(Aspects.BURN_PROOF))));
    public static final RegistryObject<Item> NETHERITE_CHESTPLATE = ITEMS_VANILLA.register("netherite_chestplate", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.NETHERITE, EquipmentSlot.CHEST, List.of(), List.of(new AspectInstance(Aspects.BURN_PROOF))));
    public static final RegistryObject<Item> NETHERITE_LEGGINGS = ITEMS_VANILLA.register("netherite_leggings", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.NETHERITE, EquipmentSlot.LEGS, List.of(), List.of(new AspectInstance(Aspects.BURN_PROOF))));
    public static final RegistryObject<Item> NETHERITE_BOOTS = ITEMS_VANILLA.register("netherite_boots", () -> new AspectArmorItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT), OdysseyArmorMaterial.NETHERITE, EquipmentSlot.FEET, List.of(), List.of(new AspectInstance(Aspects.BURN_PROOF))));

    public static final RegistryObject<Item> BOW = ITEMS_VANILLA.register("bow", () -> new AspectBowItem((new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT), OdysseyTiers.STONE, 1.0f, 20, List.of(), List.of()));
    public static final RegistryObject<Item> CROSSBOW = ITEMS_VANILLA.register("crossbow", () -> new AspectCrossbowItem((new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT), OdysseyTiers.STONE, 1.25f, 25, List.of(), List.of()));
    public static final RegistryObject<Item> ARROW = ITEMS_VANILLA.register("arrow", () -> new AspectArrowItem((new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT), ArrowType.FLINT));

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