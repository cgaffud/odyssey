package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.feature.MegaIceSpikeFeature;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleTypeRegistry {

    public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES , Odyssey.MOD_ID);
    public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES_VANILLA = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES , "minecraft");

    public static void init() {
        PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLE_TYPES_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //public static final RegistryObject<ParticleType<BasicParticleType>> PERMAFROST_ICICLE = PARTICLE_TYPES.register("permafrost_icicle", () -> new BasicParticleType(false));
}