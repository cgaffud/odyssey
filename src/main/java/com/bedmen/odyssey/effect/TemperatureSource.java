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
    public static final TemperatureSource POWDERED_SNOW = new TemperatureSource(-0.18f / 20f, Optional.of(0.5f));
    public static final TemperatureSource SNOW_WEATHER = new TemperatureSource(-0.02f / 20f);
    public static final TemperatureSource COLD_BIOME = new TemperatureSource(-0.06f / 20f);
    public static final TemperatureSource SUN = new TemperatureSource(0.02f / 20f);
    public static final TemperatureSource DESERT = new TemperatureSource(0.03f / 20f);
    public static final TemperatureSource MESA = new TemperatureSource(0.04f / 20f);
    public static final List<TemperatureSource> NETHER = List.of(
            new TemperatureSource(0.5f / 20f, Optional.of(1.0f)),
            new TemperatureSource(0.08f / 20f)
    );

    public static final float TEMPERATURE_PER_DAMAGE = 0.25f;

    public final float temperaturePerTick;
    public final Optional<Float> protectionForImmunity;

    public TemperatureSource(float temperaturePerTick){
        this(temperaturePerTick, Optional.empty());
    }

    public TemperatureSource(float temperaturePerTick, Optional<Float> protectionForImmunity){
        this.temperaturePerTick = temperaturePerTick;
        this.protectionForImmunity = protectionForImmunity;
    }

    public boolean isHot(){
        return this.temperaturePerTick > 0f;
    }

    public TemperatureSource withMultiplier(float multiplier){
        return new TemperatureSource(this.temperaturePerTick * multiplier, this.protectionForImmunity);
    }

    public void tick(LivingEntity livingEntity){
        float protectionStrength = AspectUtil.getProtectionAspectStrength(livingEntity, this.damageSource());
        if(this.protectionForImmunity.isPresent() &&  protectionStrength >= this.protectionForImmunity.get()){
            return;
        }
        if(this.isHot() && livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)){
            return;
        }
        float protectionFactor = this.protectionForImmunity.map(value -> (value - protectionStrength) / value).orElse(1.0f);
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            float temperature = odysseyLivingEntity.getTemperature();
            float changeInTemperature = this.temperaturePerTick * protectionFactor;
            odysseyLivingEntity.setTemperature(temperature + changeInTemperature);
        }
    }

    public DamageSource damageSource(){
        return damageSource(this.isHot());
    }

    public static DamageSource damageSource(boolean isHot){
        return isHot ? OdysseyDamageSource.HEAT_EXHAUSTION : DamageSource.FREEZE;
    }

    public static float getHotFactor(boolean isHot){
        return isHot ? 1f : -1f;
    }

    public static void normalizeTemperature(OdysseyLivingEntity odysseyLivingEntity, float amount){
        float temperature = odysseyLivingEntity.getTemperature();
        boolean isHot = temperature > 0f;
        float newAbsoluteTemperature = Float.max(0.0f, Mth.abs(temperature) - amount);
        float newTemperature = newAbsoluteTemperature * getHotFactor(isHot);
        odysseyLivingEntity.setTemperature(newTemperature);
    }

    public static TemperatureSource getHarmfulEffectTemperatureSource(boolean isHot, int amplifier){
        return new TemperatureSource(1/160f * (float)(1 << amplifier) * getHotFactor(isHot), Optional.of(4f * (amplifier + 1)));
    }

}
