package com.bedmen.odyssey.potions;

import com.bedmen.odyssey.util.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class OdysseyEffect extends Effect{

    public OdysseyEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (this == EffectRegistry.BLEEDING.get()){
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