package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.structure.*;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class StructureFeatureRegistry {

    public static DeferredRegister<StructureFeature<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES , Odyssey.MOD_ID);

    public static void init() {
        STRUCTURE_FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> WEAVER_COLONY = STRUCTURE_FEATURES.register("weaver_colony", () -> new WeaverColonyFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> UNDERGROUND_RUIN = STRUCTURE_FEATURES.register("underground_ruin", () -> new UndergroundRuinFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> CLOVER_STONE_SANCTUARY = STRUCTURE_FEATURES.register("clover_stone_sanctuary", () -> new CloverStoneSanctuaryFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> BARN = STRUCTURE_FEATURES.register("barn", () -> new BarnFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> MOON_TOWER = STRUCTURE_FEATURES.register("moon_tower", () -> new MoonTowerFeature(NoneFeatureConfiguration.CODEC));
}