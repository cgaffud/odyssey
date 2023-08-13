package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.OdysseyCreeperModel;
import com.bedmen.odyssey.client.renderer.entity.layer.OdysseyCreeperPowerLayer;
import com.bedmen.odyssey.entity.monster.OdysseyCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.function.Supplier;

public abstract class AbstractCreeperRenderer<T extends OdysseyCreeper, M extends OdysseyCreeperModel<T>> extends MobRenderer<T, M> {
    protected static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");

    public AbstractCreeperRenderer(EntityRendererProvider.Context context, Supplier<M> modelSupplier) {
        super(context, modelSupplier.get(), 0.5f);
        this.addLayer(new OdysseyCreeperPowerLayer(this, context.getModelSet(), modelSupplier));
    }

    protected void scale(OdysseyCreeper p_114046_, PoseStack p_114047_, float p_114048_) {
        float f = p_114046_.getSwelling(p_114048_);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        p_114047_.scale(f2, f3, f2);
    }

    protected float getWhiteOverlayProgress(OdysseyCreeper p_114043_, float p_114044_) {
        float f = p_114043_.getSwelling(p_114044_);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    public ResourceLocation getTextureLocation(OdysseyCreeper p_114041_) {
        return CREEPER_LOCATION;
    }
}