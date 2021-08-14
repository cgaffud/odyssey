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
        RegistryKey<Biome> registryKey = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Odyssey.MOD_ID, "autumn_forest"));
        net.minecraftforge.common.BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(registryKey, 10));
    }

    public static final RegistryObject<Biome> AUTUMN_FOREST = BIOMES.register("autumn_forest", () -> OdysseyBiomeMaker.autumnForestBiome(0.2F, 0.3F));
}
