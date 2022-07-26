package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OreGen {
    
    public static List<OreConfiguration.TargetBlockState> ORE_SILVER_TARGET_LIST;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> CONFIGURED_ORE_SILVER_BURIED;
    public static Holder<PlacedFeature> PLACED_ORE_SILVER;
    public static Holder<PlacedFeature> PLACED_ORE_SILVER_LOWER;

    public static Holder<ConfiguredFeature<OreConfiguration, ?>> CONFIGURED_ORE_MOONROCK_BURIED;
    public static Holder<PlacedFeature> PLACED_ORE_MOONROCK;

    public static void registerOres() {
        ORE_SILVER_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, BlockRegistry.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockRegistry.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
        CONFIGURED_ORE_SILVER_BURIED = FeatureUtils.register("ore_silver_buried", Feature.ORE, new OreConfiguration(ORE_SILVER_TARGET_LIST, 9, 0.5F));
        PLACED_ORE_SILVER = PlacementUtils.register("ore_silver", CONFIGURED_ORE_SILVER_BURIED, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
        PLACED_ORE_SILVER_LOWER = PlacementUtils.register("ore_silver_lower", CONFIGURED_ORE_SILVER_BURIED, orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))));

        CONFIGURED_ORE_MOONROCK_BURIED = FeatureUtils.register("ore_moonrock_buried", Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, BlockRegistry.MOONROCK.get().defaultBlockState())), 25));
        PLACED_ORE_MOONROCK = PlacementUtils.register("ore_moonrock", CONFIGURED_ORE_MOONROCK_BURIED, commonOrePlacement(2,  HeightRangePlacement.triangle(VerticalAnchor.absolute(110), VerticalAnchor.absolute(160))));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void generation(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();

        if(event.getCategory() == Biome.BiomeCategory.NETHER) {
            // Nether Ore

        } else if (event.getCategory() == Biome.BiomeCategory.THEEND) {
            // End Ore

        } else {
            //Overworld Ore
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_ORE_SILVER);
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_ORE_SILVER_LOWER);
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_ORE_MOONROCK);
        }
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier1) {
        return List.of(placementModifier, InSquarePlacement.spread(), placementModifier1, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier placementModifier) {
        return orePlacement(CountPlacement.of(count), placementModifier);
    }

    private static List<PlacementModifier> rareOrePlacement(int rate, PlacementModifier placementModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(rate), placementModifier);
    }
}