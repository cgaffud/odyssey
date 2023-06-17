package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.lock.TreasureChestType;
import com.bedmen.odyssey.registry.BiomeRegistry;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.FeatureRegistry;
import com.bedmen.odyssey.world.BiomeUtil;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FeatureGen {

    public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_PRAIRIE_GRASS;
    public static Holder<PlacedFeature> PLACED_PATCH_TALL_GRASS;
    public static Holder<PlacedFeature> PLACED_PATCH_PRAIRIE_GRASS;
    public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_CLOVER;
    public static Holder<PlacedFeature> PLACED_PATCH_CLOVER;

    public static Map<TreasureChestType, Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>>> TREASURE_CHESTS = new HashMap<>();
    public static Map<TreasureChestType, Holder<PlacedFeature>> PLACED_TREASURE_CHESTS = new HashMap<>();
    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> ABANDONED_IRON_GOLEM;
    public static Holder<PlacedFeature> PLACED_ABANDONED_IRON_GOLEM;

    public static void registerFeatures() {
        PATCH_PRAIRIE_GRASS = FeatureUtils.register("patch_prairie_grass", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(FeatureRegistry.TRIPLE_PLANT_BLOCK.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.PRAIRIE_GRASS.get()))));
        PLACED_PATCH_TALL_GRASS = PlacementUtils.register("placed_patch_tall_grass", VegetationFeatures.PATCH_TALL_GRASS, NoiseThresholdCountPlacement.of(-0.8D, 5, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
        PLACED_PATCH_PRAIRIE_GRASS = PlacementUtils.register("placed_patch_prairie_grass", PATCH_PRAIRIE_GRASS, NoiseThresholdCountPlacement.of(-0.8D, 5, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
        PATCH_CLOVER = FeatureUtils.register("patch_clover", Feature.RANDOM_PATCH, grassPatch(BlockStateProvider.simple(BlockRegistry.CLOVER.get()), 1));
        PLACED_PATCH_CLOVER = PlacementUtils.register("placed_patch_clover", PATCH_CLOVER, RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());


        for(TreasureChestType treasureChestType : TreasureChestType.values()){
            TREASURE_CHESTS.put(treasureChestType, FeatureUtils.register(treasureChestType.name().toLowerCase(Locale.ROOT) + "_treasure_chest", FeatureRegistry.TREASURE_CHEST_MAP.get(treasureChestType), FeatureConfiguration.NONE));
            PLACED_TREASURE_CHESTS.put(treasureChestType, PlacementUtils.register("placed_" + treasureChestType.name().toLowerCase(Locale.ROOT) + "_treasure_chest", TREASURE_CHESTS.get(treasureChestType), RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        }
        ABANDONED_IRON_GOLEM = FeatureUtils.register("abandoned_iron_golem", FeatureRegistry.ABANDONED_IRON_GOLEM.get(), FeatureConfiguration.NONE);
        PLACED_ABANDONED_IRON_GOLEM = PlacementUtils.register("placed_abandoned_iron_golem", ABANDONED_IRON_GOLEM, InSquarePlacement.spread(), CountPlacement.of(1), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void generation(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();
        Biome.BiomeCategory biomeCategory = event.getCategory();
        Biome.ClimateSettings climateSettings = event.getClimate();

        if(biomeCategory == Biome.BiomeCategory.NETHER) {

        } else if (biomeCategory == Biome.BiomeCategory.THEEND) {

        } else {
            for(TreasureChestType treasureChestType : TreasureChestType.values()){
                gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, PLACED_TREASURE_CHESTS.get(treasureChestType));
            }
            gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PLACED_ABANDONED_IRON_GOLEM);

            if(BiomeUtil.isBiome(event.getName(), BiomeRegistry.PRAIRIE.get())) {
                gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_PATCH_TALL_GRASS);
                gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_PATCH_PRAIRIE_GRASS);
            }

            if(BiomeUtil.hasGoodPlantClimate(biomeCategory, climateSettings.precipitation, climateSettings.temperature)){
                gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_PATCH_CLOVER);
            }
        }
    }

    private static RandomPatchConfiguration grassPatch(BlockStateProvider blockStateProvider, int count) {
        return FeatureUtils.simpleRandomPatchConfiguration(count, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(blockStateProvider)));
    }
}