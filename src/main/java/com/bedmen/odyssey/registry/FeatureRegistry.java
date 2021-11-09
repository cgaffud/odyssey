package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.feature.AbandonedIronGolemFeature;
import com.bedmen.odyssey.world.gen.feature.FogFeature;
import com.bedmen.odyssey.world.gen.feature.PermafrostTowerFeature;
import com.bedmen.odyssey.world.gen.feature.tree.PalmTreeFeature;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FeatureRegistry {

    public static DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES , Odyssey.MOD_ID);
    public static DeferredRegister<Feature<?>> FEATURES_VANILLA = DeferredRegister.create(ForgeRegistries.FEATURES , "minecraft");

    public static void init() {
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        FEATURES_VANILLA.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Feature<NoFeatureConfig>> PERMAFROST_TOWER = FEATURES.register("permafrost_tower", () -> new PermafrostTowerFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> FOG = FEATURES.register("fog", () -> new FogFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> ABANDONED_IRON_GOLEM = FEATURES.register("abandoned_iron_golem", () -> new AbandonedIronGolemFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<Feature<BaseTreeFeatureConfig>> DIAGONAL_TREE = FEATURES.register("diagonal_tree", () -> new PalmTreeFeature(BaseTreeFeatureConfig.CODEC));
}