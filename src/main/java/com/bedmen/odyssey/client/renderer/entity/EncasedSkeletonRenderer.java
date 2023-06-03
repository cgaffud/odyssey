package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.renderer.entity.layer.SkeletonOuterLayer;
import com.bedmen.odyssey.entity.monster.EncasedSkeleton;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.layers.StrayClothingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class EncasedSkeletonRenderer extends SkeletonRenderer {
    private static final ResourceLocation ENCASED_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public EncasedSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.STRAY, ModelLayers.STRAY_INNER_ARMOR, ModelLayers.STRAY_OUTER_ARMOR);
        this.addLayer(new SkeletonOuterLayer<>(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(EncasedSkeleton p_116049_) {
        return ENCASED_LOCATION;
    }
}
