package com.bedmen.odyssey.world;

import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.effect.TemperatureSource;
import com.bedmen.odyssey.world.gen.biome.weather.OdysseyPrecipitation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
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
        boolean correctClimate = precipitation == Biome.Precipitation.RAIN && temperature > 0.15f && temperature < 1.0f;
        return correctBiomeCategory && correctClimate;
    }

    public static List<TemperatureSource> getTemperatureSourceList(Player player){
        if(player.isCreative() || player.isSpectator()){
            return List.of();
        }
        BlockPos blockPos = player.blockPosition();
        Biome biome =  player.level.getBiome(blockPos).value();
        Biome.BiomeCategory biomeCategory = biome.getBiomeCategory();
        if(biomeCategory == Biome.BiomeCategory.NETHER){
            return TemperatureSource.NETHER_LIST;
        }
        if(biomeCategory == Biome.BiomeCategory.THEEND){
            return List.of();
        }
        List<TemperatureSource> temperatureSourceList = new ArrayList<>();
        if(player.getY() > 48){
            temperatureSourceList.add(TemperatureSource.SUN.withMultiplier(sunLightMultiplier(player)));
            if(isSnowingAtLivingEntity(player)){
                temperatureSourceList.add(TemperatureSource.SNOW_WEATHER);
            }
            if(biomeCategory == Biome.BiomeCategory.DESERT){
                temperatureSourceList.add(TemperatureSource.DESERT);
            } else if(biomeCategory == Biome.BiomeCategory.MESA){
                temperatureSourceList.add(TemperatureSource.MESA);
            }
            if(biome.getPrecipitation() == OdysseyPrecipitation.BLIZZARD){
                temperatureSourceList.add(TemperatureSource.BLIZZARD);
            } else if(biome.coldEnoughToSnow(blockPos)){
                temperatureSourceList.add(TemperatureSource.COLD_BIOME);
            }
        }
        return temperatureSourceList;
    }

    private static float sunLightMultiplier(LivingEntity livingEntity){
        BlockPos blockPos = livingEntity.blockPosition();
        return Aspects.getSunBoost(blockPos, livingEntity.level) * skyLightMultiplier(livingEntity);
    }

    private static float skyLightMultiplier(LivingEntity livingEntity){
        BlockPos blockPos = livingEntity.blockPosition();
        return livingEntity.level.getBrightness(LightLayer.SKY, blockPos) / 15f;
    }

    private static boolean isSnowingAtLivingEntity(LivingEntity livingEntity){
        BlockPos blockPos = livingEntity.blockPosition();
        Level level = livingEntity.level;
        if (!level.isRaining()) {
            return false;
        } else if (!level.canSeeSky(blockPos)) {
            return false;
        } else if (level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos).getY() > blockPos.getY()) {
            return false;
        } else {
            Biome biome = level.getBiome(blockPos).value();
            return biome.getPrecipitation() == Biome.Precipitation.SNOW && biome.coldEnoughToSnow(blockPos);
        }
    }

    public static boolean isInBlizzard(LivingEntity livingEntity){
        return livingEntity.level.getBiome(livingEntity.blockPosition()).value().getPrecipitation() == OdysseyPrecipitation.BLIZZARD;
    }

}
