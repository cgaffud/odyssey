package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.WeaverModel;
import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.bedmen.odyssey.entity.monster.Weaver;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PassiveWeaverRenderer<T extends PassiveWeaver> extends MobRenderer<T, WeaverModel<T>> {
    private static final ResourceLocation WEAVER_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/weaver/passive.png");

    public PassiveWeaverRenderer(EntityRendererProvider.Context context) {
        super(context, new WeaverModel(context.bakeLayer(WeaverModel.LAYER_LOCATION)), 0.8F);
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
        return WEAVER_LOCATION;
    }
}