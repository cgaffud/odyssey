package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.WeaverModel;
import com.bedmen.odyssey.client.model.WraithModel;
import com.bedmen.odyssey.entity.monster.Weaver;
import com.bedmen.odyssey.entity.monster.Wraith;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class WraithRenderer<T extends Wraith> extends MobRenderer<T, WraithModel<T>> {
    private static final ResourceLocation WRAITH_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/wraith.png");

    public WraithRenderer(EntityRendererProvider.Context context) {
        super(context, new WraithModel<>(context.bakeLayer(WraithModel.LAYER_LOCATION)), 0.8F);
        this.addLayer(new ItemInHandLayer<>(this));
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