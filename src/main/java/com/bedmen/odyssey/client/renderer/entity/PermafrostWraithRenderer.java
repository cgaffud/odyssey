package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.PermafrostWraithModel;
import com.bedmen.odyssey.entity.boss.permafrost.PermafrostWraith;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class PermafrostWraithRenderer<T extends PermafrostWraith> extends MobRenderer<T, PermafrostWraithModel<T>> {
    private static final ResourceLocation WRAITH_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/permafrost/wraith.png");

    public PermafrostWraithRenderer(EntityRendererProvider.Context context) {
        super(context, new PermafrostWraithModel<>(context.bakeLayer(PermafrostWraithModel.LAYER_LOCATION)), 0.8F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    protected void scale(T entity, PoseStack poseStack, float partialTicks) {
        float f = entity.getScale();
        poseStack.scale(f,f,f);
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(T pEntity) {
        return WRAITH_LOCATION;
    }
}