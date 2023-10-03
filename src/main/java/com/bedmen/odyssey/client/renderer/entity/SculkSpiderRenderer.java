package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.SculkSpiderModel;
import com.bedmen.odyssey.entity.monster.SculkSpider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SculkSpiderRenderer<T extends SculkSpider> extends MobRenderer<T, SculkSpiderModel<T>> {
    private static final ResourceLocation SCULK_SPIDER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/spider/sculk_spider.png");

    public SculkSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new SculkSpiderModel<>(context.bakeLayer(SculkSpiderModel.LAYER_LOCATION)), 0.8F);
    }

    protected float getFlipDegrees(T p_116011_) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(SculkSpider sculkSpider) {
        return SCULK_SPIDER_LOCATION;
    }
}
