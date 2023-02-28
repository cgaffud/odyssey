package com.bedmen.odyssey.effect;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.combat.damagesource.OdysseyDamageSource;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public class TemperatureSource {
    public static final TemperatureSource POWDERED_SNOW = new TemperatureSource(false, 0.18f, Optional.of(0.5f));
    public static final TemperatureSource SUN = new TemperatureSource(true, 0.02f);
    public static final TemperatureSource DESERT = new TemperatureSource(true, 0.03f);
    public static final TemperatureSource MESA = new TemperatureSource(true, 0.04f);
    public static final List<TemperatureSource> NETHER = List.of(
            new TemperatureSource(true, 0.5f, Optional.of(1.0f)),
            new TemperatureSource(true, 0.08f)
    );

    public static final float TEMPERATURE_PER_DAMAGE = 0.25f;

    public final boolean isHot;
    public final float temperaturePerTick;
    public final Optional<Float> protectionForImmunity;

    public TemperatureSource(boolean isHot, float temperaturePerSecond){
        this(isHot, temperaturePerSecond, Optional.empty());
    }

    public TemperatureSource(boolean isHot, float temperaturePerSecond, Optional<Float> protectionForImmunity){
        this.isHot = isHot;
        this.temperaturePerTick = temperaturePerSecond / 20f;
        this.protectionForImmunity = protectionForImmunity;
    }

    public TemperatureSource withMultiplier(float multiplier){
        return new TemperatureSource(this.isHot, this.temperaturePerTick * multiplier, this.protectionForImmunity);
    }

    public void tick(LivingEntity livingEntity){
        float protectionStrength = AspectUtil.getProtectionAspectStrength(livingEntity, this.damageSource());
        if(this.protectionForImmunity.isPresent() &&  protectionStrength >= this.protectionForImmunity.get()){
            return;
        }
        if(this.isHot && livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)){
            return;
        }
        float protectionFactor = this.protectionForImmunity.map(value -> (value - protectionStrength) / value).orElse(1.0f);
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            float temperature = odysseyLivingEntity.getTemperature();
            float changeInTemperature = this.temperaturePerTick * protectionFactor * this.getHotFactor();
            odysseyLivingEntity.setTemperature(temperature + changeInTemperature);
        }
    }

    public DamageSource damageSource(){
        return damageSource(this.isHot);
    }

    public static DamageSource damageSource(boolean isHot){
        return isHot ? OdysseyDamageSource.HEAT_EXHAUSTION : DamageSource.FREEZE;
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
        return new TemperatureSource(isHot, 1/160f * (float)(1 << amplifier), Optional.of(4f * (amplifier + 1)));
    }

}
