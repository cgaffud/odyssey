package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.ArmedCovenWitchModel;
import com.bedmen.odyssey.entity.boss.coven.NetherWitch;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NetherWitchRenderer extends MobRenderer<NetherWitch, ArmedCovenWitchModel<NetherWitch>> {
    private static final ResourceLocation WITCH_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/coven/nether_witch.png");
    private static final ResourceLocation ENRAGED_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/coven/enraged_nether_witch.png");


    public NetherWitchRenderer(EntityRendererProvider.Context context) {
        super(context, new ArmedCovenWitchModel<>(context.bakeLayer(ArmedCovenWitchModel.LAYER_LOCATION)), 0.5F);
    }

    public ResourceLocation getTextureLocation(NetherWitch netherWitch) {
        return netherWitch.isEnraged ? ENRAGED_LOCATION : WITCH_LOCATION;
    }

    public void render(NetherWitch netherWitch, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        super.render(netherWitch, yRot, partialTicks, poseStack, multiBufferSource, packedLight);
    }

    protected void scale(NetherWitch netherWitch, PoseStack poseStack, float partialTicks) {
        float f = 0.9375F;
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}