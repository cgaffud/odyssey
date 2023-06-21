package com.bedmen.odyssey.effect;

import com.bedmen.odyssey.registry.EffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

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

    public boolean isDurationEffectTick(int duration, int amplifier) {
        if(this == EffectRegistry.BLEEDING.get()){
            return OdysseyEffect.commonDurationEffectTick(duration, amplifier);
        }
        return super.isDurationEffectTick(duration, amplifier);
    }

    public static final IClientMobEffectExtensions ODYSSEY_EFFECT_RENDERER = new IClientMobEffectExtensions()
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

    public static boolean commonDurationEffectTick(int duration, int amplifier) {
        int tickRate = 120 / (amplifier+3);
        if(tickRate > 1){
            return duration % tickRate == 0;
        } else {
            return true;
        }
    }
}