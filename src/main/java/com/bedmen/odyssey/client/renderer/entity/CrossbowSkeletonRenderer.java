package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.CrossbowSkeletonModel;
import com.bedmen.odyssey.entity.monster.CrossbowSkeleton;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrossbowSkeletonRenderer extends HumanoidMobRenderer<CrossbowSkeleton, CrossbowSkeletonModel<CrossbowSkeleton>> {
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public CrossbowSkeletonRenderer(Context context) {
        this(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    public CrossbowSkeletonRenderer(Context context, ModelLayerLocation modelLayerLocation, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2) {
        super(context, new CrossbowSkeletonModel(context.bakeLayer(modelLayerLocation)), 0.5F);
        this.addLayer(new HumanoidArmorLayer(this, new CrossbowSkeletonModel(context.bakeLayer(modelLayerLocation1)), new CrossbowSkeletonModel(context.bakeLayer(modelLayerLocation2))));
    }

    public ResourceLocation getTextureLocation(CrossbowSkeleton crossbowSkeleton) {
        return SKELETON_LOCATION;
    }

    protected boolean isShaking(CrossbowSkeleton crossbowSkeleton) {
        return crossbowSkeleton.isShaking();
    }
}
