package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.FeatureRegistry;
import com.bedmen.odyssey.registry.StructureFeatureRegistry;
import com.bedmen.odyssey.world.gen.feature.tree.LeaningTrunkPlacer;
import com.bedmen.odyssey.world.gen.feature.tree.PalmFoliagePlacer;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StructureGen {

    public static ConfiguredStructureFeature<?, ?> WEAVER_COLONY;

    public static void registerStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;

        WEAVER_COLONY = structureGen(StructureFeatureRegistry.WEAVER_COLONY);
        Registry.register(registry, new ResourceLocation(Odyssey.MOD_ID, "configured_weaver_colony"), WEAVER_COLONY);
        FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(StructureFeatureRegistry.WEAVER_COLONY.get(), StructureGen.WEAVER_COLONY);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void generation(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();

        if(event.getCategory() == Biome.BiomeCategory.NETHER) {

        } else if (event.getCategory() == Biome.BiomeCategory.THEEND) {

        } else {
            gen.addStructureStart(WEAVER_COLONY);
            event.getGeneration().getStructures().add(() -> WEAVER_COLONY);
//            gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ABANDONED_IRON_GOLEM);
//            gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, FOG);
//            if(event.getName().toString().equals("minecraft:ice_spikes")) {
//                gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PERMAFROST_TOWER);
//            }
//            if(event.getName().toString().equals("oddc:autumn_forest")) {
//                gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AUTUMN_FOREST);
//            }
//            if(event.getName().toString().equals("oddc:tropics")) {
//                gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PALM_TREES);
//            }
//            if(event.getName().toString().equals("oddc:tropical_beach")) {
//                gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PALM_TREES);
//            }
        }
    }

    private static ConfiguredStructureFeature<?, ?> structureGen(RegistryObject<StructureFeature<NoneFeatureConfiguration>> structure){
        return structure.get().configured(NoneFeatureConfiguration.INSTANCE);
    }
}