package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.layer.SkeletonOuterLayer;
import com.bedmen.odyssey.entity.monster.EncasedSkeleton;
import com.bedmen.odyssey.entity.monster.OdysseySkeleton;
import com.bedmen.odyssey.entity.monster.OvergrownSkeleton;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;

public class OvergrownSkeletonRenderer extends OdysseySkeletonRenderer {
    private static final ResourceLocation OVERGROWN_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/skeleton/overgrown.png");

    public OvergrownSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.STRAY, ModelLayers.STRAY_INNER_ARMOR, ModelLayers.STRAY_OUTER_ARMOR);
        this.addLayer(new SkeletonOuterLayer<>(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(OdysseySkeleton p_116049_) {
        return OVERGROWN_LOCATION;
    }
}
