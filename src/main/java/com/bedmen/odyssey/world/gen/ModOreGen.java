package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.util.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModOreGen {

    public static ConfiguredFeature<?, ?> SILVER_ORE_FEATURE;
    public static final RuleTest SAND_RULE = new BlockMatchRuleTest(Blocks.SAND);
    // OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD for generating in stone

    public static void registerOres() {
        SILVER_ORE_FEATURE = oreGen(128, 100, 20, BlockRegistry.SILVER_ORE, SAND_RULE);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void generation(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();

        if(event.getCategory() == Biome.Category.NETHER) {
            // Nether Ore

        } else if (event.getCategory() == Biome.Category.THEEND) {
            // End Ore

        } else {
            //Overworld Ore
            if(event.getCategory() == Biome.Category.BEACH) {
                //gen.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, SILVER_ORE_FEATURE);
            }
        }

    }

    private static ConfiguredFeature<?, ?> oreGen(int max_y, int size, int count, RegistryObject<Block> oreBlock, RuleTest rule){
        return Feature.ORE.withConfiguration(new OreFeatureConfig(rule, oreBlock.get().getDefaultState(), size)).range(max_y).square().func_242731_b(count);
    }

}