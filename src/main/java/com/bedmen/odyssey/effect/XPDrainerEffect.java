package com.bedmen.odyssey.effect;

import com.bedmen.odyssey.effect.FireEffect;
import com.bedmen.odyssey.effect.OdysseyEffect;
import com.bedmen.odyssey.effect.TemperatureSource;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.magic.ExperienceCost;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class XPDrainerEffect extends OdysseyEffect {

    public final ExperienceCost experienceCost;

    public XPDrainerEffect(MobEffectCategory typeIn, int liquidColorIn, boolean displayEffect, float xpCostPerSecond) {
        super(typeIn, liquidColorIn, displayEffect);

        // We will incur this per tick
        this.experienceCost = new ExperienceCost(xpCostPerSecond / 20);
    }

    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (this.experienceCost.canPay(serverPlayer, amplifier+1))
                this.experienceCost.pay(serverPlayer, amplifier+1);
            else
                serverPlayer.hurt(DamageSource.MAGIC, Float.MAX_VALUE);
        } else {
            super.applyEffectTick(livingEntity, amplifier);
        }
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return OdysseyEffect.commonDurationEffectTick(duration, amplifier);
    }
}
