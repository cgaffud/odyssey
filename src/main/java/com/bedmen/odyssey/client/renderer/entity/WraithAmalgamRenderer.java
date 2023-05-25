package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.WraithAmalgamModel;
import com.bedmen.odyssey.entity.monster.WraithAmalgam;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class WraithAmalgamRenderer<T extends WraithAmalgam> extends MobRenderer<T, WraithAmalgamModel<T>> {
    private static final ResourceLocation WRAITH_AMALGAM_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/wraith/amalgam.png");

    public WraithAmalgamRenderer(EntityRendererProvider.Context context) {
        super(context, new WraithAmalgamModel<>(context.bakeLayer(WraithAmalgamModel.LAYER_LOCATION)), 0.8F);
        this.addLayer(new ItemInHandLayer<>(this));
    }

    protected void scale(T entity, PoseStack poseStack, float partialTicks) {
        float f = entity.getScale();
        poseStack.scale(f,f,f);
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(T pEntity) {
        return WRAITH_AMALGAM_LOCATION;
    }
}
