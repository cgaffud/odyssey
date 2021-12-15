package com.bedmen.odyssey.potions;

import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class OdysseyEffect extends MobEffect{

    public OdysseyEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (this == EffectRegistry.BLEEDING.get()) {
            livingEntity.hurt(DamageSource.MAGIC, amplifier);
        } else {
            super.applyEffectTick(livingEntity, amplifier);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if (this == EffectRegistry.BLEEDING.get()) {
            return true;
        }
        return super.isDurationEffectTick(duration, amplifier);
    }
}