package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.blocks.NewBeaconBlock;
import com.bedmen.odyssey.world.ModBiomeMaker;
import net.minecraft.block.Block;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeRegistry {


    public static DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES , Odyssey.MOD_ID);

    public static void init() {
        //register(AUTUMN_FOREST, "autumn_forest", BiomeDictionary.Type.FOREST, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.COLD);
        BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static final RegistryObject<Biome> AUTUMN_FOREST = BIOMES.register("autumn_forest", () -> BiomeMaker.theVoidBiome());

    //public static final Biome AUTUMN_FOREST = ModBiomeMaker.autumnForestBiome(0.1F, 0.2F);

//    private static void register(Biome biome, String name, BiomeDictionary.Type... types){
//        biome.setRegistryName(Odyssey.MOD_ID, name);
//        ForgeRegistries.BIOMES.register(biome);
//
//        RegistryKey<Biome> key = RegistryKey.create(Registry.BIOME_REGISTRY, ForgeRegistries.BIOMES.getKey(biome));
//
//        BiomeDictionary.addTypes(key, types);
//        BiomeManager.addAdditionalOverworldBiomes(key);
//    }
//
//    public static ResourceLocation location(String name) {
//        return new ResourceLocation(Odyssey.MOD_ID, name);
//    }
}
