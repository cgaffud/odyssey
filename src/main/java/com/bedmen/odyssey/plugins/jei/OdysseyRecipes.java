package com.bedmen.odyssey.plugins.jei;

import com.bedmen.odyssey.recipes.AlloyRecipe;
import com.bedmen.odyssey.recipes.OdysseyRecipeType;
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
        return getRecipes(recipeManager, OdysseyRecipeType.ALLOYING);
    }

    @SuppressWarnings("unchecked")
    private static <C extends Container, T extends Recipe<C>> List<T> getRecipes(
            RecipeManager recipeManager,
            RecipeType<T> recipeType
    ) {
        return recipeManager.getAllRecipesFor(recipeType);
    }
}
