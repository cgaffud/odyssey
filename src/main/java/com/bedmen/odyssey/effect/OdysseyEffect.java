package com.bedmen.odyssey.effect;

import com.bedmen.odyssey.registry.EffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.EffectRenderer;

public class OdysseyEffect extends MobEffect {

    private boolean displayEffect;

    public OdysseyEffect(MobEffectCategory typeIn, int liquidColorIn, boolean displayEffect) {
        super(typeIn, liquidColorIn);
        this.displayEffect = displayEffect;
    }

    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if ((this == EffectRegistry.BLEEDING.get())) {
            livingEntity.hurt(DamageSource.MAGIC, 1.0F);
        } else {
            super.applyEffectTick(livingEntity, amplifier);
        }
    }

    /* TODO XPDrainerEffect code
        @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if ((this == EffectRegistry.BLEEDING.get()) && (livingEntity.tickCount % (100 / (amplifier + 3)) == 0)) {
            livingEntity.hurt(DamageSource.MAGIC, 1.0F);
        } else if (this == EffectRegistry.HEXFLAME.get()) {
            livingEntity.clearFire();
            if (livingEntity.tickCount % (120 / (amplifier + 3)) == 0)
                livingEntity.hurt(DamageSource.ON_FIRE, 1.0F);
        } else if ((this instanceof XPDrainerEffect xpDrainerEffect) && (livingEntity instanceof ServerPlayer serverPlayer)) {
            if (xpDrainerEffect.experienceCost.canPay(serverPlayer, amplifier+1))
                xpDrainerEffect.experienceCost.pay(serverPlayer, amplifier+1);
            else
                serverPlayer.hurt(DamageSource.MAGIC, Float.MAX_VALUE);
        } else {
            super.applyEffectTick(livingEntity, amplifier);
        }

    }

        @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if ((this == EffectRegistry.BLEEDING.get()) || this instanceof FireEffect || this instanceof XPDrainerEffect) {
            return true;
        }
        return super.isDurationEffectTick(duration, amplifier);
    }
    */

    public boolean isDurationEffectTick(int duration, int amplifier) {
        if(this == EffectRegistry.BLEEDING.get()){
            int tickRate = 100 / (amplifier+3);
            if(tickRate > 1){
                return duration % tickRate == 0;
            } else {
                return true;
            }
        }
        return super.isDurationEffectTick(duration, amplifier);
    }

    public static final EffectRenderer ODYSSEY_EFFECT_RENDERER = new EffectRenderer()
    {
        public void renderInventoryEffect(MobEffectInstance effectInstance, EffectRenderingInventoryScreen<?> gui, PoseStack poseStack, int x, int y, float z) {}

        public void renderHUDEffect(MobEffectInstance effectInstance, GuiComponent gui, PoseStack poseStack, int x, int y, float z, float alpha) {}

        public boolean shouldRender(MobEffectInstance effect)
        {
            return effect.getEffect() instanceof OdysseyEffect odysseyEffect && odysseyEffect.displayEffect;
        }

        public boolean shouldRenderHUD(MobEffectInstance effect)
        {
            return effect.getEffect() instanceof OdysseyEffect odysseyEffect && odysseyEffect.displayEffect;
        }
    };

    public Object getEffectRendererInternal() {
        return ODYSSEY_EFFECT_RENDERER;
    }
}