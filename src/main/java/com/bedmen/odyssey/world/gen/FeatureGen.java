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

//    public static ConfiguredFeature<?, ?> PERMAFROST_TOWER;
//    public static ConfiguredFeature<?, ?> FOG;
    public static ConfiguredFeature<?, ?> TREASURE_CHEST;
    public static PlacedFeature PLACED_TREASURE_CHEST;
    public static ConfiguredFeature<?, ?> ABANDONED_IRON_GOLEM;
    public static PlacedFeature PLACED_ABANDONED_IRON_GOLEM;

    public static void registerFeatures() {
//        PERMAFROST_TOWER = featureGen(FeatureRegistry.PERMAFROST_TOWER, 1);
//        FOG = FeatureRegistry.FOG.get().configured(FeatureConfiguration.NONE);

        TREASURE_CHEST = FeatureRegistry.TREASURE_CHEST.get().configured(FeatureConfiguration.NONE);
        PLACED_TREASURE_CHEST = PlacementUtils.register("placed_treasure_chest", TREASURE_CHEST.placed(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
        ABANDONED_IRON_GOLEM = FeatureRegistry.ABANDONED_IRON_GOLEM.get().configured(FeatureConfiguration.NONE);
        PLACED_ABANDONED_IRON_GOLEM = PlacementUtils.register("placed_abandoned_iron_golem", ABANDONED_IRON_GOLEM.placed(CountPlacement.of(1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void generation(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();

        if(event.getCategory() == Biome.BiomeCategory.NETHER) {

        } else if (event.getCategory() == Biome.BiomeCategory.THEEND) {

        } else {
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, PLACED_TREASURE_CHEST);
            gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PLACED_ABANDONED_IRON_GOLEM);
//            gen.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, FOG);
//            if(event.getName().toString().equals("minecraft:ice_spikes")) {
//                gen.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, PERMAFROST_TOWER);
//            }
        }
    }
}