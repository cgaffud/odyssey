package com.bedmen.odyssey.registry;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.structure.UndergroundRuin;
import com.bedmen.odyssey.world.gen.structure.WeaverColonyFeature;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class StructureFeatureRegistry {

    public static DeferredRegister<StructureFeature<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES , Odyssey.MOD_ID);

    public static void init() {
        STRUCTURE_FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> WEAVER_COLONY = STRUCTURE_FEATURES.register("weaver_colony", () -> new WeaverColonyFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> UNDERGROUND_RUIN = STRUCTURE_FEATURES.register("underground_ruin", () -> new UndergroundRuin(NoneFeatureConfiguration.CODEC));

    public static void setupStructures() {
        setupMapSpacingAndLand(
                WEAVER_COLONY.get(),
                new StructureFeatureConfiguration(16 /* average distance apart in chunks between spawn attempts */,
                        8 /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE*/,
                        24357620 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                false);
        setupMapSpacingAndLand(
                UNDERGROUND_RUIN.get(),
                new StructureFeatureConfiguration(16, 8,97344289), true
        );
    }

    /**
     * Adds the provided structure to the registry, and adds the separation settings.
     * The rarity of the structure is determined based on the values passed into
     * this method in the StructureFeatureConfiguration argument.
     * This method is called by setupStructures above.
     */
    public static <F extends StructureFeature<?>> void setupMapSpacingAndLand(
            F structure,
            StructureFeatureConfiguration structureFeatureConfiguration,
            boolean transformSurroundingLand)
    {

        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if(transformSurroundingLand){
            StructureFeature.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<StructureFeature<?>>builder()
                            .addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        StructureSettings.DEFAULTS =
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                        .putAll(StructureSettings.DEFAULTS)
                        .put(structure, structureFeatureConfiguration)
                        .build();

        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();
            if(structureMap instanceof ImmutableMap){
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureFeatureConfiguration);
                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else{
                structureMap.put(structure, structureFeatureConfiguration);
            }
        });
    }
}