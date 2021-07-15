package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.util.BlockRegistry;
import com.bedmen.odyssey.util.FeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModFeatureGen {

    public static ConfiguredFeature<?, ?> MEGA_ICE_SPIKE;

    public static void registerFeatures() {
        MEGA_ICE_SPIKE = featureGen(FeatureRegistry.MEGA_ICE_SPIKE);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void generation(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();

        if(event.getCategory() == Biome.Category.NETHER) {

        } else if (event.getCategory() == Biome.Category.THEEND) {

        } else {
            if(event.getName().toString().equals("minecraft:ice_spikes")) {
                //gen.surfaceBuilder(ConfiguredSurfaceBuilders.ICE_SPIKES);
                gen.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MEGA_ICE_SPIKE);
            }
        }

    }

    private static ConfiguredFeature<?, ?> featureGen(RegistryObject<Feature<NoFeatureConfig>> feature){
        return feature.get().configured(IFeatureConfig.NONE).decorated(Features.Placements.HEIGHTMAP_SQUARE).count(1);
    }

}