package com.bedmen.odyssey.recipes;

import java.util.Optional;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;

public interface ModRecipeType<T extends IRecipe<?>> {
    IRecipeType<AlloyRecipe> ALLOYING = register("alloying");
    IRecipeType<NewSmithingRecipe> NEW_SMITHING = register("new_smithing");
    IRecipeType<InfusingRecipe> INFUSING = register("infusing");
    IRecipeType<EnchantedBookInfusingRecipe> ENCHANTED_BOOK_INFUSING = register("enchanted_book_infusing");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
        return IRecipeType.register(key);
    }

    default <C extends IInventory> Optional<T> matches(IRecipe<C> recipe, World worldIn, C inv) {
        return recipe.matches(inv, worldIn) ? Optional.of((T)recipe) : Optional.empty();
    }
}