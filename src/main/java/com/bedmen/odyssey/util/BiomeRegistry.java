package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.ModBiomeMaker;
import net.minecraft.data.BiomeProvider;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeRegistry {

    public static DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES , Odyssey.MOD_ID);
    public static DeferredRegister<Biome> BIOMES_VANILLA = DeferredRegister.create(ForgeRegistries.BIOMES , "minecraft");

    public static void init() {
        BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
        BIOMES_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Biome> AUTUMN_FOREST = BIOMES.register("autumn_forest", () -> ModBiomeMaker.autumnForestBiome(0.1F, 0.2F));
}
