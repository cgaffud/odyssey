package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.BladeSpiderModel;
import com.bedmen.odyssey.entity.monster.BarnSpider;
import com.bedmen.odyssey.entity.monster.BladeSpider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BladeSpiderRenderer<T extends BladeSpider> extends MobRenderer<T, BladeSpiderModel<T>> {
    private static final ResourceLocation BLADE_SPIDER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/spider/blade_spider.png");
    private static final float SCALE = 1.5F;

    public BladeSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new BladeSpiderModel<>(context.bakeLayer(BladeSpiderModel.LAYER_LOCATION)),0.8F);
        this.shadowRadius *= SCALE;
    }

    protected void scale(T entity, PoseStack poseStack, float partialTicks) {
        poseStack.scale(SCALE,SCALE,SCALE);
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }


    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return BLADE_SPIDER_LOCATION;
    }
    public ResourceLocation getTextureLocation(BarnSpider barnSpider) {
        return BLADE_SPIDER_LOCATION;
    }
}