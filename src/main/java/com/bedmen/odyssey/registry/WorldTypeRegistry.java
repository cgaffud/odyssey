package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.OdysseyGeneration;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WorldTypeRegistry {

    public static DeferredRegister<WorldPreset> WORLD_TYPE = DeferredRegister.create(Registry.WORLD_PRESET_REGISTRY, Odyssey.MOD_ID);
    public static final ResourceKey<NoiseGeneratorSettings> ODYSSEY_RESOURCE_KEY = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation(Odyssey.MOD_ID));

    public static void init() {
        WORLD_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<WorldPreset> ODYSSEY_WORLD_TYPE = WORLD_TYPE.register(Odyssey.MOD_ID, OdysseyGeneration::createPresetWithOdysseyOverworld);
}