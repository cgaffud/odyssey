package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.ArmedCovenWitchModel;
import com.bedmen.odyssey.entity.boss.coven.OverworldWitch;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WitchModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.WitchItemLayer;
import net.minecraft.resources.ResourceLocation;

public class OverworldWitchRenderer extends MobRenderer<OverworldWitch, ArmedCovenWitchModel<OverworldWitch>> {
    private static final ResourceLocation WITCH_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/coven/overworld_witch.png");
    private static final ResourceLocation ENRAGED_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/coven/enraged_overworld_witch.png");

    public OverworldWitchRenderer(EntityRendererProvider.Context context) {
        super(context, new ArmedCovenWitchModel<>(context.bakeLayer(ArmedCovenWitchModel.LAYER_LOCATION)), 0.5F);
    }

    public ResourceLocation getTextureLocation(OverworldWitch overworldWitch) {
        if (overworldWitch.isEnraged())
            return ENRAGED_LOCATION;
        return WITCH_LOCATION;
    }

    public void render(OverworldWitch overworldWitch, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        super.render(overworldWitch, yRot, partialTicks, poseStack, multiBufferSource, packedLight);
    }

    protected void scale(OverworldWitch overworldWitch, PoseStack poseStack, float partialTicks) {
        float f = 0.9375F;
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}