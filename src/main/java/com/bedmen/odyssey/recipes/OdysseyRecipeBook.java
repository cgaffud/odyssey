package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.RecipeBookManager;

import java.util.List;

public class OdysseyRecipeBook {

    // todo add recipe book gui to crafting stations with guis
    public static final RecipeBookCategories ALLOYING_SEARCH = RecipeBookCategories.create("ALLOYING_SEARCH", new ItemStack(Items.COMPASS));
    public static final RecipeBookCategories ALLOYING = RecipeBookCategories.create("ALLOYING", new ItemStack(ItemRegistry.STERLING_SILVER_INGOT.get()));
    public static final RecipeBookCategories RECYCLING_SEARCH = RecipeBookCategories.create("RECYCLING_SEARCH", new ItemStack(Items.COMPASS));
    public static final RecipeBookCategories RECYCLING = RecipeBookCategories.create("RECYCLING", new ItemStack(ItemRegistry.COPPER_NUGGET.get()));
    public static final RecipeBookCategories WEAVING = RecipeBookCategories.create("WEAVING", new ItemStack(ItemRegistry.COPPER_COBWEB.get()));
    public static final RecipeBookCategories STITCHING_SEARCH = RecipeBookCategories.create("STITCHING_SEARCH", new ItemStack(Items.COMPASS));
    public static final RecipeBookCategories STITCHING = RecipeBookCategories.create("STITCHING", new ItemStack(ItemRegistry.THORNMAIL.get()));
    public static final RecipeBookCategories INFUSER_CRAFTING = RecipeBookCategories.create("INFUSER_CRAFTING", new ItemStack(ItemRegistry.AERIAL_FABRIC.get()));
}
