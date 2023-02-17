package com.bedmen.odyssey.potions;

import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

public class FireEffect extends OdysseyEffect {

    public final FireType fireType;

    public FireEffect(MobEffectCategory mobEffectCategory, int liquidColorIn, boolean displayEffect, FireType fireType) {
        super(mobEffectCategory, liquidColorIn, displayEffect);
        this.fireType = fireType;
    }

    public static MobEffectInstance getFireEffectInstance(MobEffect mobEffect, int duration, int amp){
        return new MobEffectInstance(mobEffect, duration,
                amp,false, false, false);
    }
}
