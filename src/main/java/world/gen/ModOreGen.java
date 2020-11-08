package world.gen;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import util.RegistryHandler;

import com.bedmen.odyssey.Odyssey;

@Mod.EventBusSubscriber
public class ModOreGen {
	
	public static ConfiguredFeature<?, ?> RUBY_ORE;
	
	public static void registerOres() {
		RUBY_ORE = register("rubyOre", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, RegistryHandler.RUBY_ORE.get().getDefaultState(), 9)).range(64).square().func_242731_b(20));
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void generation(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder gen = event.getGeneration();
		if (!(event.getCategory().equals(Biome.Category.NETHER) || event.getCategory().equals(Biome.Category.THEEND))) {
			gen.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, RUBY_ORE);
		}
		
	}
	
	private static <FC extends IFeatureConfig> ConfiguredFeature<FC,?> register(String name, ConfiguredFeature<FC, ?>  cfeat) {
	      return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Odyssey.MOD_ID, cfeat);
	}
	
}
