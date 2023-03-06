package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.client.model.StrayBruteModel;
import com.bedmen.odyssey.entity.monster.StrayBrute;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StrayBruteClothingLayer extends RenderLayer<StrayBrute, StrayBruteModel> {
    private static final ResourceLocation STRAY_CLOTHES_LOCATION = new ResourceLocation("textures/entity/skeleton/stray_overlay.png");
    private final StrayBruteModel layerModel;

    public StrayBruteClothingLayer(RenderLayerParent<StrayBrute, StrayBruteModel> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.layerModel = new StrayBruteModel(entityModelSet.bakeLayer(ModelLayers.STRAY_OUTER_LAYER));
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int p_117555_, StrayBrute strayBrute, float p_117557_, float p_117558_, float p_117559_, float p_117560_, float p_117561_, float p_117562_) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, STRAY_CLOTHES_LOCATION, poseStack, multiBufferSource, p_117555_, strayBrute, p_117557_, p_117558_, p_117560_, p_117561_, p_117562_, p_117559_, 1.0F, 1.0F, 1.0F);
    }
}