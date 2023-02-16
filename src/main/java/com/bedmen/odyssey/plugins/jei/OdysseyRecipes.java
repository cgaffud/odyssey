package com.bedmen.odyssey.plugins.jei;

import com.bedmen.odyssey.recipes.*;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public final class OdysseyRecipes {
    public final RecipeManager recipeManager;

    public OdysseyRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        this.recipeManager = world.getRecipeManager();
    }

    public <C extends Container, T extends Recipe<C>> List<T> getRecipes(RecipeType<T> recipeType){
        return getRecipes(this.recipeManager, recipeType);
    }

    @SuppressWarnings("unchecked")
    private static <C extends Container, T extends Recipe<C>> List<T> getRecipes(
            RecipeManager recipeManager,
            RecipeType<T> recipeType
    ) {
        return recipeManager.getAllRecipesFor(recipeType);
    }
}
