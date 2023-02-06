package com.bedmen.odyssey.items.odyssey_versions;

import net.minecraft.world.food.FoodProperties;

public class OdysseyFood {
    public static final FoodProperties COCONUT_COOKIE = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).build();
    public static final FoodProperties PERMABUFF = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.3F).alwaysEat().build();

}
