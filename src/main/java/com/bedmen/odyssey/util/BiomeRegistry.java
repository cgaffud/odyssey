package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.ModBiomeMaker;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeRegistry {

    public static void init() {
        register(AUTUMN_FOREST, "autumn_forest", BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.COLD);
    }
    public static final Biome AUTUMN_FOREST = ModBiomeMaker.autumnForestBiome(0.1F, 0.2F);

    private static void register(Biome biome, String name, BiomeDictionary.Type... types){
        biome.setRegistryName(Odyssey.MOD_ID, name);
        ForgeRegistries.BIOMES.register(biome);

        RegistryKey<Biome> key = RegistryKey.create(Registry.BIOME_REGISTRY, ForgeRegistries.BIOMES.getKey(biome));

        BiomeDictionary.addTypes(key, types);
        BiomeManager.addAdditionalOverworldBiomes(key);
    }

    public static ResourceLocation location(String name) {
        return new ResourceLocation(Odyssey.MOD_ID, name);
    }
}
