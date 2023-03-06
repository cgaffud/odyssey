package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.OdysseyAbstractSkeletonModel;
import com.bedmen.odyssey.entity.monster.OdysseyStray;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.StrayClothingLayer;

public class OdysseyStrayRenderer extends OdysseyAbstractStrayRenderer<OdysseyStray, OdysseyAbstractSkeletonModel<OdysseyStray>>{
    public OdysseyStrayRenderer(EntityRendererProvider.Context context) {
        super(context, new OdysseyAbstractSkeletonModel<>(context.bakeLayer(ModelLayers.STRAY)), new OdysseyAbstractSkeletonModel<>(context.bakeLayer(ModelLayers.STRAY_INNER_ARMOR)), new OdysseyAbstractSkeletonModel<>(context.bakeLayer(ModelLayers.STRAY_OUTER_ARMOR)));
        this.addLayer(new StrayClothingLayer<>(this, context.getModelSet()));
    }
}
