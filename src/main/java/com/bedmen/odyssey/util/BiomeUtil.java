package com.bedmen.odyssey.util;

import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.effect.TemperatureSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BiomeUtil {

    public static class Climate {
        public float temperature;
        public float downfall;
        public Climate(float temperature, float downfall){
            this.temperature = temperature;
            this.downfall = downfall;
        }
    }

    private static final Map<ResourceKey<Biome>, Climate> CLIMATE_MAP = new HashMap<>();

    public static void init(){
        for(Map.Entry<ResourceKey<Biome>, Biome> entry : ForgeRegistries.BIOMES.getEntries()) {
            Biome biome = entry.getValue();
            CLIMATE_MAP.put(entry.getKey(), new Climate(biome.getBaseTemperature(), biome.getDownfall()));
        }

        CLIMATE_MAP.put(Biomes.WARM_OCEAN, new Climate(1f, 0.6f));
        CLIMATE_MAP.put(Biomes.DEEP_LUKEWARM_OCEAN, new Climate(0.75f, 0.6f));
        CLIMATE_MAP.put(Biomes.LUKEWARM_OCEAN, new Climate(0.75f, 0.6f));
        CLIMATE_MAP.put(Biomes.DEEP_OCEAN, new Climate(0.5f, 0.6f));
        CLIMATE_MAP.put(Biomes.OCEAN, new Climate(0.5f, 0.6f));
        CLIMATE_MAP.put(Biomes.DEEP_COLD_OCEAN, new Climate(0.25f, 0.6f));
        CLIMATE_MAP.put(Biomes.COLD_OCEAN, new Climate(0.25f, 0.6f));
        CLIMATE_MAP.put(Biomes.DEEP_FROZEN_OCEAN, new Climate(0.0f, 0.6f));
        CLIMATE_MAP.put(Biomes.FROZEN_OCEAN, new Climate(0.0f, 0.6f));

        CLIMATE_MAP.put(Biomes.LUSH_CAVES, new Climate(0.8f, 0.8f));
        CLIMATE_MAP.put(Biomes.DRIPSTONE_CAVES, new Climate(0.4f, 0.8f));
    }

    public static Climate getClimate(Holder<Biome> biomeHolder) {
        Optional<ResourceKey<Biome>> maybeResourceKey = biomeHolder.unwrapKey();
        if (maybeResourceKey.isPresent()) {
            return CLIMATE_MAP.get(maybeResourceKey.get());
        }
        return new Climate(biomeHolder.value().getBaseTemperature(), biomeHolder.value().getDownfall());
    }

    public static int getFoliageColor(Level level, Entity entity) {
        double temperatureFactor = Aspects.getHotBoost(entity.eyeBlockPosition(), level);
        double downfallFactor = Aspects.getHumidBoost(entity.eyeBlockPosition(), level);
        return FoliageColor.get(temperatureFactor, downfallFactor);
    }

    private static final Map<Double, Integer> ARID_COLOR_MAP = new ConcurrentHashMap<>();
    public static int getAridColor(Level level, Entity entity) {
        double temperatureFactor = Aspects.getHotBoost(entity.eyeBlockPosition(), level);
        double downfallFactor = Aspects.getHumidBoost(entity.eyeBlockPosition(), level);
        double adjustedDownfallFactor = Mth.clamp(downfallFactor, 0, temperatureFactor);
        double key = 2 * temperatureFactor + adjustedDownfallFactor;
        if(ARID_COLOR_MAP.containsKey(key)){
            return ARID_COLOR_MAP.get(key);
        }
        int color = Color.getHSBColor(0.072f, (float) (temperatureFactor * 0.8f), (float) (1.0f - 0.6f * adjustedDownfallFactor)).getRGB();
        ARID_COLOR_MAP.put(key, color);
        return color;
    }

    private static final Map<Double, Integer> COLD_COLOR_MAP = new ConcurrentHashMap<>();
    public static int getColdColor(Level level, Entity entity) {
        double coldFactor = Aspects.getColdBoost(entity.eyeBlockPosition(), level);
        if(COLD_COLOR_MAP.containsKey(coldFactor)){
            return COLD_COLOR_MAP.get(coldFactor);
        }
        int color = Color.getHSBColor((float) (0.67f - 0.07f * coldFactor), (float) (0.6f - 0.1f * coldFactor), (float) (0.6f + 0.4f * coldFactor)).getRGB();
        COLD_COLOR_MAP.put(coldFactor, color);
        return color;
    }

    public static boolean hasGoodPlantClimate(Biome.BiomeCategory biomeCategory, Biome.Precipitation precipitation, float temperature){
        boolean correctBiomeCategory = switch (biomeCategory){
            case TAIGA, EXTREME_HILLS, JUNGLE, PLAINS, FOREST, SWAMP -> true;
            default -> false;
        };
        boolean correctClimate = precipitation == Biome.Precipitation.RAIN && temperature > 0.1f && temperature < 1.0f;
        return correctBiomeCategory && correctClimate;
    }

    public static List<TemperatureSource> getTemperatureSourceList(LivingEntity livingEntity){
        Holder<Biome> biomeHolder = livingEntity.level.getBiome(livingEntity.blockPosition());
        Biome.BiomeCategory biomeCategory = biomeHolder.value().getBiomeCategory();
        if(biomeCategory == Biome.BiomeCategory.NETHER){
            return TemperatureSource.NETHER;
        }
        if(biomeCategory == Biome.BiomeCategory.THEEND){
            return List.of();
        }
        List<TemperatureSource> temperatureSourceList = new ArrayList<>();
        temperatureSourceList.add(TemperatureSource.SUN.withMultiplier(sunLight(livingEntity)));
        if(biomeCategory == Biome.BiomeCategory.DESERT){
            temperatureSourceList.add(TemperatureSource.DESERT);
        } else if(biomeCategory == Biome.BiomeCategory.MESA){
            temperatureSourceList.add(TemperatureSource.MESA);
        }
        return temperatureSourceList;
    }

    private static float sunLight(LivingEntity livingEntity){
        BlockPos blockPos = livingEntity.blockPosition();
        return Aspects.getSunBoost(blockPos, livingEntity.level) * livingEntity.level.getBrightness(LightLayer.SKY, blockPos) / 15f;
    }
}
