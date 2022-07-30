package com.bedmen.odyssey.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public static int getFoliageColor(Climate climate) {
        double d0 = Mth.clamp(climate.temperature, 0.0F, 1.0F);
        double d1 = Mth.clamp(climate.downfall, 0.0F, 1.0F);
        return FoliageColor.get(d0, d1);
    }
}
