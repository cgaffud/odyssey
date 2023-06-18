package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.ZombieBruteModel;
import com.bedmen.odyssey.entity.monster.ZombieBrute;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ZombieBruteRenderer extends AbstractZombieRenderer<ZombieBrute, ZombieBruteModel<ZombieBrute>> {
    private static final ResourceLocation ZOMBIE_BRUTE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");

    public ZombieBruteRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombieBruteModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), new ZombieBruteModel<>(context.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new ZombieBruteModel<>(context.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)));
    }

    protected void scale(ZombieBrute zombieBrute, PoseStack poseStack, float partialTicks) {
        poseStack.scale(ZombieBruteModel.BODY_SCALE, ZombieBruteModel.BODY_SCALE, ZombieBruteModel.BODY_SCALE);
    }

    public ResourceLocation getTextureLocation(ZombieBrute zombieBrute) {
        return ZOMBIE_BRUTE_LOCATION;
    }
}