package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.AbandonedIronGolemModel;
import com.bedmen.odyssey.client.renderer.entity.layer.AbandonedIronGolemCrackinessLayer;
import com.bedmen.odyssey.entity.boss.AbandonedIronGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AbandonedIronGolemRenderer extends MobRenderer<AbandonedIronGolem, AbandonedIronGolemModel<AbandonedIronGolem>> {
    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/abandoned_iron_golem/golem.png");

    public AbandonedIronGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new AbandonedIronGolemModel<>(context.bakeLayer(AbandonedIronGolemModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new AbandonedIronGolemCrackinessLayer(this));
    }

    public ResourceLocation getTextureLocation(AbandonedIronGolem abandonedIronGolem) {
        return GOLEM_LOCATION;
    }

    protected void setupRotations(AbandonedIronGolem abandonedIronGolem, PoseStack poseStack, float p_115016_, float p_115017_, float p_115018_) {
        super.setupRotations(abandonedIronGolem, poseStack, p_115016_, p_115017_, p_115018_);
        if (!((double)abandonedIronGolem.animationSpeed < 0.01D)) {
            float f = 13.0F;
            float f1 = abandonedIronGolem.animationPosition - abandonedIronGolem.animationSpeed * (1.0F - p_115018_) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
        }
    }
}