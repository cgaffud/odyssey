package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.WeaverModel;
import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PassiveWeaverRenderer<T extends PassiveWeaver> extends MobRenderer<T, WeaverModel<T>> {
    private static final ResourceLocation WEAVER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/weaver/passive.png");

    public PassiveWeaverRenderer(EntityRendererProvider.Context context) {
        super(context, new WeaverModel(context.bakeLayer(WeaverModel.LAYER_LOCATION)), 0.8F);
    }

    protected void scale(T entity, PoseStack poseStack, float partialTicks) {
        float f = entity.getScale();
        poseStack.scale(f,f,f);
    }

    public void render(T entity, float p_116262_, float p_116263_, PoseStack p_116264_, MultiBufferSource p_116265_, int p_116266_) {
        if (entity.isBaby()) {
            this.shadowRadius *= 0.5F;
        }
        super.render(entity, p_116262_, p_116263_, p_116264_, p_116265_, p_116266_);
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(T pEntity) {
        return WEAVER_LOCATION;
    }
}