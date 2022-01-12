package com.bedmen.odyssey.tools;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;

import java.util.List;

public class OdysseyTiers {

    //Main-line; Require an associated blocktag
    public static final Tag.Named<Block> STERLING_SILVER_TAG = BlockTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "needs_sterling_silver_tool"));
    public static final Tier STERLING_SILVER = new ForgeTier(2, 500,7.0F, -1.0f, 2, STERLING_SILVER_TAG, () -> Ingredient.of(ItemRegistry.SILVER_INGOT.get()));

    //Vanilla Overrides
    public static final Tier WOOD = new ForgeTier(0, 59,2.0F, -1.0f, 0, Tags.Blocks.NEEDS_WOOD_TOOL, () -> Ingredient.of(ItemTags.PLANKS));
    public static final Tier STONE = new ForgeTier(1, 131,4.0F, -1.0f, 0, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS));
    public static final Tier IRON = new ForgeTier(2, 250,6.0F, -1.0f, 0, BlockTags.NEEDS_IRON_TOOL, () -> Ingredient.of(Items.IRON_INGOT));
    public static final Tier GOLD = new ForgeTier(2, 32,12.0F, -1.0f, 7, Tags.Blocks.NEEDS_GOLD_TOOL, () -> Ingredient.of(Items.GOLD_INGOT));
    public static final Tier DIAMOND = new ForgeTier(3, 1561,8.0F, -1.0f, 0, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(Items.DIAMOND));
    public static final Tier NETHERITE = new ForgeTier(4, 2031,9.0F, -1.0f, 3, Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT));

    //Equipment, No Diggers
    public static final Tier FLINT = new ForgeTier(0, 150,2.0F, -1.0f, 0, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(Items.FLINT));
    public static final Tier BONE = new ForgeTier(0, 175,2.0F, -1.0f, 0, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(Items.BONE));
    public static final Tier COPPER = new ForgeTier(0, 200,2.0F, -1.0f, 0, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(Items.COPPER_INGOT));
    public static final Tier AMETHYST = new ForgeTier(0, 750,2.0F, -1.0f, 0, STERLING_SILVER_TAG, () -> Ingredient.of(Items.AMETHYST_SHARD));
    public static final Tier OBSIDIAN = new ForgeTier(0, 2000,2.0F, -1.0f, 0, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(Items.OBSIDIAN));

    //Equipment, Diggers
    public static final Tier CLOVER_STONE = new ForgeTier(2, 131,9.0F, -1.0f, 0, STERLING_SILVER_TAG, () -> Ingredient.of(ItemRegistry.CLOVER_STONE.get()));
    public static final Tier MARINE = new ForgeTier(2, 1000,10.0F, -1.0f, 0, BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ItemRegistry.PEARL.get()));

    public static void init(){
        TierSortingRegistry.registerTier(STERLING_SILVER, new ResourceLocation(Odyssey.MOD_ID,"sterling_silver"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));
        TierSortingRegistry.registerTier(CLOVER_STONE, new ResourceLocation(Odyssey.MOD_ID,"clover_stone"), List.of(STERLING_SILVER), List.of(Tiers.DIAMOND));

        TierSortingRegistry.registerTier(WOOD, new ResourceLocation(Odyssey.MOD_ID,"wood"), List.of(Tiers.WOOD), List.of(Tiers.STONE));
        TierSortingRegistry.registerTier(STONE, new ResourceLocation(Odyssey.MOD_ID,"stone"), List.of(Tiers.STONE), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(IRON, new ResourceLocation(Odyssey.MOD_ID,"iron"), List.of(Tiers.IRON), List.of(STERLING_SILVER));
        TierSortingRegistry.registerTier(GOLD, new ResourceLocation(Odyssey.MOD_ID,"gold"), List.of(IRON), List.of(STERLING_SILVER));
        TierSortingRegistry.registerTier(DIAMOND, new ResourceLocation(Odyssey.MOD_ID,"diamond"), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));
        TierSortingRegistry.registerTier(NETHERITE, new ResourceLocation(Odyssey.MOD_ID,"netherite"), List.of(Tiers.NETHERITE), List.of());
    }
}