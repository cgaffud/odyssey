package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.projectile.SonicBoom;
import com.bedmen.odyssey.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SonicBoomRenderer<T extends SonicBoom> extends EntityRenderer<T> {
    public static final ResourceLocation SONIC_BOOM_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/sonic_boom.png");
    public SonicBoomRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(T sonicBoom, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, sonicBoom.yRotO, sonicBoom.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, sonicBoom.xRotO, sonicBoom.getXRot())));

        poseStack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        poseStack.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack.translate(-4.0D, 0.0D, 0.0D);
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityCutout(this.getTextureLocation(sonicBoom)));

        RenderUtil.xShapeProjectileVertices(poseStack, vertexconsumer, 9, 14, 0, 0, 9f/32f, 14f/32f, packedLight);

        poseStack.popPose();
        super.render(sonicBoom, yRot, partialTicks, poseStack, multiBufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T sonicBoom) {
        return SONIC_BOOM_LOCATION;
    }

}