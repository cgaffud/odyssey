package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BiomeRegistry;
import com.bedmen.odyssey.registry.WorldTypeRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraftforge.common.world.ForgeWorldPreset;

import java.util.function.Supplier;

public class OdysseyGeneration {

    private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);
    private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
    private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = makeStateRule(Blocks.WHITE_TERRACOTTA);
    private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
    private static final SurfaceRules.RuleSource TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
    private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
    private static final SurfaceRules.RuleSource RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
    private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
    private static final SurfaceRules.RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource PODZOL = makeStateRule(Blocks.PODZOL);
    private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
    private static final SurfaceRules.RuleSource MYCELIUM = makeStateRule(Blocks.MYCELIUM);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource CALCITE = makeStateRule(Blocks.CALCITE);
    private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
    private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
    private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
    private static final SurfaceRules.RuleSource PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);
    private static final SurfaceRules.RuleSource SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
    private static final SurfaceRules.RuleSource POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
    private static final SurfaceRules.RuleSource ICE = makeStateRule(Blocks.ICE);
    private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);

    public static final MultiNoiseBiomeSource.Preset ODYSSEY_OVERWORLD = new MultiNoiseBiomeSource.Preset(new ResourceLocation(Odyssey.MOD_ID+"_overworld"), (p_187108_) -> {
        ImmutableList.Builder<Pair<Climate.ParameterPoint, Supplier<Biome>>> builder = ImmutableList.builder();
        (new OdysseyOverworldBiomeBuilder()).addBiomes((p_187098_) -> {
            builder.add(p_187098_.mapSecond((p_187103_) -> {
                return () -> {
                    return p_187108_.getOrThrow(p_187103_);
                };
            }));
        });
        return new Climate.ParameterList<>(builder.build());
    });

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }

    private static SurfaceRules.ConditionSource surfaceNoiseAbove(double p_194809_) {
        return SurfaceRules.noiseCondition(Noises.SURFACE, p_194809_ / 8.25D, Double.MAX_VALUE);
    }

    public static NoiseGeneratorSettings odysseyOverworld(boolean amplified, boolean largeBiomes) {
        return new NoiseGeneratorSettings(new StructureSettings(true), NoiseSettings.create(-64, 384, new NoiseSamplingSettings(1.0D, 1.0D, 80.0D, 160.0D), new NoiseSlider(-0.078125D, 2, amplified ? 0 : 8), new NoiseSlider(amplified ? 0.4D : 0.1171875D, 3, 0), 1, 2, false, amplified, largeBiomes, TerrainProvider.overworld(amplified)), Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), odysseyOverworldSurfaceRules(), 63, false, true, true, true, true, false);
    }

    public static SurfaceRules.RuleSource odysseyOverworldSurfaceRules() {
        return odysseyOverworldLike(true, false, true);
    }

    public static SurfaceRules.RuleSource odysseyOverworldLike(boolean p_198381_, boolean p_198382_, boolean p_198383_) {
        SurfaceRules.ConditionSource surfacerules$conditionsource = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2);
        SurfaceRules.ConditionSource surfacerules$conditionsource1 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource2 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
        SurfaceRules.ConditionSource surfacerules$conditionsource3 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(74), 1);
        SurfaceRules.ConditionSource surfacerules$conditionsource4 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource5 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource6 = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource7 = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource8 = SurfaceRules.waterStartCheck(-6, -1);
        SurfaceRules.ConditionSource surfacerules$conditionsource9 = SurfaceRules.hole();
        SurfaceRules.ConditionSource surfacerules$conditionsource10 = SurfaceRules.isBiome(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
        SurfaceRules.ConditionSource surfacerules$conditionsource11 = SurfaceRules.steep();
        SurfaceRules.RuleSource surfacerules$rulesource = SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource6, GRASS_BLOCK), DIRT);
        SurfaceRules.RuleSource surfacerules$rulesource1 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND);
        SurfaceRules.RuleSource surfacerules$rulesource2 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL);
        SurfaceRules.ConditionSource surfacerules$conditionsource12 = SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.DESERT, Biomes.BEACH, Biomes.SNOWY_BEACH, BiomeRegistry.TROPICS_RESOURCE_KEY);
        SurfaceRules.RuleSource surfacerules$rulesource3 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.STONY_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.CALCITE, -0.0125D, 0.0125D), CALCITE), STONE)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.STONY_SHORE), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.GRAVEL, -0.05D, 0.05D), surfacerules$rulesource2), STONE)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_HILLS), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE)), SurfaceRules.ifTrue(surfacerules$conditionsource12, surfacerules$rulesource1), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.DRIPSTONE_CAVES), STONE));
        SurfaceRules.RuleSource surfacerules$rulesource4 = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45D, 0.58D), POWDER_SNOW);
        SurfaceRules.RuleSource surfacerules$rulesource5 = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.35D, 0.6D), POWDER_SNOW);
        SurfaceRules.RuleSource surfacerules$rulesource6 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource11, PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, -0.5D, 0.2D), PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, -0.0625D, 0.025D), ICE), SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SNOWY_SLOPES), SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource11, STONE), surfacerules$rulesource4, SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), STONE), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE), SurfaceRules.sequence(surfacerules$rulesource4, DIRT)), surfacerules$rulesource3, SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA), SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), surfacerules$rulesource2), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), DIRT), surfacerules$rulesource2)), DIRT);
        SurfaceRules.RuleSource surfacerules$rulesource7 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource11, PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, 0.0D, 0.2D), PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, 0.0D, 0.025D), ICE), SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SNOWY_SLOPES), SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource11, STONE), surfacerules$rulesource5, SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource11, STONE), SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE), SurfaceRules.sequence(surfacerules$rulesource5, SNOW_BLOCK)), surfacerules$rulesource3, SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE), SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5D), COARSE_DIRT))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), surfacerules$rulesource2), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), surfacerules$rulesource), surfacerules$rulesource2)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT), SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.ICE_SPIKES), SNOW_BLOCK), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MUSHROOM_FIELDS), MYCELIUM), surfacerules$rulesource);
        SurfaceRules.ConditionSource surfacerules$conditionsource13 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.909D, -0.5454D);
        SurfaceRules.ConditionSource surfacerules$conditionsource14 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.1818D, 0.1818D);
        SurfaceRules.ConditionSource surfacerules$conditionsource15 = SurfaceRules.noiseCondition(Noises.SURFACE, 0.5454D, 0.909D);
        SurfaceRules.RuleSource surfacerules$rulesource8 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WOODED_BADLANDS), SurfaceRules.ifTrue(surfacerules$conditionsource, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource13, COARSE_DIRT), SurfaceRules.ifTrue(surfacerules$conditionsource14, COARSE_DIRT), SurfaceRules.ifTrue(surfacerules$conditionsource15, COARSE_DIRT), surfacerules$rulesource))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SWAMP), SurfaceRules.ifTrue(surfacerules$conditionsource4, SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource5), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)))))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource1, ORANGE_TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource3, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource13, TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource14, TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource15, TERRACOTTA), SurfaceRules.bandlands())), SurfaceRules.ifTrue(surfacerules$conditionsource6, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE), RED_SAND)), SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource9), ORANGE_TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource8, WHITE_TERRACOTTA), surfacerules$rulesource2)), SurfaceRules.ifTrue(surfacerules$conditionsource2, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource5, SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource3), ORANGE_TERRACOTTA)), SurfaceRules.bandlands())), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource8, WHITE_TERRACOTTA)))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource6, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource10, SurfaceRules.ifTrue(surfacerules$conditionsource9, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource7, AIR), SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE), WATER))), surfacerules$rulesource7))), SurfaceRules.ifTrue(surfacerules$conditionsource8, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource10, SurfaceRules.ifTrue(surfacerules$conditionsource9, WATER))), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, surfacerules$rulesource6), SurfaceRules.ifTrue(surfacerules$conditionsource12, SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, true, true, CaveSurface.FLOOR), SANDSTONE)))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS), STONE), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN), surfacerules$rulesource1), surfacerules$rulesource2)));
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        if (p_198382_) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK));
        }

        if (p_198383_) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
        }

        SurfaceRules.RuleSource surfacerules$rulesource9 = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), surfacerules$rulesource8);
        builder.add(p_198381_ ? surfacerules$rulesource9 : surfacerules$rulesource8);
        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
        return SurfaceRules.sequence(builder.build().toArray((p_198379_) -> {
            return new SurfaceRules.RuleSource[p_198379_];
        }));
    }

    public static NoiseBasedChunkGenerator makeOdysseyOverworld(RegistryAccess registryAccess, long seed, ResourceKey<NoiseGeneratorSettings> resourceKey, boolean b) {
        return new NoiseBasedChunkGenerator(registryAccess.registryOrThrow(Registry.NOISE_REGISTRY), ODYSSEY_OVERWORLD.biomeSource(registryAccess.registryOrThrow(Registry.BIOME_REGISTRY), b), seed, () -> {
            return registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getOrThrow(resourceKey);
        });
    }

    public static class OdysseyChunkGeneratorFactory implements ForgeWorldPreset.IBasicChunkGeneratorFactory {
        @Override
        public ChunkGenerator createChunkGenerator(RegistryAccess registryAccess, long seed) {
            return makeOdysseyOverworld(registryAccess, seed, WorldTypeRegistry.ODYSSEY_RESOURCE_KEY, true);
        }
    }
}
