package com.bedmen.odyssey.client.renderer;

import com.bedmen.odyssey.client.model.StrayBruteModel;
import com.bedmen.odyssey.client.model.ZombieBruteModel;
import com.bedmen.odyssey.client.renderer.entity.OdysseyAbstractStrayRenderer;
import com.bedmen.odyssey.client.renderer.entity.layer.StrayBruteClothingLayer;
import com.bedmen.odyssey.entity.monster.StrayBrute;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StrayBruteRenderer extends OdysseyAbstractStrayRenderer<StrayBrute, StrayBruteModel> {
    public StrayBruteRenderer(EntityRendererProvider.Context context) {
        super(context, new StrayBruteModel(context.bakeLayer(ModelLayers.STRAY)), new StrayBruteModel(context.bakeLayer(ModelLayers.STRAY_INNER_ARMOR)), new StrayBruteModel(context.bakeLayer(ModelLayers.STRAY_OUTER_ARMOR)));
        this.addLayer(new StrayBruteClothingLayer(this, context.getModelSet()));
    }

    protected void scale(StrayBrute strayBrute, PoseStack poseStack, float partialTicks) {
        poseStack.scale(ZombieBruteModel.BODY_SCALE, ZombieBruteModel.BODY_SCALE, ZombieBruteModel.BODY_SCALE);
    }
}
