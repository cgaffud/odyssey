package com.bedmen.odyssey.tier;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tags.OdysseyBlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class OdysseyTiers {

    // # Has diggers

    // ## Tier 1
    public static final OdysseyTier CACTUS = new OdysseyTier(2, () -> Ingredient.of(Items.CACTUS));
    public static final OdysseyTier WOOD = new OdysseyTier(3,59,2.0F, Tags.Blocks.NEEDS_WOOD_TOOL, () -> Ingredient.of(ItemTags.PLANKS));
    public static final OdysseyTier COCONUT = new OdysseyTier(3,() -> Ingredient.of(ItemRegistry.HOLLOW_COCONUT.get()));
    public static final OdysseyTier STONE = new OdysseyTier(1,131,4.0F, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS));
    public static final OdysseyTier FLINT = new OdysseyTier(2,150, () -> Ingredient.of(Items.FLINT));
    public static final OdysseyTier BONE = new OdysseyTier(2,175, () -> Ingredient.of(Items.BONE));
    public static final OdysseyTier LEATHER = new OdysseyTier(3,() -> Ingredient.of(Items.LEATHER));
    public static final OdysseyTier CHICKEN = new OdysseyTier(3,() -> Ingredient.of(ItemRegistry.FEATHER_BUNDLE.get()));
    public static final OdysseyTier FUR = new OdysseyTier(3,() -> Ingredient.of(ItemRegistry.POLAR_BEAR_FUR.get()));
    public static final OdysseyTier COPPER = new OdysseyTier(2,200, () -> Ingredient.of(Items.COPPER_INGOT));
    public static final OdysseyTier RUSTY_IRON = new OdysseyTier(2,200, () -> Ingredient.EMPTY);
    public static final OdysseyTier IRON = new OdysseyTier(2,250,6.0F, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.IRON_INGOT));
    public static final OdysseyTier GOLD = new OdysseyTier(5,32,12.0F, Tags.Blocks.NEEDS_GOLD_TOOL, () -> Ingredient.of(Items.GOLD_INGOT));
    public static final OdysseyTier TURTLE = new OdysseyTier(2,() -> Ingredient.of(Items.SCUTE));
    public static final OdysseyTier UNCRAFTABLE_1 = new OdysseyTier(2,350, 6.0F, () -> Ingredient.EMPTY);
    public static final OdysseyTier ULTRA_1 = new OdysseyTier(2,500, () -> Ingredient.EMPTY);
    // ## Tier 2
    public static final OdysseyTier GLIDER = new OdysseyTier(3,() -> Ingredient.of(Items.PHANTOM_MEMBRANE));
    public static final OdysseyTier THORNMAIL = new OdysseyTier(3,() -> Ingredient.EMPTY);
    public static final OdysseyTier PARKA = new OdysseyTier(3,() -> Ingredient.EMPTY);
    public static final OdysseyTier STERLING_SILVER = new OdysseyTier(3,500,7.0F, OdysseyBlockTags.STERLING_SILVER_TAG, () -> Ingredient.of(ItemRegistry.STERLING_SILVER_INGOT.get()));
    public static final OdysseyTier AMETHYST = new OdysseyTier(2,450, () -> Ingredient.of(Items.AMETHYST_SHARD));
    public static final OdysseyTier OBSIDIAN = new OdysseyTier(2,800, () -> Ingredient.of(Items.OBSIDIAN));
    public static final OdysseyTier CLOVER_STONE = new OdysseyTier(2,131,9.0F, () -> Ingredient.of(ItemRegistry.CLOVER_STONE.get()));
    public static final OdysseyTier REINFORCED = new OdysseyTier(2,() -> Ingredient.EMPTY);
    public static final OdysseyTier DIAMOND = new OdysseyTier(4,500,8.0F, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(Items.DIAMOND));
    public static final OdysseyTier GREATROOT = new OdysseyTier(4,750, () -> Ingredient.of(ItemRegistry.GREATROOT.get()));
    public static final OdysseyTier ARCTIC = new OdysseyTier(4,() -> Ingredient.of(ItemRegistry.PERMAFROST_SHARD.get()));
    public static final OdysseyTier MARINE = new OdysseyTier(4,1000,10.0F, () -> Ingredient.of(ItemRegistry.PEARL.get()));
    public static final OdysseyTier UNCRAFTABLE_2 = new OdysseyTier(2,700, 9.0F, () -> Ingredient.EMPTY);
    public static final OdysseyTier ULTRA_2 = new OdysseyTier(2,750, () -> Ingredient.EMPTY);
    // ## Tier 3
    public static final OdysseyTier ZEPHYR = new OdysseyTier(4,() -> Ingredient.EMPTY);
    public static final OdysseyTier NETHERITE = new OdysseyTier(4,1000,9.0F, Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT));

    // # No Diggers

    public static void init(){
        TierSortingRegistry.registerTier(CACTUS, new ResourceLocation(Odyssey.MOD_ID,"cactus"), List.of(), List.of(Tiers.WOOD));
        TierSortingRegistry.registerTier(WOOD, new ResourceLocation(Odyssey.MOD_ID,"wood"), List.of(Tiers.WOOD), List.of(Tiers.STONE));
        TierSortingRegistry.registerTier(COCONUT, new ResourceLocation(Odyssey.MOD_ID,"coconut"), List.of(WOOD), List.of(Tiers.STONE));
        TierSortingRegistry.registerTier(STONE, new ResourceLocation(Odyssey.MOD_ID,"stone"), List.of(Tiers.STONE), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(FLINT, new ResourceLocation(Odyssey.MOD_ID,"flint"), List.of(STONE), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(BONE, new ResourceLocation(Odyssey.MOD_ID,"bone"), List.of(FLINT), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(LEATHER, new ResourceLocation(Odyssey.MOD_ID,"leather"), List.of(BONE), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(CHICKEN, new ResourceLocation(Odyssey.MOD_ID,"chicken"), List.of(LEATHER), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(FUR, new ResourceLocation(Odyssey.MOD_ID,"fur"), List.of(CHICKEN), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(COPPER, new ResourceLocation(Odyssey.MOD_ID,"copper"), List.of(FUR), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(RUSTY_IRON, new ResourceLocation(Odyssey.MOD_ID,"rusty_iron"), List.of(COPPER), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(IRON, new ResourceLocation(Odyssey.MOD_ID,"iron"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(GOLD, new ResourceLocation(Odyssey.MOD_ID,"gold"), List.of(IRON), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(TURTLE, new ResourceLocation(Odyssey.MOD_ID,"turtle"), List.of(GOLD), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(UNCRAFTABLE_1, new ResourceLocation(Odyssey.MOD_ID,"uncraftable_1"), List.of(TURTLE), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(ULTRA_1, new ResourceLocation(Odyssey.MOD_ID,"ultra_1"), List.of(UNCRAFTABLE_1), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(GLIDER, new ResourceLocation(Odyssey.MOD_ID,"glider"), List.of(ULTRA_1), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(THORNMAIL, new ResourceLocation(Odyssey.MOD_ID,"thornmail"), List.of(GLIDER), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(PARKA, new ResourceLocation(Odyssey.MOD_ID,"parka"), List.of(THORNMAIL), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(STERLING_SILVER, new ResourceLocation(Odyssey.MOD_ID,"sterling_silver"), List.of(PARKA), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(AMETHYST, new ResourceLocation(Odyssey.MOD_ID,"amethyst"), List.of(STERLING_SILVER), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(OBSIDIAN, new ResourceLocation(Odyssey.MOD_ID,"obsidian"), List.of(AMETHYST), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(CLOVER_STONE, new ResourceLocation(Odyssey.MOD_ID,"clover_stone"), List.of(OBSIDIAN), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(REINFORCED, new ResourceLocation(Odyssey.MOD_ID,"reinforced"), List.of(CLOVER_STONE), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(DIAMOND, new ResourceLocation(Odyssey.MOD_ID,"diamond"), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));
        TierSortingRegistry.registerTier(GREATROOT, new ResourceLocation(Odyssey.MOD_ID,"greatroot"), List.of(DIAMOND), List.of(Tiers.NETHERITE));
        TierSortingRegistry.registerTier(ARCTIC, new ResourceLocation(Odyssey.MOD_ID,"arctic"), List.of(GREATROOT), List.of(Tiers.NETHERITE));
        TierSortingRegistry.registerTier(MARINE, new ResourceLocation(Odyssey.MOD_ID,"marine"), List.of(ARCTIC), List.of(Tiers.NETHERITE));
        TierSortingRegistry.registerTier(UNCRAFTABLE_2, new ResourceLocation(Odyssey.MOD_ID,"uncraftable_2"), List.of(MARINE), List.of(Tiers.NETHERITE));
        TierSortingRegistry.registerTier(ULTRA_2, new ResourceLocation(Odyssey.MOD_ID,"ultra_2"), List.of(UNCRAFTABLE_2), List.of(Tiers.NETHERITE));
        TierSortingRegistry.registerTier(ZEPHYR, new ResourceLocation(Odyssey.MOD_ID,"zephyr"), List.of(ULTRA_2), List.of(Tiers.NETHERITE));
        TierSortingRegistry.registerTier(NETHERITE, new ResourceLocation(Odyssey.MOD_ID,"netherite"), List.of(Tiers.NETHERITE), List.of());
    }
}