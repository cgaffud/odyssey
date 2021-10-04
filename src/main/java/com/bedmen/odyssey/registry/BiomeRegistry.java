package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.OdysseyBiomeMaker;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

public class BiomeRegistry {

    public static DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES , Odyssey.MOD_ID);

    public static void init() {
        BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Biome> AUTUMN_FOREST = BIOMES.register("autumn_forest", () -> OdysseyBiomeMaker.autumnForestBiome(0.0F, 0.0F));
    public static final RegistryObject<Biome> TROPICS = BIOMES.register("tropics", OdysseyBiomeMaker::tropicsBeachBiome);
    public static final RegistryObject<Biome> TROPICAL_BEACH = BIOMES.register("tropical_beach", OdysseyBiomeMaker::tropicsBiome);

    public static final RegistryKey<Biome> AUTUMN_FOREST_REGISTRY_KEY = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, "autumn_forest"));

    public static void register()
    {
        registerBiome("autumn_forest", BiomeManager.BiomeType.COOL, 10, FOREST, OVERWORLD);
        registerBiome("tropics", BiomeManager.BiomeType.DESERT, 0, OVERWORLD);
        registerBiome("tropical_beach", BiomeManager.BiomeType.DESERT, 0, OVERWORLD);
    }

    private static void registerBiome(String biomeName, BiomeManager.BiomeType type, int weight,  BiomeDictionary.Type... types) {
        RegistryKey<Biome> registryKey = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, biomeName));
        BiomeManager.addAdditionalOverworldBiomes(registryKey);
        BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(registryKey, weight));
        BiomeDictionary.addTypes(registryKey, types);
    }
}
