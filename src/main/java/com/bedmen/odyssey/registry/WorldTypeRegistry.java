package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.OdysseyGeneration;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraftforge.common.world.ForgeWorldPreset;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WorldTypeRegistry {

    public static DeferredRegister<ForgeWorldPreset> WORLD_TYPE = DeferredRegister.create(ForgeRegistries.Keys.WORLD_TYPES, Odyssey.MOD_ID);
    public static final ResourceKey<NoiseGeneratorSettings> ODYSSEY_RESOURCE_KEY = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation(Odyssey.MOD_ID));

    public static void init() {
        WORLD_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<ForgeWorldPreset> ODYSSEY_WORLD_TYPE = WORLD_TYPE.register(Odyssey.MOD_ID, () -> new ForgeWorldPreset(new OdysseyGeneration.OdysseyChunkGeneratorFactory()));
}