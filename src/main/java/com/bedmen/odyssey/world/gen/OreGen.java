package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Odyssey.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OreGen {

    public static ConfiguredFeature<?, ?> ORE_SILVER;
    public static ImmutableList<OreConfiguration.TargetBlockState> ORE_SILVER_TARGET_LIST;

    public static void registerOres() {
        ORE_SILVER_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.STONE_ORE_REPLACEABLES, BlockRegistry.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockRegistry.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
        ORE_SILVER = Feature.ORE.configured(new OreConfiguration(ORE_SILVER_TARGET_LIST, 9, 0.5F)).rangeTriangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32)).squared().count(4);
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
            gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SILVER);
        }
    }
}