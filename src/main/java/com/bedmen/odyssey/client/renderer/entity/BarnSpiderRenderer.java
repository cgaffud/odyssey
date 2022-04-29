package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.monster.BarnSpider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BarnSpiderRenderer extends SpiderRenderer<BarnSpider> {
    private static final ResourceLocation BARN_SPIDER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/spider/barn_spider.png");
    private static final float SCALE = 0.7F;

    public BarnSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.CAVE_SPIDER);
        this.shadowRadius *= SCALE;
    }

    protected void scale(BarnSpider barnSpider, PoseStack poseStack, float partialTicks) {
        poseStack.scale(SCALE, SCALE, SCALE);
    }

    public ResourceLocation getTextureLocation(BarnSpider barnSpider) {
        return BARN_SPIDER_LOCATION;
    }
}