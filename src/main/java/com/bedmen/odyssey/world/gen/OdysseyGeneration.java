package com.bedmen.odyssey.world.gen;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.world.gen.biome.BiomeResourceKeys;
import com.bedmen.odyssey.world.gen.biome.OdysseyOverworldBiomeBuilder;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;

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

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }

    private static SurfaceRules.ConditionSource surfaceNoiseAbove(double noiseThreshold) {
        return SurfaceRules.noiseCondition(Noises.SURFACE, noiseThreshold / 8.25D, Double.MAX_VALUE);
    }

    public static SurfaceRules.RuleSource odysseyOverworldLike(boolean abovePreliminarySurface, boolean bedrockRoof, boolean bedrockFloor) {
        // Conditions
        SurfaceRules.ConditionSource coarseDirtHeightCondition = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2);
        SurfaceRules.ConditionSource above256Condition = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
        SurfaceRules.ConditionSource orangeTerracotaHeightCondition = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
        SurfaceRules.ConditionSource regularTerracotaHeightCondition = SurfaceRules.yStartCheck(VerticalAnchor.absolute(74), 1);
        SurfaceRules.ConditionSource above62Condition = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
        SurfaceRules.ConditionSource above63Condition = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
        SurfaceRules.ConditionSource waterSurfaceOrAboveCondition = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource notUnderWaterCondition = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource somewhatUnderWaterOrAboveCondition = SurfaceRules.waterStartCheck(-6, -1);
        SurfaceRules.ConditionSource holeCondition = SurfaceRules.hole();
        SurfaceRules.ConditionSource steepSurfaceCondition = SurfaceRules.steep();
        SurfaceRules.ConditionSource lowSurfaceNoiseCondition = SurfaceRules.noiseCondition(Noises.SURFACE, -0.909D, -0.5454D);
        SurfaceRules.ConditionSource mediumSurfaceNoiseCondition = SurfaceRules.noiseCondition(Noises.SURFACE, -0.1818D, 0.1818D);
        SurfaceRules.ConditionSource highSurfaceNoiseCondition = SurfaceRules.noiseCondition(Noises.SURFACE, 0.5454D, 0.909D);
        SurfaceRules.ConditionSource isBeachLikeCondition = SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH, BiomeResourceKeys.TROPICS_RESOURCE_KEY);
        SurfaceRules.ConditionSource isDesertCondition = SurfaceRules.isBiome(Biomes.DESERT);
        SurfaceRules.ConditionSource frozenOceanCondition = SurfaceRules.isBiome(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
        SurfaceRules.ConditionSource stonyPeakcalciteNoiseCondition = SurfaceRules.noiseCondition(Noises.CALCITE, -0.0125D, 0.0125D);
        SurfaceRules.ConditionSource stonyShoreGravelNoiseCondition = SurfaceRules.noiseCondition(Noises.GRAVEL, -0.05D, 0.05D);
        SurfaceRules.ConditionSource smallAmountOfPowderSnowNoiseCondition = SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45D, 0.58D);
        SurfaceRules.ConditionSource largeAmountOfPowderSnowNoiseCondition = SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.35D, 0.6D);
        SurfaceRules.ConditionSource smallAmountOfPackedIceNoiseCondition = SurfaceRules.noiseCondition(Noises.PACKED_ICE, 0.0D, 0.2D);
        SurfaceRules.ConditionSource largeAmountOfPackedIceNoiseCondition = SurfaceRules.noiseCondition(Noises.PACKED_ICE, -0.5D, 0.2D);
        SurfaceRules.ConditionSource smallAmountOficeNoiseCondition = SurfaceRules.noiseCondition(Noises.ICE, 0.0D, 0.025D);
        SurfaceRules.ConditionSource largeAmountOficeNoiseCondition = SurfaceRules.noiseCondition(Noises.ICE, -0.0625D, 0.025D);

        // Small Rules
        SurfaceRules.RuleSource calciteRule = SurfaceRules.ifTrue(stonyPeakcalciteNoiseCondition, CALCITE);
        SurfaceRules.RuleSource grassIfNotUnderWaterOtherwiseDirtRule = SurfaceRules.sequence(SurfaceRules.ifTrue(notUnderWaterCondition, GRASS_BLOCK), DIRT);
        SurfaceRules.RuleSource sandButNotOnCeilingRule = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND);
        SurfaceRules.RuleSource gravelButNotOnCeilingRule = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL);
        SurfaceRules.RuleSource stonyShoreGravelRule = SurfaceRules.ifTrue(stonyShoreGravelNoiseCondition, gravelButNotOnCeilingRule);
        SurfaceRules.RuleSource windsweptHillsStoneRule = SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE);
        SurfaceRules.RuleSource powderSnowRule = SurfaceRules.ifTrue(notUnderWaterCondition, POWDER_SNOW);
        SurfaceRules.RuleSource smallAmountOfPowderSnowRule = SurfaceRules.ifTrue(smallAmountOfPowderSnowNoiseCondition, powderSnowRule);
        SurfaceRules.RuleSource largeAmountOfPowderSnowRule = SurfaceRules.ifTrue(largeAmountOfPowderSnowNoiseCondition, powderSnowRule);
        SurfaceRules.RuleSource packedIceSteepSurfaceRule = SurfaceRules.ifTrue(steepSurfaceCondition, PACKED_ICE);
        SurfaceRules.RuleSource smallAmountOfPackedIceNoiseRule = SurfaceRules.ifTrue(smallAmountOfPackedIceNoiseCondition, PACKED_ICE);
        SurfaceRules.RuleSource largeAmountOfPackedIceNoiseRule = SurfaceRules.ifTrue(largeAmountOfPackedIceNoiseCondition, PACKED_ICE);
        SurfaceRules.RuleSource smallAmountOfIceNoiseRule = SurfaceRules.ifTrue(smallAmountOficeNoiseCondition, ICE);
        SurfaceRules.RuleSource largeAmountOfIceNoiseRule = SurfaceRules.ifTrue(largeAmountOficeNoiseCondition, ICE);
        SurfaceRules.RuleSource snowNotUnderWaterRule = SurfaceRules.ifTrue(notUnderWaterCondition, SNOW_BLOCK);
        SurfaceRules.RuleSource stoneSteepSurfaceRule = SurfaceRules.ifTrue(steepSurfaceCondition, STONE);
        SurfaceRules.RuleSource windsweptSavannaStoneRule = SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE);
        SurfaceRules.RuleSource windsweptGravellyHillsGravelRule = SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), gravelButNotOnCeilingRule);

        // Sequence Rules
        SurfaceRules.RuleSource frozenPeaksSmallIceSequenceRule = SurfaceRules.sequence(
                packedIceSteepSurfaceRule,
                smallAmountOfPackedIceNoiseRule,
                smallAmountOfIceNoiseRule,
                snowNotUnderWaterRule
        );
        SurfaceRules.RuleSource frozenPeaksLargeIceSequenceRule = SurfaceRules.sequence(
                packedIceSteepSurfaceRule,
                largeAmountOfPackedIceNoiseRule,
                largeAmountOfIceNoiseRule,
                snowNotUnderWaterRule
        );
        SurfaceRules.RuleSource snowySlopesSmallPowderSnowSequenceRule = SurfaceRules.sequence(
                stoneSteepSurfaceRule,
                smallAmountOfPowderSnowRule,
                snowNotUnderWaterRule
        );
        SurfaceRules.RuleSource snowySlopesLargePowderSnowSequenceRule = SurfaceRules.sequence(
                stoneSteepSurfaceRule,
                largeAmountOfPowderSnowRule,
                snowNotUnderWaterRule
        );
        SurfaceRules.RuleSource groveSmallPowderSnowSequenceRule = SurfaceRules.sequence(
                smallAmountOfPowderSnowRule,
                DIRT
        );
        SurfaceRules.RuleSource groveLargePowderSnowSequenceRule = SurfaceRules.sequence(
                largeAmountOfPowderSnowRule,
                snowNotUnderWaterRule
        );
        SurfaceRules.RuleSource windsweptGravellyHillsWithDirtSequenceRule = SurfaceRules.sequence(
                windsweptGravellyHillsGravelRule,
                windsweptHillsStoneRule,
                SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), DIRT),
                gravelButNotOnCeilingRule
        );
        SurfaceRules.RuleSource windsweptGravellyHillsWithGrassSequenceRule = SurfaceRules.sequence(
                windsweptGravellyHillsGravelRule,
                windsweptHillsStoneRule,
                SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), grassIfNotUnderWaterOtherwiseDirtRule),
                gravelButNotOnCeilingRule
        );
        SurfaceRules.RuleSource jaggedPeaksWithSnowSequenceRule = SurfaceRules.sequence(
                stoneSteepSurfaceRule,
                snowNotUnderWaterRule
        );
        SurfaceRules.RuleSource windsweptSavannaWithCoarseDirtSequenceRule = SurfaceRules.sequence(
                windsweptSavannaStoneRule,
                SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5D), COARSE_DIRT)
        );
        SurfaceRules.RuleSource oldGrowthTaigaSequenceRule = SurfaceRules.sequence(
                SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT),
                SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL)
        );
        SurfaceRules.RuleSource arcticLessIceMoreSnowSequenceRule = SurfaceRules.sequence(
                smallAmountOfPackedIceNoiseRule,
                smallAmountOfIceNoiseRule,
                largeAmountOfPowderSnowRule,
                snowNotUnderWaterRule
        );
        SurfaceRules.RuleSource arcticMoreIceLessSnowSequenceRule = SurfaceRules.sequence(
                largeAmountOfPackedIceNoiseRule,
                largeAmountOfIceNoiseRule,
                smallAmountOfPowderSnowRule,
                snowNotUnderWaterRule
        );

        // Biome Rules
        SurfaceRules.RuleSource stonyPeaksRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.STONY_PEAKS), SurfaceRules.sequence(calciteRule, STONE));
        SurfaceRules.RuleSource stonyShoreRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.STONY_SHORE), SurfaceRules.sequence(stonyShoreGravelRule, STONE));
        SurfaceRules.RuleSource windsweptHillsRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_HILLS), windsweptHillsStoneRule);
        SurfaceRules.RuleSource beachSandRule = SurfaceRules.ifTrue(isBeachLikeCondition, sandButNotOnCeilingRule);
        SurfaceRules.RuleSource desertSandRule = SurfaceRules.ifTrue(isDesertCondition, sandButNotOnCeilingRule);
        SurfaceRules.RuleSource dripstoneCavesStoneRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.DRIPSTONE_CAVES), STONE);
        SurfaceRules.RuleSource frozenPeaksSmallIceRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS), frozenPeaksSmallIceSequenceRule);
        SurfaceRules.RuleSource frozenPeaksLargeIceRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS), frozenPeaksLargeIceSequenceRule);
        SurfaceRules.RuleSource snowySlopesSmallPowderSnowRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SNOWY_SLOPES), snowySlopesSmallPowderSnowSequenceRule);
        SurfaceRules.RuleSource snowySlopesLargePowderSnowRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SNOWY_SLOPES), snowySlopesLargePowderSnowSequenceRule);
        SurfaceRules.RuleSource jaggedPeaksNoSnowRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), STONE);
        SurfaceRules.RuleSource jaggedPeaksWithSnowRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), jaggedPeaksWithSnowSequenceRule);
        SurfaceRules.RuleSource groveSmallPowderSnowRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE), groveSmallPowderSnowSequenceRule);
        SurfaceRules.RuleSource groveLargePowderSnowRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE), groveLargePowderSnowSequenceRule);
        SurfaceRules.RuleSource windsweptSavannaNoCoarseDirtRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA), windsweptSavannaStoneRule);
        SurfaceRules.RuleSource windsweptSavannaWithCoarseDirtRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA), windsweptSavannaWithCoarseDirtSequenceRule);
        SurfaceRules.RuleSource windsweptGravellyHillsWithDirtRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS), windsweptGravellyHillsWithDirtSequenceRule);
        SurfaceRules.RuleSource windsweptGravellyHillsWithGrassRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS), windsweptGravellyHillsWithGrassSequenceRule);
        SurfaceRules.RuleSource oldGrowthTaigaRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA), oldGrowthTaigaSequenceRule);
        SurfaceRules.RuleSource iceSpikesRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.ICE_SPIKES), snowNotUnderWaterRule);
        SurfaceRules.RuleSource mushroomFieldsRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MUSHROOM_FIELDS), MYCELIUM);
        SurfaceRules.RuleSource woodedBadlandsCoarseDirtRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WOODED_BADLANDS), SurfaceRules.ifTrue(coarseDirtHeightCondition, SurfaceRules.sequence(SurfaceRules.ifTrue(lowSurfaceNoiseCondition, COARSE_DIRT), SurfaceRules.ifTrue(mediumSurfaceNoiseCondition, COARSE_DIRT), SurfaceRules.ifTrue(highSurfaceNoiseCondition, COARSE_DIRT), grassIfNotUnderWaterOtherwiseDirtRule)));
        SurfaceRules.RuleSource swampRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SWAMP), SurfaceRules.ifTrue(above62Condition, SurfaceRules.ifTrue(SurfaceRules.not(above63Condition), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER))));
        SurfaceRules.RuleSource arcticLessIceMoreSnowRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(BiomeResourceKeys.ARCTIC_RESOURCE_KEY), arcticLessIceMoreSnowSequenceRule);
        SurfaceRules.RuleSource arcticMoreIceLessSnowRule = SurfaceRules.ifTrue(SurfaceRules.isBiome(BiomeResourceKeys.ARCTIC_RESOURCE_KEY), arcticMoreIceLessSnowSequenceRule);

        // Multi-Biome Rules
        SurfaceRules.RuleSource stoneAndSandRules = SurfaceRules.sequence(
                stonyPeaksRule,
                stonyShoreRule,
                windsweptHillsRule,
                beachSandRule,
                desertSandRule,
                dripstoneCavesStoneRule
        );

        SurfaceRules.RuleSource lessSnowySurfaceRule = SurfaceRules.sequence(
                frozenPeaksLargeIceRule,
                snowySlopesSmallPowderSnowRule,
                arcticMoreIceLessSnowRule,
                jaggedPeaksNoSnowRule,
                groveSmallPowderSnowRule,
                stoneAndSandRules,
                windsweptSavannaNoCoarseDirtRule,
                windsweptGravellyHillsWithDirtRule,
                DIRT
        );
        
        SurfaceRules.RuleSource moreSnowySurfaceRule = SurfaceRules.sequence(
                frozenPeaksSmallIceRule,
                snowySlopesLargePowderSnowRule,
                arcticLessIceMoreSnowRule,
                jaggedPeaksWithSnowRule,
                groveLargePowderSnowRule,
                stoneAndSandRules,
                windsweptSavannaWithCoarseDirtRule,
                windsweptGravellyHillsWithGrassRule,
                oldGrowthTaigaRule,
                iceSpikesRule,
                mushroomFieldsRule,
                grassIfNotUnderWaterOtherwiseDirtRule
        );
        
        SurfaceRules.RuleSource surfacerules$rulesource8 = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(woodedBadlandsCoarseDirtRule, swampRule)),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(above256Condition, ORANGE_TERRACOTTA), SurfaceRules.ifTrue(regularTerracotaHeightCondition, SurfaceRules.sequence(SurfaceRules.ifTrue(lowSurfaceNoiseCondition, TERRACOTTA), SurfaceRules.ifTrue(mediumSurfaceNoiseCondition, TERRACOTTA), SurfaceRules.ifTrue(highSurfaceNoiseCondition, TERRACOTTA), SurfaceRules.bandlands())), SurfaceRules.ifTrue(waterSurfaceOrAboveCondition, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE), RED_SAND)), SurfaceRules.ifTrue(SurfaceRules.not(holeCondition), ORANGE_TERRACOTTA), SurfaceRules.ifTrue(somewhatUnderWaterOrAboveCondition, WHITE_TERRACOTTA), gravelButNotOnCeilingRule)), SurfaceRules.ifTrue(orangeTerracotaHeightCondition, SurfaceRules.sequence(SurfaceRules.ifTrue(above63Condition, SurfaceRules.ifTrue(SurfaceRules.not(regularTerracotaHeightCondition), ORANGE_TERRACOTTA)), SurfaceRules.bandlands())), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(somewhatUnderWaterOrAboveCondition, WHITE_TERRACOTTA)))),
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(waterSurfaceOrAboveCondition, SurfaceRules.sequence(SurfaceRules.ifTrue(frozenOceanCondition, SurfaceRules.ifTrue(holeCondition, SurfaceRules.sequence(SurfaceRules.ifTrue(notUnderWaterCondition, AIR), SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE), WATER))), moreSnowySurfaceRule))),
                SurfaceRules.ifTrue(somewhatUnderWaterOrAboveCondition, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(frozenOceanCondition, SurfaceRules.ifTrue(holeCondition, WATER))), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, lessSnowySurfaceRule), SurfaceRules.ifTrue(isBeachLikeCondition, SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)), SurfaceRules.ifTrue(isDesertCondition, SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE)))),
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS), STONE), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN), sandButNotOnCeilingRule), gravelButNotOnCeilingRule))
        );
        
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        if (bedrockRoof) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK));
        }

        if (bedrockFloor) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
        }

        SurfaceRules.RuleSource surfacerules$rulesource9 = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), surfacerules$rulesource8);
        builder.add(abovePreliminarySurface ? surfacerules$rulesource9 : surfacerules$rulesource8);
        builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }

    public static MultiNoiseBiomeSource.Preset ODYSSEY_OVERWORLD;

    public static void init(){
        ODYSSEY_OVERWORLD = new MultiNoiseBiomeSource.Preset(new ResourceLocation(Odyssey.MOD_ID, "overworld"), (biomeRegistry) -> {
            ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
            (new OdysseyOverworldBiomeBuilder()).addBiomes((keyPair) -> {
                builder.add(keyPair.mapSecond(biomeRegistry::getOrCreateHolderOrThrow));
            });
            return new Climate.ParameterList<>(builder.build());
        });
    }
}
