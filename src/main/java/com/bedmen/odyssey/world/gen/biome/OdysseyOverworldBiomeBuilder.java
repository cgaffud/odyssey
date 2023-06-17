package com.bedmen.odyssey.world.gen.biome;

import com.bedmen.odyssey.registry.BiomeRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.SharedConstants;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import java.util.function.Consumer;

public class OdysseyOverworldBiomeBuilder {
    private static final float VALLEY_SIZE = 0.05F;
    private static final float LOW_START = 0.26666668F;
    public static final float HIGH_START = 0.4F;
    private static final float HIGH_END = 0.93333334F;
    private static final float PEAK_SIZE = 0.1F;
    public static final float PEAK_START = 0.56666666F;
    private static final float PEAK_END = 0.7666667F;
    public static final float NEAR_INLAND_START = -0.11F;
    public static final float MID_INLAND_START = 0.03F;
    public static final float FAR_INLAND_START = 0.3F;
    public static final float EROSION_INDEX_1_START = -0.78F;
    public static final float EROSION_INDEX_2_START = -0.375F;
    private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
    private final Climate.Parameter[] temperatures = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.45F), Climate.Parameter.span(-0.45F, -0.15F), Climate.Parameter.span(-0.15F, 0.2F), Climate.Parameter.span(0.2F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
    private final Climate.Parameter[] humidities = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, -0.35F), Climate.Parameter.span(-0.35F, -PEAK_SIZE), Climate.Parameter.span(-PEAK_SIZE, PEAK_SIZE), Climate.Parameter.span(PEAK_SIZE, FAR_INLAND_START), Climate.Parameter.span(FAR_INLAND_START, 1.0F)};
    private final Climate.Parameter[] erosions = new Climate.Parameter[]{Climate.Parameter.span(-1.0F, EROSION_INDEX_1_START), Climate.Parameter.span(EROSION_INDEX_1_START, EROSION_INDEX_2_START), Climate.Parameter.span(EROSION_INDEX_2_START, -0.2225F), Climate.Parameter.span(-0.2225F, VALLEY_SIZE), Climate.Parameter.span(VALLEY_SIZE, 0.45F), Climate.Parameter.span(0.45F, 0.55F), Climate.Parameter.span(0.55F, 1.0F)};
    private final Climate.Parameter FROZEN_RANGE = this.temperatures[0];
    private final Climate.Parameter UNFROZEN_RANGE = Climate.Parameter.span(this.temperatures[1], this.temperatures[4]);
    private final Climate.Parameter mushroomFieldsContinentalness = Climate.Parameter.span(-1.2F, -1.05F);
    private final Climate.Parameter deepOceanContinentalness = Climate.Parameter.span(-1.05F, -0.455F);
    private final Climate.Parameter oceanContinentalness = Climate.Parameter.span(-0.455F, -0.19F);
    private final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, NEAR_INLAND_START);
    private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(NEAR_INLAND_START, 0.55F);
    private final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(NEAR_INLAND_START, MID_INLAND_START);
    private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(MID_INLAND_START, FAR_INLAND_START);
    private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(FAR_INLAND_START, 1.0F);
    private final ResourceKey<Biome>[][] OCEANS = new ResourceKey[][]{{Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN}, {Biomes.FROZEN_OCEAN, Biomes.COLD_OCEAN, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN}};
    private final ResourceKey<Biome>[][] MIDDLE_BIOMES = new ResourceKey[][]{{Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.TAIGA}, {Biomes.PLAINS, Biomes.PLAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA}, {Biomes.FLOWER_FOREST, Biomes.PLAINS, Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST}, {Biomes.SAVANNA, Biomes.SAVANNA, Biomes.FOREST, Biomes.JUNGLE, Biomes.JUNGLE}, {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.DESERT}};
    private final ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{{Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null}, {null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA}, {Biomes.SUNFLOWER_PLAINS, null, null, Biomes.OLD_GROWTH_BIRCH_FOREST, null}, {BiomeRegistry.PRAIRIE_RESOURCE_KEY, BiomeRegistry.PRAIRIE_RESOURCE_KEY, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE}, {null, null, null, null, null}};
    private final ResourceKey<Biome>[][] PLATEAU_BIOMES = new ResourceKey[][]{{Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA}, {Biomes.MEADOW, Biomes.MEADOW, Biomes.FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA}, {Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.MEADOW, Biomes.DARK_FOREST}, {Biomes.SAVANNA_PLATEAU, Biomes.SAVANNA_PLATEAU, Biomes.FOREST, Biomes.FOREST, Biomes.JUNGLE}, {Biomes.BADLANDS, Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.WOODED_BADLANDS}};
    private final ResourceKey<Biome>[][] PLATEAU_BIOMES_VARIANT = new ResourceKey[][]{{Biomes.ICE_SPIKES, null, null, null, null}, {null, null, Biomes.MEADOW, Biomes.MEADOW, Biomes.OLD_GROWTH_PINE_TAIGA}, {null, null, Biomes.FOREST, Biomes.BIRCH_FOREST, null}, {null, null, null, null, null}, {Biomes.ERODED_BADLANDS, Biomes.ERODED_BADLANDS, null, null, null}};
    private final ResourceKey<Biome>[][] EXTREME_HILLS = new ResourceKey[][]{{Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST}, {Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST}, {Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_FOREST}, {null, null, null, null, null}, {null, null, null, null, null}};

    public void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer) {
        if (SharedConstants.debugGenerateSquareTerrainWithoutNoise) {
            TerrainProvider.overworld(false).addDebugBiomesToVisualizeSplinePoints(pairConsumer);
        } else {
            this.addOffCoastBiomes(pairConsumer);
            this.addInlandBiomes(pairConsumer);
            this.addUndergroundBiomes(pairConsumer);
            this.addSurfaceBiome(pairConsumer, Climate.Parameter.span(-1.0f, -0.75f), FULL_RANGE, this.midInlandContinentalness, FULL_RANGE, FULL_RANGE, 0.0f, BiomeRegistry.ARCTIC_RESOURCE_KEY);
        }
    }

    private void addOffCoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer) {
        this.addSurfaceBiome(pairConsumer, Climate.Parameter.span(temperatures[0], temperatures[3]), this.FULL_RANGE, this.mushroomFieldsContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.MUSHROOM_FIELDS);
        this.addSurfaceBiome(pairConsumer, temperatures[4], this.FULL_RANGE, this.mushroomFieldsContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, BiomeRegistry.TROPICS_RESOURCE_KEY);

        for(int temperatureIndex = 0; temperatureIndex < this.temperatures.length; ++temperatureIndex) {
            Climate.Parameter temperatureParameter = this.temperatures[temperatureIndex];
            this.addSurfaceBiome(pairConsumer, temperatureParameter, this.FULL_RANGE, this.deepOceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[0][temperatureIndex]);
            this.addSurfaceBiome(pairConsumer, temperatureParameter, this.FULL_RANGE, this.oceanContinentalness, this.FULL_RANGE, this.FULL_RANGE, 0.0F, this.OCEANS[1][temperatureIndex]);
        }

    }

    private void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer) {
        this.addMidSlice(pairConsumer, Climate.Parameter.span(-1.0F, -HIGH_END));
        this.addHighSlice(pairConsumer, Climate.Parameter.span(-HIGH_END, -PEAK_END));
        this.addPeaks(pairConsumer, Climate.Parameter.span(-PEAK_END, -PEAK_START));
        this.addHighSlice(pairConsumer, Climate.Parameter.span(-PEAK_START, -HIGH_START));
        this.addMidSlice(pairConsumer, Climate.Parameter.span(-HIGH_START, -LOW_START));
        this.addLowSlice(pairConsumer, Climate.Parameter.span(-LOW_START, -VALLEY_SIZE));
        this.addValleys(pairConsumer, Climate.Parameter.span(-VALLEY_SIZE, VALLEY_SIZE));
        this.addLowSlice(pairConsumer, Climate.Parameter.span(VALLEY_SIZE, LOW_START));
        this.addMidSlice(pairConsumer, Climate.Parameter.span(LOW_START, HIGH_START));
        this.addHighSlice(pairConsumer, Climate.Parameter.span(HIGH_START, PEAK_START));
        this.addPeaks(pairConsumer, Climate.Parameter.span(PEAK_START, PEAK_END));
        this.addHighSlice(pairConsumer, Climate.Parameter.span(PEAK_END, HIGH_END));
        this.addMidSlice(pairConsumer, Climate.Parameter.span(HIGH_END, 1.0F));
    }

    private void addPeaks(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer, Climate.Parameter weirdnessParameter) {
        for(int temperatureIndex = 0; temperatureIndex < this.temperatures.length; ++temperatureIndex) {
            Climate.Parameter temperatureParameter = this.temperatures[temperatureIndex];

            for(int humidityIndex = 0; humidityIndex < this.humidities.length; ++humidityIndex) {
                Climate.Parameter humidityParameter = this.humidities[humidityIndex];
                ResourceKey<Biome> resourcekey = this.pickMiddleBiome(temperatureIndex, humidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey1 = this.pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey2 = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey3 = this.pickPlateauBiome(temperatureIndex, humidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey4 = this.pickExtremeHillsBiome(temperatureIndex, humidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey5 = this.maybePickShatteredBiome(temperatureIndex, humidityIndex, weirdnessParameter, resourcekey4);
                ResourceKey<Biome> resourcekey6 = this.pickPeakBiome(temperatureIndex, humidityIndex, weirdnessParameter);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[0], weirdnessParameter, 0.0F, resourcekey6);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[1], weirdnessParameter, 0.0F, resourcekey2);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdnessParameter, 0.0F, resourcekey6);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdnessParameter, 0.0F, resourcekey);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdnessParameter, 0.0F, resourcekey3);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, this.midInlandContinentalness, this.erosions[3], weirdnessParameter, 0.0F, resourcekey1);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, this.farInlandContinentalness, this.erosions[3], weirdnessParameter, 0.0F, resourcekey3);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdnessParameter, 0.0F, resourcekey);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[5], weirdnessParameter, 0.0F, resourcekey5);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdnessParameter, 0.0F, resourcekey4);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdnessParameter, 0.0F, resourcekey);
            }
        }

    }

    private void addHighSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer, Climate.Parameter weirdnessParameter) {
        for(int temperatureIndex = 0; temperatureIndex < this.temperatures.length; ++temperatureIndex) {
            Climate.Parameter temperatureParameter = this.temperatures[temperatureIndex];

            for(int hudmidityIndex = 0; hudmidityIndex < this.humidities.length; ++hudmidityIndex) {
                Climate.Parameter humidityParameter = this.humidities[hudmidityIndex];
                ResourceKey<Biome> resourcekey = this.pickMiddleBiome(temperatureIndex, hudmidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey1 = this.pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, hudmidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey2 = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, hudmidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey3 = this.pickPlateauBiome(temperatureIndex, hudmidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey4 = this.pickExtremeHillsBiome(temperatureIndex, hudmidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey5 = this.maybePickShatteredBiome(temperatureIndex, hudmidityIndex, weirdnessParameter, resourcekey);
                ResourceKey<Biome> resourcekey6 = this.pickSlopeBiome(temperatureIndex, hudmidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey7 = this.pickPeakBiome(temperatureIndex, hudmidityIndex, weirdnessParameter);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdnessParameter, 0.0F, resourcekey);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, this.nearInlandContinentalness, this.erosions[0], weirdnessParameter, 0.0F, resourcekey6);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdnessParameter, 0.0F, resourcekey7);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, this.nearInlandContinentalness, this.erosions[1], weirdnessParameter, 0.0F, resourcekey2);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[1], weirdnessParameter, 0.0F, resourcekey6);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdnessParameter, 0.0F, resourcekey);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[2], weirdnessParameter, 0.0F, resourcekey3);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, this.midInlandContinentalness, this.erosions[3], weirdnessParameter, 0.0F, resourcekey1);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, this.farInlandContinentalness, this.erosions[3], weirdnessParameter, 0.0F, resourcekey3);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdnessParameter, 0.0F, resourcekey);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[5], weirdnessParameter, 0.0F, resourcekey5);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdnessParameter, 0.0F, resourcekey4);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[6], weirdnessParameter, 0.0F, resourcekey);
            }
        }

    }

    private void addMidSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer, Climate.Parameter weirdnessParameter) {
        this.addSurfaceBiome(pairConsumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdnessParameter, 0.0F, Biomes.STONY_SHORE);
        this.addSurfaceBiome(pairConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdnessParameter, 0.0F, Biomes.SWAMP);

        for(int i = 0; i < this.temperatures.length; ++i) {
            Climate.Parameter climate$parameter = this.temperatures[i];

            for(int j = 0; j < this.humidities.length; ++j) {
                Climate.Parameter climate$parameter1 = this.humidities[j];
                ResourceKey<Biome> resourcekey = this.pickMiddleBiome(i, j, weirdnessParameter);
                ResourceKey<Biome> resourcekey1 = this.pickMiddleBiomeOrBadlandsIfHot(i, j, weirdnessParameter);
                ResourceKey<Biome> resourcekey2 = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(i, j, weirdnessParameter);
                ResourceKey<Biome> resourcekey3 = this.pickExtremeHillsBiome(i, j, weirdnessParameter);
                ResourceKey<Biome> resourcekey4 = this.pickPlateauBiome(i, j, weirdnessParameter);
                ResourceKey<Biome> resourcekey5 = this.pickBeachBiome(i, j);
                ResourceKey<Biome> resourcekey6 = this.maybePickShatteredBiome(i, j, weirdnessParameter, resourcekey);
                ResourceKey<Biome> resourcekey7 = this.pickShatteredCoastBiome(i, j, weirdnessParameter);
                ResourceKey<Biome> resourcekey8 = this.pickSlopeBiome(i, j, weirdnessParameter);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[0], weirdnessParameter, 0.0F, resourcekey8);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.midInlandContinentalness), this.erosions[1], weirdnessParameter, 0.0F, resourcekey2);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.farInlandContinentalness, this.erosions[1], weirdnessParameter, 0.0F, i == 0 ? resourcekey8 : resourcekey4);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, this.erosions[2], weirdnessParameter, 0.0F, resourcekey);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.midInlandContinentalness, this.erosions[2], weirdnessParameter, 0.0F, resourcekey1);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.farInlandContinentalness, this.erosions[2], weirdnessParameter, 0.0F, resourcekey4);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.nearInlandContinentalness), this.erosions[3], weirdnessParameter, 0.0F, resourcekey);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[3], weirdnessParameter, 0.0F, resourcekey1);
                if (weirdnessParameter.max() < 0L) {
                    this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[4], weirdnessParameter, 0.0F, resourcekey5);
                    this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdnessParameter, 0.0F, resourcekey);
                } else {
                    this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), this.erosions[4], weirdnessParameter, 0.0F, resourcekey);
                }

                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[5], weirdnessParameter, 0.0F, resourcekey7);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, this.erosions[5], weirdnessParameter, 0.0F, resourcekey6);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdnessParameter, 0.0F, resourcekey3);
                if (weirdnessParameter.max() < 0L) {
                    this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[6], weirdnessParameter, 0.0F, resourcekey5);
                } else {
                    this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[6], weirdnessParameter, 0.0F, resourcekey);
                }

                if (i == 0) {
                    this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdnessParameter, 0.0F, resourcekey);
                }
            }
        }

    }

    private void addLowSlice(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer, Climate.Parameter weirdnessParameter) {
        this.addSurfaceBiome(pairConsumer, this.FULL_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[2]), weirdnessParameter, 0.0F, Biomes.STONY_SHORE);
        this.addSurfaceBiome(pairConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdnessParameter, 0.0F, Biomes.SWAMP);

        for(int temperatureIndex = 0; temperatureIndex < this.temperatures.length; ++temperatureIndex) {
            Climate.Parameter climate$parameter = this.temperatures[temperatureIndex];

            for(int humidityIndex = 0; humidityIndex < this.humidities.length; ++humidityIndex) {
                Climate.Parameter climate$parameter1 = this.humidities[humidityIndex];
                ResourceKey<Biome> resourcekey = this.pickMiddleBiome(temperatureIndex, humidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey1 = this.pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey2 = this.pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(temperatureIndex, humidityIndex, weirdnessParameter);
                ResourceKey<Biome> resourcekey3 = this.pickBeachBiome(temperatureIndex, humidityIndex);
                ResourceKey<Biome> resourcekey4 = this.maybePickShatteredBiome(temperatureIndex, humidityIndex, weirdnessParameter, resourcekey);
                ResourceKey<Biome> resourcekey5 = this.pickShatteredCoastBiome(temperatureIndex, humidityIndex, weirdnessParameter);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdnessParameter, 0.0F, resourcekey1);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdnessParameter, 0.0F, resourcekey2);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdnessParameter, 0.0F, resourcekey);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[3]), weirdnessParameter, 0.0F, resourcekey1);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, Climate.Parameter.span(this.erosions[3], this.erosions[4]), weirdnessParameter, 0.0F, resourcekey3);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[4], weirdnessParameter, 0.0F, resourcekey);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[5], weirdnessParameter, 0.0F, resourcekey5);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.nearInlandContinentalness, this.erosions[5], weirdnessParameter, 0.0F, resourcekey4);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), this.erosions[5], weirdnessParameter, 0.0F, resourcekey);
                this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, this.coastContinentalness, this.erosions[6], weirdnessParameter, 0.0F, resourcekey3);
                if (temperatureIndex == 0) {
                    this.addSurfaceBiome(pairConsumer, climate$parameter, climate$parameter1, Climate.Parameter.span(this.nearInlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdnessParameter, 0.0F, resourcekey);
                }
            }
        }

    }

    private void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer, Climate.Parameter weirdnessParameter) {
        this.addSurfaceBiome(pairConsumer, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdnessParameter, 0.0F, weirdnessParameter.max() < 0L ? Biomes.STONY_SHORE : Biomes.FROZEN_RIVER);
        this.addSurfaceBiome(pairConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdnessParameter, 0.0F, weirdnessParameter.max() < 0L ? Biomes.STONY_SHORE : Biomes.RIVER);
        this.addSurfaceBiome(pairConsumer, this.FROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdnessParameter, 0.0F, Biomes.FROZEN_RIVER);
        this.addSurfaceBiome(pairConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, this.nearInlandContinentalness, Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdnessParameter, 0.0F, Biomes.RIVER);
        this.addSurfaceBiome(pairConsumer, this.FROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), weirdnessParameter, 0.0F, Biomes.FROZEN_RIVER);
        this.addSurfaceBiome(pairConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.coastContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[2], this.erosions[5]), weirdnessParameter, 0.0F, Biomes.RIVER);
        this.addSurfaceBiome(pairConsumer, this.FROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdnessParameter, 0.0F, Biomes.FROZEN_RIVER);
        this.addSurfaceBiome(pairConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, this.coastContinentalness, this.erosions[6], weirdnessParameter, 0.0F, Biomes.RIVER);
        this.addSurfaceBiome(pairConsumer, this.UNFROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdnessParameter, 0.0F, Biomes.SWAMP);
        this.addSurfaceBiome(pairConsumer, this.FROZEN_RANGE, this.FULL_RANGE, Climate.Parameter.span(this.inlandContinentalness, this.farInlandContinentalness), this.erosions[6], weirdnessParameter, 0.0F, Biomes.FROZEN_RIVER);

        for(int temperatureIndex = 0; temperatureIndex < this.temperatures.length; ++temperatureIndex) {
            Climate.Parameter temperatureParameter = this.temperatures[temperatureIndex];

            for(int hudmidityIndex = 0; hudmidityIndex < this.humidities.length; ++hudmidityIndex) {
                Climate.Parameter humidityParameter = this.humidities[hudmidityIndex];
                ResourceKey<Biome> resourcekey = this.pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, hudmidityIndex, weirdnessParameter);
                this.addSurfaceBiome(pairConsumer, temperatureParameter, humidityParameter, Climate.Parameter.span(this.midInlandContinentalness, this.farInlandContinentalness), Climate.Parameter.span(this.erosions[0], this.erosions[1]), weirdnessParameter, 0.0F, resourcekey);
            }
        }

    }

    private void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer) {
        this.addUndergroundBiome(pairConsumer, this.FULL_RANGE, this.FULL_RANGE, Climate.Parameter.span(0.8F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.DRIPSTONE_CAVES);
        this.addUndergroundBiome(pairConsumer, this.FULL_RANGE, Climate.Parameter.span(0.7F, 1.0F), this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, 0.0F, Biomes.LUSH_CAVES);
    }

    private ResourceKey<Biome> pickMiddleBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdnessParameter) {
        if (weirdnessParameter.max() < 0L) {
            return this.MIDDLE_BIOMES[temperatureIndex][humidityIndex];
        } else {
            ResourceKey<Biome> resourcekey = this.MIDDLE_BIOMES_VARIANT[temperatureIndex][humidityIndex];
            return resourcekey == null ? this.MIDDLE_BIOMES[temperatureIndex][humidityIndex] : resourcekey;
        }
    }

    private ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHot(int temperatureIndex, int humidityIndex, Climate.Parameter weirdnessParameter) {
        return temperatureIndex == 4 ? this.pickBadlandsBiome(humidityIndex, weirdnessParameter) : this.pickMiddleBiome(temperatureIndex, humidityIndex, weirdnessParameter);
    }

    private ResourceKey<Biome> pickMiddleBiomeOrBadlandsIfHotOrSlopeIfCold(int temperatureIndex, int humidityIndex, Climate.Parameter weirdnessParameter) {
        return temperatureIndex == 0 ? this.pickSlopeBiome(temperatureIndex, humidityIndex, weirdnessParameter) : this.pickMiddleBiomeOrBadlandsIfHot(temperatureIndex, humidityIndex, weirdnessParameter);
    }

    private ResourceKey<Biome> maybePickShatteredBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdnessParameter, ResourceKey<Biome> biomeResourceKey) {
        return temperatureIndex > 1 && humidityIndex < 4 && weirdnessParameter.max() >= 0L ? Biomes.WINDSWEPT_SAVANNA : biomeResourceKey;
    }

    private ResourceKey<Biome> pickShatteredCoastBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdnessParameter) {
        ResourceKey<Biome> resourcekey = weirdnessParameter.max() >= 0L ? this.pickMiddleBiome(temperatureIndex, humidityIndex, weirdnessParameter) : this.pickBeachBiome(temperatureIndex, humidityIndex);
        return this.maybePickShatteredBiome(temperatureIndex, humidityIndex, weirdnessParameter, resourcekey);
    }

    private ResourceKey<Biome> pickBeachBiome(int temperatureIndex, int humidityIndex) {
        return switch (temperatureIndex) {
            case 0 -> Biomes.SNOWY_BEACH;
            case 3 -> BiomeRegistry.TROPICS_RESOURCE_KEY;
            case 4 -> Biomes.DESERT;
            default -> Biomes.BEACH;
        };
    }

    private ResourceKey<Biome> pickBadlandsBiome(int humidityIndex, Climate.Parameter weirdnessParameter) {
        if (humidityIndex < 2) {
            return weirdnessParameter.max() < 0L ? Biomes.ERODED_BADLANDS : Biomes.BADLANDS;
        } else {
            return humidityIndex < 3 ? Biomes.BADLANDS : Biomes.WOODED_BADLANDS;
        }
    }

    private ResourceKey<Biome> pickPlateauBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdnessParameter) {
        if (weirdnessParameter.max() < 0L) {
            return this.PLATEAU_BIOMES[temperatureIndex][humidityIndex];
        } else {
            ResourceKey<Biome> resourcekey = this.PLATEAU_BIOMES_VARIANT[temperatureIndex][humidityIndex];
            return resourcekey == null ? this.PLATEAU_BIOMES[temperatureIndex][humidityIndex] : resourcekey;
        }
    }

    private ResourceKey<Biome> pickPeakBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdnessParameter) {
        if (temperatureIndex <= 2) {
            return weirdnessParameter.max() < 0L ? Biomes.JAGGED_PEAKS : Biomes.FROZEN_PEAKS;
        } else {
            return temperatureIndex == 3 ? Biomes.STONY_PEAKS : this.pickBadlandsBiome(humidityIndex, weirdnessParameter);
        }
    }

    private ResourceKey<Biome> pickSlopeBiome(int temperatureIndex, int humidityIndex, Climate.Parameter weirdnessParameter) {
        if (temperatureIndex >= 3) {
            return this.pickPlateauBiome(temperatureIndex, humidityIndex, weirdnessParameter);
        } else {
            return humidityIndex <= 1 ? Biomes.SNOWY_SLOPES : Biomes.GROVE;
        }
    }

    private ResourceKey<Biome> pickExtremeHillsBiome(int temperatureIndex, int humidityIndex, Climate.Parameter p_187251_) {
        ResourceKey<Biome> resourcekey = this.EXTREME_HILLS[temperatureIndex][humidityIndex];
        return resourcekey == null ? this.pickMiddleBiome(temperatureIndex, humidityIndex, p_187251_) : resourcekey;
    }

    private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer, Climate.Parameter temperatureParameter, Climate.Parameter humidityParameter, Climate.Parameter continentalnessParameter, Climate.Parameter erosionParameter, Climate.Parameter weirdnessParameter, float offset, ResourceKey<Biome> biomeResourceKey) {
        pairConsumer.accept(Pair.of(Climate.parameters(temperatureParameter, humidityParameter, continentalnessParameter, erosionParameter, Climate.Parameter.point(0.0F), weirdnessParameter, offset), biomeResourceKey));
        pairConsumer.accept(Pair.of(Climate.parameters(temperatureParameter, humidityParameter, continentalnessParameter, erosionParameter, Climate.Parameter.point(1.0F), weirdnessParameter, offset), biomeResourceKey));
    }

    private void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> pairConsumer, Climate.Parameter temperatureParameter, Climate.Parameter humidityParameter, Climate.Parameter continentalnessParameter, Climate.Parameter erosionParameter, Climate.Parameter weirdnessParameter, float offset, ResourceKey<Biome> biomeResourceKey) {
        pairConsumer.accept(Pair.of(Climate.parameters(temperatureParameter, humidityParameter, continentalnessParameter, erosionParameter, Climate.Parameter.span(0.2F, 0.9F), weirdnessParameter, offset), biomeResourceKey));
    }
}