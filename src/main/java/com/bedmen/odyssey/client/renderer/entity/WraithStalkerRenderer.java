package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.WraithStalkerModel;
import com.bedmen.odyssey.entity.monster.WraithStalker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class WraithStalkerRenderer<T extends WraithStalker> extends MobRenderer<T, WraithStalkerModel<T>> {
    private static final ResourceLocation WRAITH_STALKER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/wraith/stalker.png");

    public WraithStalkerRenderer(EntityRendererProvider.Context context) {
        super(context, new WraithStalkerModel<>(context.bakeLayer(WraithStalkerModel.LAYER_LOCATION)), 0.8F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    protected void scale(T entity, PoseStack poseStack, float partialTicks) {
        float f = entity.getScale();
        poseStack.scale(f,f,f);
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(T pEntity) {
        return WRAITH_STALKER_LOCATION;
    }
}
