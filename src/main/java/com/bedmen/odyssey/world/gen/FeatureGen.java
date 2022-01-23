package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.registry.BiomeRegistry;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.FeatureRegistry;
import com.bedmen.odyssey.world.gen.feature.tree.GreatFoliagePlacer;
import com.bedmen.odyssey.world.gen.feature.tree.GreatTrunkPlacer;
import com.bedmen.odyssey.world.gen.feature.tree.LeaningTrunkPlacer;
import com.bedmen.odyssey.world.gen.feature.tree.PalmFoliagePlacer;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
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

//    public static ConfiguredFeature<?, ?> PERMAFROST_TOWER;
//    public static ConfiguredFeature<?, ?> FOG;
    public static ConfiguredFeature<RandomPatchConfiguration, ?> PATCH_PRAIRIE_GRASS;
    public static PlacedFeature PLACED_PATCH_TALL_GRASS;
    public static PlacedFeature PLACED_PATCH_PRAIRIE_GRASS;
    public static Map<TreasureChestMaterial, ConfiguredFeature<?, ?>> TREASURE_CHESTS = new HashMap<>();
    public static Map<TreasureChestMaterial, PlacedFeature> PLACED_TREASURE_CHESTS = new HashMap<>();
    public static ConfiguredFeature<?, ?> ABANDONED_IRON_GOLEM;
    public static PlacedFeature PLACED_ABANDONED_IRON_GOLEM;

    public static void registerFeatures() {
//        PERMAFROST_TOWER = featureGen(FeatureRegistry.PERMAFROST_TOWER, 1);
//        FOG = FeatureRegistry.FOG.get().configured(FeatureConfiguration.NONE);

        PATCH_PRAIRIE_GRASS = FeatureUtils.register("patch_prairie_grass", Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(FeatureRegistry.TRIPLE_PLANT_BLOCK.get().configured(new SimpleBlockConfiguration(BlockStateProvider.simple(BlockRegistry.PRAIRIE_GRASS.get()))))));
        PLACED_PATCH_TALL_GRASS = PlacementUtils.register("placed_patch_tall_grass", VegetationFeatures.PATCH_TALL_GRASS.placed(NoiseThresholdCountPlacement.of(-0.8D, 5, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        PLACED_PATCH_PRAIRIE_GRASS = PlacementUtils.register("placed_patch_prairie_grass", PATCH_PRAIRIE_GRASS.placed(NoiseThresholdCountPlacement.of(-0.8D, 5, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        for(TreasureChestMaterial treasureChestMaterial : TreasureChestMaterial.values()){
            TREASURE_CHESTS.put(treasureChestMaterial, FeatureRegistry.TREASURE_CHEST_MAP.get(treasureChestMaterial).configured(FeatureConfiguration.NONE));
            PLACED_TREASURE_CHESTS.put(treasureChestMaterial, PlacementUtils.register(treasureChestMaterial.name().toLowerCase(Locale.ROOT) + "_placed_treasure_chest", TREASURE_CHESTS.get(treasureChestMaterial).placed(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
        }
        ABANDONED_IRON_GOLEM = FeatureRegistry.ABANDONED_IRON_GOLEM.get().configured(FeatureConfiguration.NONE);
        PLACED_ABANDONED_IRON_GOLEM = PlacementUtils.register("placed_abandoned_iron_golem", ABANDONED_IRON_GOLEM.placed(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void generation(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();

        if(event.getCategory() == Biome.BiomeCategory.NETHER) {

        } else if (event.getCategory() == Biome.BiomeCategory.THEEND) {

        } else {
            for(TreasureChestMaterial treasureChestMaterial : TreasureChestMaterial.values()){
                gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, PLACED_TREASURE_CHESTS.get(treasureChestMaterial));
            }
            gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PLACED_ABANDONED_IRON_GOLEM);

            if(event.getName().equals(BiomeRegistry.PRAIRIE.get().getRegistryName())) {
                gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_PATCH_TALL_GRASS);
                gen.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_PATCH_PRAIRIE_GRASS);
            }
//            gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, FOG);
//            if(event.getName().toString().equals("minecraft:ice_spikes")) {
//                gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PERMAFROST_TOWER);
//            }
        }
    }
}