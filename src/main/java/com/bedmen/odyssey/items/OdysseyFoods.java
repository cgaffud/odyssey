package com.bedmen.odyssey.items;

import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;

public class OdysseyFoods {
    public static final Food LIFE_FRUIT = (new Food.Builder()).nutrition(4).saturationMod(0.3F).alwaysEat().effect(() -> new EffectInstance(EffectRegistry.LIFE_INCREASE.get(), 1, 0, false, false, false), 1.0f).build();
}
