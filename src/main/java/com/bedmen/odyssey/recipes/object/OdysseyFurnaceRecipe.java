package com.bedmen.odyssey.recipes.object;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public abstract class OdysseyFurnaceRecipe implements Recipe<Container> {
    public abstract float getExperience();
    public abstract int getCookingTime();
}
