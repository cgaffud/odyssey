package com.bedmen.odyssey.effect;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.damagesource.OdysseyDamageSource;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.tags.OdysseyEntityTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public class TemperatureSource {
    public static final float ONE_PERCENT_PER_SECOND = 0.0005f;
    public static final float STABILIZATION_RATE = 4f * ONE_PERCENT_PER_SECOND;

    public static final TemperatureSource POWDERED_SNOW = temperaturePercentPerSecondSource(-14f, Optional.of(0.5f));
    public static final TemperatureSource WATER_OR_RAIN = temperaturePercentPerSecondSource(-1f);
    public static final TemperatureSource SNOW_WEATHER = temperaturePercentPerSecondSource(-2f);
    public static final TemperatureSource PERMAFROST_PASSIVE = temperaturePercentPerSecondSource(-4f);
    public static final TemperatureSource PERMAFROST_DEATH = temperaturePercentPerSecondSource(-12f);
    public static final TemperatureSource COLD_BIOME = temperaturePercentPerSecondSource(-6f);
    public static final TemperatureSource BLIZZARD = temperaturePercentPerSecondSource(-8f);

    public static final TemperatureSource ON_FIRE = temperaturePercentPerSecondSource(10f);
    public static final TemperatureSource SUN = temperaturePercentPerSecondSource(2f);
    public static final TemperatureSource DESERT = temperaturePercentPerSecondSource(3f);
    public static final TemperatureSource MESA = temperaturePercentPerSecondSource(5f);
    public static final List<TemperatureSource> NETHER_LIST = List.of(
            temperaturePercentPerSecondSource(50f, Optional.of(1.0f)),
            temperaturePercentPerSecondSource(8f)
    );

    public static final float TEMPERATURE_PER_DAMAGE = 0.25f;

    public final float temperaturePerTick;
    public final Optional<Float> protectionForImmunity;

    public TemperatureSource(float temperaturePerTick, Optional<Float> protectionForImmunity){
        this.temperaturePerTick = temperaturePerTick;
        this.protectionForImmunity = protectionForImmunity;
    }

    public static TemperatureSource temperaturePercentPerSecondSource(float temperaturePercentPerSecond){
        return temperaturePercentPerSecondSource(temperaturePercentPerSecond, Optional.empty());
    }

    public static TemperatureSource temperaturePercentPerSecondSource(float temperaturePercentPerSecond, Optional<Float> protectionForImmunity){
        return new TemperatureSource(temperaturePercentPerSecond * ONE_PERCENT_PER_SECOND, protectionForImmunity);
    }


    public boolean isHot(){
        return this.temperaturePerTick > 0f;
    }

    public TemperatureSource withMultiplier(float multiplier){
        return new TemperatureSource(this.temperaturePerTick * multiplier, this.protectionForImmunity);
    }

    public void tick(LivingEntity livingEntity){
        float temperatureProtectionStrength;
        if(this.isHot()){
            temperatureProtectionStrength = AspectUtil.getTotalAspectStrength(livingEntity, Aspects.COOLING);
        } else {
            temperatureProtectionStrength = AspectUtil.getTotalAspectStrength(livingEntity, Aspects.WARMTH);
        }
        if(this.protectionForImmunity.isPresent() &&  temperatureProtectionStrength >= this.protectionForImmunity.get()){
            return;
        }
        if(this.isHot() && livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)){
            return;
        }
        float protectionFactor = this.protectionForImmunity.map(value -> (value - temperatureProtectionStrength) / value).orElse(1.0f);
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            float temperature = odysseyLivingEntity.getTemperature();
            float changeInTemperature = this.temperaturePerTick * protectionFactor;
            EntityType<?> entityType = livingEntity.getType();
            if(this.isHot()){
                if(livingEntity.fireImmune() || entityType.is(OdysseyEntityTags.OVERHEATING_IMMUNE)){
                    return;
                }
            } else {
                if(entityType.is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)){
                    return;
                }
                if(entityType.is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)){
                    changeInTemperature *= 2.0f;
                }
            }
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

    public static void stabilizeTemperatureNaturally(OdysseyLivingEntity odysseyLivingEntity){
        stabilizeTemperature(odysseyLivingEntity, STABILIZATION_RATE);
    }

    public static void stabilizeTemperature(OdysseyLivingEntity odysseyLivingEntity, float temperatureChange){
        if(odysseyLivingEntity.isHot()){
            addHelpfulTemperature(odysseyLivingEntity, -temperatureChange);
        } else {
            addHelpfulTemperature(odysseyLivingEntity, temperatureChange);
        }
    }

    public static void addHelpfulTemperature(OdysseyLivingEntity odysseyLivingEntity, float temperatureChange){
        if(!odysseyLivingEntity.isCold() && !odysseyLivingEntity.isHot()){
            return;
        }
        if(odysseyLivingEntity.isHot() && temperatureChange > 0){
            return;
        }
        if(odysseyLivingEntity.isCold() && temperatureChange < 0){
            return;
        }
        float oldTemperature = odysseyLivingEntity.getTemperature();
        float newTemperature = oldTemperature + temperatureChange;
        boolean oldIsHot = oldTemperature > 0;
        boolean newIsHot = newTemperature > 0;
        if(oldIsHot != newIsHot){
            newTemperature = 0.0f;
        }
        odysseyLivingEntity.setTemperature(newTemperature);
    }

    public static TemperatureSource getHarmfulTemperatureEffectSource(boolean isHot, int amplifier){
        return temperaturePercentPerSecondSource(6.25f * (1 << amplifier) * getHotFactor(isHot), Optional.of(4f * (amplifier + 1)));
    }

    public static TemperatureSource getHarmfulTemperatureAspectSource(float strength){
        return temperaturePercentPerSecondSource(strength);
    }

}
