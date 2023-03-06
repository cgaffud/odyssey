package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.OdysseyAbstractSkeletonModel;
import com.bedmen.odyssey.entity.monster.OdysseySkeleton;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class OdysseySkeletonRenderer extends OdysseyAbstractSkeletonRenderer<OdysseySkeleton, OdysseyAbstractSkeletonModel<OdysseySkeleton>>{
    public OdysseySkeletonRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    public OdysseySkeletonRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2) {
        super(context, new OdysseyAbstractSkeletonModel<>(context.bakeLayer(modelLayerLocation)), new OdysseyAbstractSkeletonModel<>(context.bakeLayer(modelLayerLocation1)), new OdysseyAbstractSkeletonModel<>(context.bakeLayer(modelLayerLocation2)));
    }
}
