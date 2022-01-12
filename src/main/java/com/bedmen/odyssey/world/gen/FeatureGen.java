package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.FeatureRegistry;
import com.bedmen.odyssey.world.gen.feature.tree.GreatFoliagePlacer;
import com.bedmen.odyssey.world.gen.feature.tree.GreatTrunkPlacer;
import com.bedmen.odyssey.world.gen.feature.tree.LeaningTrunkPlacer;
import com.bedmen.odyssey.world.gen.feature.tree.PalmFoliagePlacer;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FeatureGen {

    // Single Generating Features
//    public static ConfiguredFeature<?, ?> PERMAFROST_TOWER;
//    public static ConfiguredFeature<?, ?> ABANDONED_IRON_GOLEM;
//    public static ConfiguredFeature<?, ?> FOG;
//    public static ConfiguredFeature<TreeConfiguration, ?> AUTUMN_TREE_RED;
//    public static ConfiguredFeature<TreeConfiguration, ?> AUTUMN_TREE_ORANGE;
//    public static ConfiguredFeature<TreeConfiguration, ?> AUTUMN_TREE_YELLOW;
    public static ConfiguredFeature<TreeConfiguration, ?> PALM_TREE;
    public static ConfiguredFeature<TreeConfiguration, ?> GREATWOOD_TREE;
    public static ConfiguredFeature<?, ?> TREASURE_CHEST;
    public static PlacedFeature PLACED_TREASURE_CHEST;

    // Groups
//    public static ConfiguredFeature<?,?> AUTUMN_FOREST;
//    public static ConfiguredFeature<?,?> PALM_TREES;

    public static void registerFeatures() {
//        PERMAFROST_TOWER = featureGen(FeatureRegistry.PERMAFROST_TOWER, 1);
//        ABANDONED_IRON_GOLEM = featureGen(FeatureRegistry.ABANDONED_IRON_GOLEM, 1);
//
//        FOG = FeatureRegistry.FOG.get().configured(FeatureConfiguration.NONE);
//
//        AUTUMN_TREE_RED =  Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(Blocks.BIRCH_LOG.defaultBlockState()), new SimpleStateProvider(BlockRegistry.AUTUMN_LEAVES_RED.get().defaultBlockState()), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(6, 3, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
//        AUTUMN_TREE_ORANGE = Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(Blocks.BIRCH_LOG.defaultBlockState()), new SimpleStateProvider(BlockRegistry.AUTUMN_LEAVES_ORANGE.get().defaultBlockState()), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(6, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
//        AUTUMN_TREE_YELLOW = Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(Blocks.BIRCH_LOG.defaultBlockState()), new SimpleStateProvider(BlockRegistry.AUTUMN_LEAVES_YELLOW.get().defaultBlockState()), new BlobFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0), 3), new StraightTrunkPlacer(6, 1, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
//        AUTUMN_FOREST = Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(Features.BIRCH.weighted(0.05F), AUTUMN_TREE_YELLOW.weighted(0.05F), AUTUMN_TREE_RED.weighted(0.5F)), AUTUMN_TREE_ORANGE)).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(50, 0.1F, 1)));
        PALM_TREE = FeatureRegistry.CORNER_LEAF_TREE.get().configured((new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.PALM_LOG.get()), new LeaningTrunkPlacer(6, 1, 0), BlockStateProvider.simple(BlockRegistry.PALM_LEAVES.get()), new PalmFoliagePlacer(ConstantInt.of(5), ConstantInt.of(0)), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build());
//        PALM_TREES = Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(PALM_TREE.weighted(1.0F)), PALM_TREE)).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(1, 0.1F, 1)));
        TREASURE_CHEST = FeatureRegistry.TREASURE_CHEST.get().configured(FeatureConfiguration.NONE);
        PLACED_TREASURE_CHEST = PlacementUtils.register("treasure_chest", TREASURE_CHEST.placed(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        GREATWOOD_TREE = Feature.TREE.configured((new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(BlockRegistry.GREATWOOD_LOG.get()), new GreatTrunkPlacer(17,0,0, BlockStateProvider.simple(BlockRegistry.GREATROOTS.get())), BlockStateProvider.simple(BlockRegistry.GREATWOOD_LEAVES.get()), new GreatFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0),2), new TwoLayersFeatureSize(1, 1, 2))).ignoreVines().build());

        //        PALM_TREES = Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(ImmutableList.of(PALM_TREE.weighted(1.0F)), PALM_TREE)).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(1, 0.1F, 1)));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void generation(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();

        if(event.getCategory() == Biome.BiomeCategory.NETHER) {

        } else if (event.getCategory() == Biome.BiomeCategory.THEEND) {

        } else {
            gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PLACED_TREASURE_CHEST);
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
}