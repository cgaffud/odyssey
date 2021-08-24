package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.OdysseyBiomeMaker;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeRegistry {

    public static DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES , Odyssey.MOD_ID);

    public static void init() {
        BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void register()
    {
        registerBiome("autumn_forest", BiomeManager.BiomeType.COOL, 10);
        registerBiome("palm_beach", BiomeManager.BiomeType.WARM, 10);
    }

    private static void registerBiome(String biomeName, BiomeManager.BiomeType type, int weight) {
        RegistryKey<Biome> registryKey = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, biomeName));
        BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(registryKey, weight));
    }

    public static final RegistryObject<Biome> AUTUMN_FOREST = BIOMES.register("autumn_forest", () -> OdysseyBiomeMaker.autumnForestBiome(0.0F, 0.0F));
    public static final RegistryObject<Biome> PALM_BEACH = BIOMES.register("palm_beach", () -> OdysseyBiomeMaker.tropicsBiome());
}
