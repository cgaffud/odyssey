package com.bedmen.odyssey.food;

import com.bedmen.odyssey.effect.TemperatureEffect;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class OdysseyFood {
    public static final FoodProperties COCONUT_COOKIE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final FoodProperties SNOW_CONE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.2F).alwaysEat().effect(() -> TemperatureEffect.getTemperatureEffectInstance(EffectRegistry.COOLING.get(), 300, 1, false), 1.0f).build();
    public static final FoodProperties WARM_SOUP = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.2F).alwaysEat().effect(() -> TemperatureEffect.getTemperatureEffectInstance(EffectRegistry.WARMING.get(), 300, 1, false), 1.0f).build();
    public static final FoodProperties PERMABUFF = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.3F).alwaysEat().build();

}
