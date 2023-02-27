package com.bedmen.odyssey.effect;

import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.EffectRenderer;

import java.util.List;

public class OdysseyEffect extends MobEffect {

    private boolean displayEffect;

    public OdysseyEffect(MobEffectCategory typeIn, int liquidColorIn, boolean displayEffect) {
        super(typeIn, liquidColorIn);
        this.displayEffect = displayEffect;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if ((this == EffectRegistry.BLEEDING.get())) {
            livingEntity.hurt(DamageSource.MAGIC, 1.0F);
        } else if (this instanceof FireEffect){
            livingEntity.clearFire();
            if(this == EffectRegistry.HEXFLAME.get()){
                livingEntity.hurt(DamageSource.ON_FIRE, 1.0F);
            }
        } else if(this == EffectRegistry.FREEZING.get()){
            TemperatureSource.getEffectTemperatureSource(false, amplifier).tick(livingEntity);
        } else if (this == EffectRegistry.SEARING.get()){
            TemperatureSource.getEffectTemperatureSource(true, amplifier).tick(livingEntity);
        } else {
            super.applyEffectTick(livingEntity, amplifier);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if(this == EffectRegistry.BLEEDING.get()){
            int tickRate = 100 / (amplifier+3);
            if(tickRate > 1){
                return duration % tickRate == 0;
            } else {
                return true;
            }
            // TODO soulflame
        } else if(this == EffectRegistry.HEXFLAME.get()){
            int tickRate = 120 / (amplifier+3);
            if(tickRate > 1){
                return duration % tickRate == 0;
            } else {
                return true;
            }
        } else if(this == EffectRegistry.FREEZING.get() || this == EffectRegistry.SEARING.get()){
            return true;
        }
        return super.isDurationEffectTick(duration, amplifier);
    }

    public List<ItemStack> getCurativeItems() {
        return this instanceof FireEffect ? List.of() : super.getCurativeItems();
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