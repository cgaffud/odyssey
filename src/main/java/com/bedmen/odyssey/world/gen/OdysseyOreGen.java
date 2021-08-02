package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.util.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
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
public class OdysseyOreGen {

    public static ConfiguredFeature<?, ?> SAPPHIRE_ORE_FEATURE;
    public static final RuleTest PACKED_ICE_RULE = new BlockMatchRuleTest(Blocks.PACKED_ICE);
    // OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD for generating in stone

    public static void registerOres() {
        SAPPHIRE_ORE_FEATURE = oreGen(128, 20, 20, BlockRegistry.SAPPHIRE_ORE, PACKED_ICE_RULE);
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
            if(event.getCategory() == Biome.Category.ICY) {
                gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, SAPPHIRE_ORE_FEATURE);
            }
        }

    }

    private static ConfiguredFeature<?, ?> oreGen(int max_y, int size, int count, RegistryObject<Block> oreBlock, RuleTest rule){
        return Feature.ORE.configured(new OreFeatureConfig(rule, oreBlock.get().defaultBlockState(), size)).range(max_y).squared().count(count);
    }

}