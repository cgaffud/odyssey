package com.bedmen.odyssey.potions;

import com.bedmen.odyssey.util.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import java.io.Console;

public class ModEffect extends Effect{

    public ModEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    // lol idk what the second thing is
    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (this == EffectRegistry.BLEEDING.get()){
            livingEntity.hurt(DamageSource.MAGIC, amplifier);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return (this == EffectRegistry.BLEEDING.get());
    }
}