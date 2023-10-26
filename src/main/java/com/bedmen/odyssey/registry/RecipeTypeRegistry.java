package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.recipes.object.*;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypeRegistry {

    public static DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, Odyssey.MOD_ID);

    public static void init() {
        RECIPE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<RecipeType<RecyclingRecipe>> RECYCLING = RECIPE_TYPES.register("recycling", () -> new RecipeType<>() {
        public String toString() {
            return "recycling";
        }
    });

    public static final RegistryObject<RecipeType<WeavingRecipe>> WEAVING = RECIPE_TYPES.register("weaving", () -> new RecipeType<>() {
        public String toString() {
            return "weaving";
        }
    });

    public static final RegistryObject<RecipeType<StitchingRecipe>> STITCHING = RECIPE_TYPES.register("stitching", () -> new RecipeType<>() {
        public String toString() {
            return "stitching";
        }
    });

    public static final RegistryObject<RecipeType<AlloyRecipe>> ALLOYING = RECIPE_TYPES.register("alloying", () -> new RecipeType<>() {
        public String toString() {
            return "alloying";
        }
    });

}