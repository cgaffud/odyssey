package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.ArmedCovenWitchModel;
import com.bedmen.odyssey.entity.boss.coven.EnderWitch;
import com.bedmen.odyssey.entity.boss.coven.NetherWitch;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WitchModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.WitchItemLayer;
import net.minecraft.resources.ResourceLocation;

public class NetherWitchRenderer extends MobRenderer<NetherWitch, ArmedCovenWitchModel<NetherWitch>> {
    private static final ResourceLocation WITCH_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/coven/nether_witch.png");
    private static final ResourceLocation ENRAGED_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/coven/enraged_nether_witch.png");


    public NetherWitchRenderer(EntityRendererProvider.Context p_174443_) {
        super(p_174443_, new ArmedCovenWitchModel<>(p_174443_.bakeLayer(ArmedCovenWitchModel.LAYER_LOCATION)), 0.5F);
    }

    public ResourceLocation getTextureLocation(NetherWitch netherWitch) {
        if (netherWitch.isEnraged())
            return ENRAGED_LOCATION;
        return WITCH_LOCATION;
    }

    public void render(NetherWitch p_116412_, float p_116413_, float p_116414_, PoseStack p_116415_, MultiBufferSource p_116416_, int p_116417_) {
        super.render(p_116412_, p_116413_, p_116414_, p_116415_, p_116416_, p_116417_);
    }

    protected void scale(NetherWitch p_116419_, PoseStack p_116420_, float p_116421_) {
        float f = 0.9375F;
        p_116420_.scale(0.9375F, 0.9375F, 0.9375F);
    }
}