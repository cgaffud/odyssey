package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.OdysseyAbstractSkeletonModel;
import com.bedmen.odyssey.entity.monster.OdysseyAbstractSkeleton;
import com.bedmen.odyssey.entity.monster.OdysseySkeleton;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.layers.StrayClothingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseyStrayRenderer extends OdysseySkeletonRenderer {
    private static final ResourceLocation STRAY_SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/stray.png");

    public OdysseyStrayRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.STRAY, ModelLayers.STRAY_INNER_ARMOR, ModelLayers.STRAY_OUTER_ARMOR);
        this.addLayer(new StrayClothingLayer<>(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(OdysseyAbstractSkeleton odysseyAbstractSkeleton) {
        return STRAY_SKELETON_LOCATION;
    }
}