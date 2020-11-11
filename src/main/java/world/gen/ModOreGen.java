package world.gen;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import util.RegistryHandler;

@Mod.EventBusSubscriber
public class ModOreGen {
	
	public static ConfiguredFeature<?, ?> RUBY_ORE_FEATURE;
	
	public static void registerOres() {
		RUBY_ORE_FEATURE = oreGen(64, 9, 20, RegistryHandler.RUBY_ORE);
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
			if(event.getCategory() == Biome.Category.DESERT) {
				gen.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, RUBY_ORE_FEATURE);
			}
		}
		
	}
	
	private static ConfiguredFeature<?, ?> oreGen(int max_y, int size, int count, RegistryObject<Block> oreBlock){
		return Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, oreBlock.get().getDefaultState(), size)).range(max_y).square().func_242731_b(count);
	}
	
}
