package com.bedmen.odyssey.effect;

import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

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

    public boolean isDurationEffectTick(int duration, int amplifier) {
        // TODO soulflame
        if(this == EffectRegistry.HEXFLAME.get()){
            return OdysseyEffect.commonDurationEffectTick(duration, amplifier);
        }
        return super.isDurationEffectTick(duration, amplifier);
    }

    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.clearFire();
        // TODO soulflame
        if(this == EffectRegistry.HEXFLAME.get()){
            livingEntity.hurt(DamageSource.ON_FIRE, 1.0F);
        }
    }

    public List<ItemStack> getCurativeItems() {
        return List.of();
    }
}
