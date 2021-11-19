package com.bedmen.odyssey.tools;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class OdysseyTiers {

    //Heavy Melee Weapons
    public static final Tier COPPER = new ForgeTier(0, 250,2.0F, -1.0f, 0, BlockTags.NEEDS_STONE_TOOL, () -> Ingredient.of(Items.COPPER_INGOT));

    public static final Tag.Named<Block> STERLING_SILVER_TAG = BlockTags.createOptional(new ResourceLocation(Odyssey.MOD_ID, "needs_sterling_silver_tool"));
    public static final Tier STERLING_SILVER = new ForgeTier(2, 500,7.0F, 2.5f, 10, STERLING_SILVER_TAG, () -> Ingredient.of(ItemRegistry.SILVER_INGOT.get()));

    public static void init(){
        TierSortingRegistry.registerTier(STERLING_SILVER, new ResourceLocation("sterling_silver"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));
    }
}