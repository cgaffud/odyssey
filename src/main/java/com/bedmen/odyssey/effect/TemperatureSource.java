package com.bedmen.odyssey.effect;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class TemperatureSource {
    public static final TemperatureSource POWDERED_SNOW = new TemperatureSource(false, 1f/160f, 0.5f);

    public static final float TEMPERATURE_PER_DAMAGE = 0.25f;

    public final boolean isHot;
    public final float temperaturePerTick;
    public final float protectionForImmunity;

    public TemperatureSource(boolean isHot, float temperaturePerTick, float protectionForImmunity){
        this.isHot = isHot;
        this.temperaturePerTick = temperaturePerTick;
        this.protectionForImmunity = protectionForImmunity;
    }

    public void tick(LivingEntity livingEntity){
        float protectionStrength = AspectUtil.getProtectionAspectStrength(livingEntity, this.damageSource());
        if(protectionStrength >= this.protectionForImmunity){
            return;
        }
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            float temperature = odysseyLivingEntity.getTemperature();
            float changeInTemperature = (float) (this.temperaturePerTick * Math.pow(0.5, protectionStrength / 4f)) * this.getHotFactor();
            odysseyLivingEntity.setTemperature(temperature + changeInTemperature);
            odysseyLivingEntity.setTemperatureAffected(true);
        }
    }

    public DamageSource damageSource(){
        return damageSource(this.isHot);
    }

    public static DamageSource damageSource(boolean isHot){
        return isHot ? DamageSource.ON_FIRE : DamageSource.FREEZE;
    }

    public float getHotFactor(){
        return getHotFactor(this.isHot);
    }

    public static float getHotFactor(boolean isHot){
        return isHot ? 1f : -1f;
    }

    public static void reduceTemperature(OdysseyLivingEntity odysseyLivingEntity, float amount){
        float temperature = odysseyLivingEntity.getTemperature();
        boolean isHot = temperature > 0f;
        float newAbsoluteTemperature = Float.max(0.0f, Mth.abs(temperature) - amount);
        float newTemperature = newAbsoluteTemperature * getHotFactor(isHot);
        odysseyLivingEntity.setTemperature(newTemperature);
    }

    public static TemperatureSource getEffectTemperatureSource(boolean isHot, int amplifier){
        return new TemperatureSource(isHot, 1/160f * (float)(1 << amplifier), 4f * (amplifier + 1));
    }

}
