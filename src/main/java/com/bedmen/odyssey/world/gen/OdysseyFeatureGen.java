package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.FeatureRegistry;
import com.bedmen.odyssey.world.gen.feature.tree.LeaningTrunkPlacer;
import com.bedmen.odyssey.world.gen.feature.tree.PalmFoliagePlacer;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OdysseyFeatureGen {

    // Single Generating Features
    public static ConfiguredFeature<?, ?> PERMAFROST_TOWER;
    public static ConfiguredFeature<?, ?> FOG;
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> AUTUMN_TREE_RED;
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> AUTUMN_TREE_ORANGE;
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> AUTUMN_TREE_YELLOW;
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> PALM_TREE;

    // Groups
    public static ConfiguredFeature<?,?> AUTUMN_FOREST;
    public static ConfiguredFeature<?,?> PALM_TREES;

    public static void registerFeatures() {
        PERMAFROST_TOWER = featureGen(FeatureRegistry.PERMAFROST_TOWER);

        FOG = FeatureRegistry.FOG.get().configured(IFeatureConfig.NONE);

        AUTUMN_TREE_RED =  Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.BIRCH_LOG.defaultBlockState()), new SimpleBlockStateProvider(BlockRegistry.AUTUMN_LEAVES_RED.get().defaultBlockState()), new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3), new StraightTrunkPlacer(6, 3, 0), new TwoLayerFeature(1, 0, 1))).ignoreVines().build());
        AUTUMN_TREE_ORANGE = Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.BIRCH_LOG.defaultBlockState()), new SimpleBlockStateProvider(BlockRegistry.AUTUMN_LEAVES_ORANGE.get().defaultBlockState()), new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3), new StraightTrunkPlacer(6, 2, 0), new TwoLayerFeature(1, 0, 1))).ignoreVines().build());
        AUTUMN_TREE_YELLOW = Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.BIRCH_LOG.defaultBlockState()), new SimpleBlockStateProvider(BlockRegistry.AUTUMN_LEAVES_YELLOW.get().defaultBlockState()), new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3), new StraightTrunkPlacer(6, 1, 0), new TwoLayerFeature(1, 0, 1))).ignoreVines().build());
        AUTUMN_FOREST = Feature.RANDOM_SELECTOR.configured(new MultipleRandomFeatureConfig(ImmutableList.of(Features.BIRCH.weighted(0.05F), AUTUMN_TREE_YELLOW.weighted(0.05F), AUTUMN_TREE_RED.weighted(0.5F)), AUTUMN_TREE_ORANGE)).decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(50, 0.1F, 1)));

        PALM_TREE = FeatureRegistry.DIAGONAL_TREE.get().configured((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(BlockRegistry.PALM_LOG.get().defaultBlockState()), new SimpleBlockStateProvider(BlockRegistry.PALM_LEAVES.get().defaultBlockState()), new PalmFoliagePlacer(FeatureSpread.fixed(5), FeatureSpread.fixed(0)), new LeaningTrunkPlacer(6, 1, 0), new TwoLayerFeature(1, 0, 1))).ignoreVines().build());
        PALM_TREES = Feature.RANDOM_SELECTOR.configured(new MultipleRandomFeatureConfig(ImmutableList.of(PALM_TREE.weighted(1.0F)), PALM_TREE)).decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(1, 0.1F, 1)));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void generation(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();

        if(event.getCategory() == Biome.Category.NETHER) {

        } else if (event.getCategory() == Biome.Category.THEEND) {

        } else {
            gen.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, FOG);
            if(event.getName().toString().equals("minecraft:ice_spikes")) {
                gen.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PERMAFROST_TOWER);
            }
            if(event.getName().toString().equals("oddc:autumn_forest")) {
                gen.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AUTUMN_FOREST);
            }
            if(event.getName().toString().equals("oddc:tropics")) {
                gen.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PALM_TREES);
            }
            if(event.getName().toString().equals("oddc:tropical_beach")) {
                gen.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PALM_TREES);
            }
        }
    }

    private static ConfiguredFeature<?, ?> featureGen(RegistryObject<Feature<NoFeatureConfig>> feature){
        return feature.get().configured(IFeatureConfig.NONE).decorated(Features.Placements.HEIGHTMAP_SQUARE).count(1);
    }

}