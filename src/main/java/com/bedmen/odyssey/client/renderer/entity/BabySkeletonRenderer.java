package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.entity.monster.BabySkeleton;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BabySkeletonRenderer extends HumanoidMobRenderer<BabySkeleton, SkeletonModel<BabySkeleton>> {
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    
    public BabySkeletonRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    public BabySkeletonRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation0, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2) {
        super(context, new SkeletonModel<>(context.bakeLayer(modelLayerLocation0)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel(context.bakeLayer(modelLayerLocation1)), new SkeletonModel(context.bakeLayer(modelLayerLocation2))));
    }

    public ResourceLocation getTextureLocation(BabySkeleton p_110775_1_) {
        return SKELETON_LOCATION;
    }
}