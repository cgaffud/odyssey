package com.bedmen.odyssey.effect;

import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class TemperatureEffect extends OdysseyEffect {

    protected final boolean isHot;

    public TemperatureEffect(MobEffectCategory typeIn, int liquidColorIn, boolean isHot) {
        super(typeIn, liquidColorIn, true);
        this.isHot = isHot;
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(this.getCategory() == MobEffectCategory.HARMFUL){
            TemperatureSource.getHarmfulTemperatureEffectSource(this.isHot, amplifier).tick(livingEntity);
        } else {
            if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
                TemperatureSource.addHelpfulTemperature(odysseyLivingEntity, (amplifier + 1) * 0.0005f * TemperatureSource.getHotFactor(this.isHot));
            }
        }
    }

    public static MobEffectInstance getTemperatureEffectInstance(MobEffect mobEffect, int duration, int amp, boolean ambient){
        return new MobEffectInstance(mobEffect, duration, amp, ambient, false, true);
    }
}
