package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.ModBiomeMaker;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.data.BiomeProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeRegistry {

    //public static DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES , Odyssey.MOD_ID);
    //public static DeferredRegister<Biome> BIOMES_VANILLA = DeferredRegister.create(ForgeRegistries.BIOMES , "minecraft");
    private static final Int2ObjectMap<RegistryKey<Biome>> TO_NAME = new Int2ObjectArrayMap<>();

    public static void init() {

        register(AUTUMN_FOREST, Type.FOREST, Type.HILLS, Type.COLD);

       // BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
       // BIOMES_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final Biome AUTUMN_FOREST = ModBiomeMaker.autumnForestBiome(0.1F, 0.2F);

    private static void register(Biome biome, Type... types){
        ForgeRegistries.BIOMES.register(biome);

        RegistryKey<Biome> key = RegistryKey.create(Registry.BIOME_REGISTRY, ForgeRegistries.BIOMES.getKey(biome));
        if (key == null)
            System.out.println("bad key");
        else
            System.out.println(key.toString());

        BiomeDictionary.addTypes(key, types);
        BiomeManager.addAdditionalOverworldBiomes(key);
    }


    //private static RegistryKey<Biome> createKey(String p_242548_0_) {
    //    return RegistryKey.create(, new ResourceLocation(p_242548_0_));
    //}

    /*public static final RegistryObject<Biome> AUTUMN_FOREST = BIOMES.register("autumn_forest", () -> ModBiomeMaker.autumnForestBiome(0.1F, 0.2F));

    public static void registerBiomes() {
        RegistryKey.create()
        registerBiome(AUTUMN_FOREST.getId());
    }

    private static void registerBiome(Biome biome, Type... types) {
        BiomeDictionary.addTypes(biome, types);
        BiomeManager.addAdditionalOverworldBiomes(biome);

    }*/
}
