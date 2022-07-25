package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.recipes.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializerRegistry {

    public static DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Odyssey.MOD_ID);

    public static void init() {
        RECIPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<RecipeSerializer<RecyclingRecipe>> RECYCLING = RECIPES.register("recycling", RecyclingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<WeavingRecipe>> WEAVING = RECIPES.register("weaving", WeavingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<StitchingRecipe>> STITCHING = RECIPES.register("stitching", StitchingRecipe.Serializer::new);
    public static final RegistryObject<SimpleRecipeSerializer<OdysseyShieldDecorationRecipes>> SHIELD_DECORATION = RECIPES.register("crafting_special_shielddecoration", () -> new SimpleRecipeSerializer<>(OdysseyShieldDecorationRecipes::new));
    public static final RegistryObject<RecipeSerializer<AlloyRecipe>> ALLOYING = RECIPES.register("alloying", AlloyRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<BottlingRecipe>> BOTTLING = RECIPES.register("bottling", BottlingRecipe.Serializer::new);
//    public static final RegistryObject<RecipeSerializer<ResearchRecipe>> RESEARCH = RECIPES.register("research", ResearchRecipe.Serializer::new);
//    public static final RegistryObject<RecipeSerializer<OdysseySmithingRecipe>> ODYSSEY_SMITHING = RECIPES.register("odyssey_smithing", OdysseySmithingRecipe.Serializer::new);
}