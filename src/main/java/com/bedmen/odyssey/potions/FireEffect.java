package com.bedmen.odyssey.potions;

import net.minecraft.world.effect.MobEffectCategory;

public class FireEffect extends OdysseyEffect {

    public final FireType fireType;

    public FireEffect(MobEffectCategory mobEffectCategory, int liquidColorIn, boolean displayEffect, FireType fireType) {
        super(mobEffectCategory, liquidColorIn, displayEffect);
        this.fireType = fireType;
    }
}
