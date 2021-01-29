package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.recipes.AlloyRecipe;
import com.bedmen.odyssey.recipes.InfusingRecipe;
import com.bedmen.odyssey.recipes.NewSmithingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeRegistry {

    public static DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Odyssey.MOD_ID);

    public static void init() {
        RECIPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<IRecipeSerializer<NewSmithingRecipe>> NEW_SMITHING = RECIPES.register("new_smithing", () -> new NewSmithingRecipe.Serializer());
    public static final RegistryObject<IRecipeSerializer<AlloyRecipe>> ALLOYING = RECIPES.register("alloying", () -> new AlloyRecipe.Serializer());
    public static final RegistryObject<IRecipeSerializer<InfusingRecipe>> INFUSING = RECIPES.register("infusing", () -> new InfusingRecipe.Serializer());
}