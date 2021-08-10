package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.recipes.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShieldRecipes;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeRegistry {

    public static DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Odyssey.MOD_ID);

    public static void init() {
        RECIPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<IRecipeSerializer<OdysseySmithingRecipe>> ODYSSEY_SMITHING = RECIPES.register("odyssey_smithing", OdysseySmithingRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<AlloyRecipe>> ALLOYING = RECIPES.register("alloying", AlloyRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<RecycleRecipe>> RECYCLING = RECIPES.register("recycling", RecycleRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<ResearchRecipe>> RESEARCH = RECIPES.register("research", ResearchRecipe.Serializer::new);
}