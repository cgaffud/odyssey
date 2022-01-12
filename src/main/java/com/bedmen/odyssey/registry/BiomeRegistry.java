package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.biome.OdysseyOverworldBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraftforge.common.BiomeDictionary.Type.*;
import static net.minecraftforge.common.BiomeDictionary.Type.SANDY;

public class BiomeRegistry {

    public static DeferredRegister<Biome> BIOME = DeferredRegister.create(ForgeRegistries.BIOMES, Odyssey.MOD_ID);

    public static void init() {
        BIOME.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final ResourceKey<Biome> TROPICS_RESOURCE_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, "tropics"));
    public static final RegistryObject<Biome> TROPICS = BIOME.register("tropics", OdysseyOverworldBiomes::tropics);

    public static void register()
    {
        //registerBiome("autumn_forest", BiomeManager.BiomeType.COOL, 10, FOREST, OVERWORLD);
        registerBiome(TROPICS_RESOURCE_KEY, BiomeManager.BiomeType.DESERT, 10, OVERWORLD, HOT, WET, SANDY, OVERWORLD);
    }

    private static void registerBiome(ResourceKey<Biome> resourceKey, BiomeManager.BiomeType type, int weight, BiomeDictionary.Type... types) {
        //BiomeManager.addAdditionalOverworldBiomes(registryKey);
//        BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(resourceKey, weight));
//        BiomeDictionary.addTypes(resourceKey, types);
    }
}