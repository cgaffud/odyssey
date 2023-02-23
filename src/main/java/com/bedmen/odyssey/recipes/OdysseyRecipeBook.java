package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.RecipeBookRegistry;

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

    public static void init(){
        // Add aggregate categories
        RecipeBookRegistry.addAggregateCategories(ALLOYING_SEARCH, List.of(ALLOYING));
        RecipeBookRegistry.addAggregateCategories(RECYCLING_SEARCH, List.of(RECYCLING));
        RecipeBookRegistry.addAggregateCategories(STITCHING_SEARCH, List.of(STITCHING));
        // Add category finders
        RecipeBookRegistry.addCategoriesFinder(RecipeTypeRegistry.ALLOYING.get(), recipe -> ALLOYING);
        RecipeBookRegistry.addCategoriesFinder(RecipeTypeRegistry.RECYCLING.get(), recipe -> RECYCLING);
        RecipeBookRegistry.addCategoriesFinder(RecipeTypeRegistry.WEAVING.get(), recipe -> WEAVING);
        RecipeBookRegistry.addCategoriesFinder(RecipeTypeRegistry.STITCHING.get(), recipe -> STITCHING);
        RecipeBookRegistry.addCategoriesFinder(RecipeTypeRegistry.INFUSER_CRAFTING.get(), recipe -> INFUSER_CRAFTING);
    }
}
