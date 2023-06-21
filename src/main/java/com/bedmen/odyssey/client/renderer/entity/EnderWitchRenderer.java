package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.coven.EnderWitch;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WitchModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.WitchItemLayer;
import net.minecraft.resources.ResourceLocation;

public class EnderWitchRenderer extends MobRenderer<EnderWitch, WitchModel<EnderWitch>> {
    private static final ResourceLocation WITCH_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/coven/ender_witch.png");
    private static final ResourceLocation ENRAGED_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/coven/enraged_ender_witch.png");


    public EnderWitchRenderer(EntityRendererProvider.Context context) {
        super(context, new WitchModel<>(context.bakeLayer(ModelLayers.WITCH)), 0.5F);
        this.addLayer(new WitchItemLayer<>(this, context.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(EnderWitch enderWitch) {
        return enderWitch.isEnraged ? ENRAGED_LOCATION : WITCH_LOCATION;
    }

    public void render(EnderWitch enderWitch, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        this.model.setHoldingItem(!enderWitch.getMainHandItem().isEmpty());
        super.render(enderWitch, yRot, partialTicks, poseStack, multiBufferSource, packedLight);
    }

    protected void scale(EnderWitch enderWitch, PoseStack poseStack, float partialTicks) {
        float f = 0.9375F;
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}