package com.bedmen.odyssey.plugins.jei;

import com.bedmen.odyssey.recipes.*;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.*;

import java.util.List;

public final class OdysseyRecipes {
    private final RecipeManager recipeManager;

    public OdysseyRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        this.recipeManager = world.getRecipeManager();
    }

    public List<AlloyRecipe> getAlloyingRecipes() {
        return getRecipes(recipeManager, RecipeTypeRegistry.ALLOYING.get());
    }

    public List<StitchingRecipe> getStitchingRecipes() {
        return getRecipes(recipeManager, RecipeTypeRegistry.STITCHING.get());
    }

    public List<WeavingRecipe> getWeavingRecipes() {
        return getRecipes(recipeManager, RecipeTypeRegistry.WEAVING.get());
    }

    public List<RecyclingRecipe> getRecyclingRecipes() {
        return getRecipes(recipeManager, RecipeTypeRegistry.RECYCLING.get());
    }

    @SuppressWarnings("unchecked")
    private static <C extends Container, T extends Recipe<C>> List<T> getRecipes(
            RecipeManager recipeManager,
            RecipeType<T> recipeType
    ) {
        return recipeManager.getAllRecipesFor(recipeType);
    }
}
