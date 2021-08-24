package com.bedmen.odyssey.util;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.feature.FogFeature;
import com.bedmen.odyssey.world.gen.feature.MegaIceSpikeFeature;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FeatureRegistry {

    public static DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Odyssey.MOD_ID);
    public static DeferredRegister<Feature<?>> FEATURES_VANILLA = DeferredRegister.create(ForgeRegistries.FEATURES, "minecraft");

    public static void init() {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        FEATURES_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Feature<NoFeatureConfig>> MEGA_ICE_SPIKE = FEATURES.register("mega_ice_spike", () -> new MegaIceSpikeFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> FOG = FEATURES.register("fog", () -> new FogFeature(NoFeatureConfig.CODEC));

}