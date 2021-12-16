package com.bedmen.odyssey.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface OdysseyRecipeType<T extends Recipe<?>> {
    RecipeType<AlloyRecipe> ALLOYING = register("alloying");
//    RecipeType<OdysseySmithingRecipe> ODYSSEY_SMITHING = register("odyssey_smithing");
//    RecipeType<RecycleRecipe> RECYCLING = register("recycling");
//    RecipeType<ResearchRecipe> RESEARCH = register("research");

    static <T extends Recipe<?>> RecipeType<T> register(final String key) {
        return RecipeType.register(key);
    }

    default <C extends Container> Optional<T> matches(Recipe<C> recipe, Level worldIn, C inv) {
        return recipe.matches(inv, worldIn) ? Optional.of((T)recipe) : Optional.empty();
    }
}