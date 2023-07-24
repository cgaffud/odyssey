package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.WraithModel;
import com.bedmen.odyssey.client.model.WraithlingModel;
import com.bedmen.odyssey.entity.boss.permafrost.Wraithling;
import com.bedmen.odyssey.entity.monster.Wraith;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class WraithlingRenderer<T extends Wraithling> extends MobRenderer<T, WraithlingModel<T>> {
    private static final ResourceLocation WRAITHLING_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/permafrost/wraithling.png");

    public WraithlingRenderer(EntityRendererProvider.Context context) {
        super(context, new WraithlingModel<>(context.bakeLayer(WraithlingModel.LAYER_LOCATION)), 0.8F);
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
        return WRAITHLING_LOCATION;
    }
}