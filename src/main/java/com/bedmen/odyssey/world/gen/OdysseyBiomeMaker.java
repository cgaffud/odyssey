package com.bedmen.odyssey.world.gen;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;

public class OdysseyBiomeMaker {

    private static int calculateSkyColor(float p_244206_0_) {
        float lvt_1_1_ = p_244206_0_ / 3.0F;
        lvt_1_1_ = MathHelper.clamp(lvt_1_1_, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - lvt_1_1_ * 0.05F, 0.5F + lvt_1_1_ * 0.1F, 1.0F);
    }

    public static Biome autumnForestBiome(float depth, float scale) {
        BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder()).surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();

        DefaultBiomeFeatures.commonSpawns(mobspawninfo$builder);

        biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);

        //Underground gen
        DefaultBiomeFeatures.addDefaultOverworldLandStructures(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultCarvers(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultMonsterRoom(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultMushrooms(biomegenerationsettings$builder);

        //Land gen
        DefaultBiomeFeatures.addDefaultGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultSprings(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addSurfaceFreezing(biomegenerationsettings$builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.RAIN)
                .biomeCategory(Biome.Category.FOREST)
                .depth(depth)
                .scale(scale)
                .temperature(0.3F)
                .downfall(0.8F)
                .specialEffects((new BiomeAmbience.Builder()).grassColorOverride(10783823).waterColor(4173284).waterFogColor(336947).fogColor(12642815).skyColor(calculateSkyColor(0.7F)).ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
                .mobSpawnSettings(mobspawninfo$builder.build())
                .generationSettings(biomegenerationsettings$builder.build()).build();
    }

    public static Biome tropicsBiome() {
        BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder()).surfaceBuilder(ConfiguredSurfaceBuilders.DESERT);
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();

        DefaultBiomeFeatures.addDefaultOverworldLandStructures(biomegenerationsettings$builder);
        biomegenerationsettings$builder.addStructureStart(StructureFeatures.BURIED_TREASURE);
        biomegenerationsettings$builder.addStructureStart(StructureFeatures.SHIPWRECH_BEACHED);

        biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_DESERT);
        DefaultBiomeFeatures.addDefaultCarvers(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDesertLakes(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultMonsterRoom(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);

        DefaultBiomeFeatures.addDefaultFlowers(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDesertVegetation(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDesertExtraVegetation(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultSprings(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDesertExtraDecoration(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addSurfaceFreezing(biomegenerationsettings$builder);

        return (new Biome.Builder()).precipitation(Biome.RainType.RAIN)
                .biomeCategory(Biome.Category.DESERT)
                .depth(-0.1F)
                .scale(0.025F)
                .temperature(1.5F)
                .downfall(0.8F)
                .specialEffects((new BiomeAmbience.Builder()).waterColor(4445678).waterFogColor(270131).fogColor(12638463).skyColor(calculateSkyColor(0.5F)).ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
                .mobSpawnSettings(mobspawninfo$builder.build())
                .generationSettings(biomegenerationsettings$builder.build()).build();
    }

    public static Biome tropicsBeachBiome() {
        MobSpawnInfo.Builder mobspawninfo$builder = new MobSpawnInfo.Builder();
        mobspawninfo$builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.TURTLE, 5, 2, 5));

        DefaultBiomeFeatures.commonSpawns(mobspawninfo$builder);
        BiomeGenerationSettings.Builder biomegenerationsettings$builder = (new BiomeGenerationSettings.Builder()).surfaceBuilder(ConfiguredSurfaceBuilders.DESERT);
        biomegenerationsettings$builder.addStructureStart(StructureFeatures.MINESHAFT);
        biomegenerationsettings$builder.addStructureStart(StructureFeatures.BURIED_TREASURE);
        biomegenerationsettings$builder.addStructureStart(StructureFeatures.SHIPWRECH_BEACHED);

        biomegenerationsettings$builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
        DefaultBiomeFeatures.addDefaultCarvers(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultLakes(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultMonsterRoom(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultUndergroundVariety(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultOres(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultFlowers(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultGrass(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultExtraVegetation(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addDefaultSprings(biomegenerationsettings$builder);
        DefaultBiomeFeatures.addSurfaceFreezing(biomegenerationsettings$builder);
        return (new Biome.Builder()).precipitation(Biome.RainType.RAIN)
                .biomeCategory(Biome.Category.DESERT)
                .depth(0.05F)
                .scale(0.025F)
                .temperature(1.5F)
                .downfall(0.8F)
                .specialEffects((new BiomeAmbience.Builder()).waterColor(4445678).waterFogColor(270131).fogColor(12638463).skyColor(calculateSkyColor(0.5F)).ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
                .mobSpawnSettings(mobspawninfo$builder.build())
                .generationSettings(biomegenerationsettings$builder.build()).build();
    }
}
