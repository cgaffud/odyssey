package com.bedmen.odyssey.potions;

import com.bedmen.odyssey.magic.ExperienceCost;
import net.minecraft.world.effect.MobEffectCategory;

public class XPDrainerEffect extends OdysseyEffect {

    public final ExperienceCost experienceCost;

    public XPDrainerEffect(MobEffectCategory typeIn, int liquidColorIn, boolean displayEffect, float xpCostPerSecond) {
        super(typeIn, liquidColorIn, displayEffect);

        // We will incur this per tick
        this.experienceCost = new ExperienceCost(xpCostPerSecond / 20);
    }
}
