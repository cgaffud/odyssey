package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.OdysseyAbstractSkeletonModel;
import com.bedmen.odyssey.client.model.OdysseySkeletonModel;
import com.bedmen.odyssey.entity.monster.OdysseyAbstractSkeleton;
import com.bedmen.odyssey.entity.monster.OdysseySkeleton;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseySkeletonRenderer extends HumanoidMobRenderer<OdysseyAbstractSkeleton, OdysseyAbstractSkeletonModel<OdysseyAbstractSkeleton>> {
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public OdysseySkeletonRenderer(Context context) {
        this(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    public OdysseySkeletonRenderer(Context context, ModelLayerLocation modelLayerLocation, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2) {
        super(context, new OdysseyAbstractSkeletonModel(context.bakeLayer(modelLayerLocation)), 0.5F);
        this.addLayer(new HumanoidArmorLayer(this, new OdysseyAbstractSkeletonModel(context.bakeLayer(modelLayerLocation1)), new OdysseyAbstractSkeletonModel(context.bakeLayer(modelLayerLocation2))));
    }

    public ResourceLocation getTextureLocation(OdysseyAbstractSkeleton odysseyAbstractSkeleton) {
        return SKELETON_LOCATION;
    }

    protected boolean isShaking(OdysseyAbstractSkeleton odysseyAbstractSkeleton) {
        return odysseyAbstractSkeleton.isShaking();
    }
}
