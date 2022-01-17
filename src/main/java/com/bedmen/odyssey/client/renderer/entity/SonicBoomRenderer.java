package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.projectile.SonicBoom;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SonicBoomRenderer<T extends SonicBoom> extends EntityRenderer<T> {
    public static final ResourceLocation SONIC_BOOM_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/abandoned_iron_golem/sonic_boom.png");
    public SonicBoomRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(T sonicBoom, float p_113840_, float p_113841_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_113844_) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(p_113841_, sonicBoom.yRotO, sonicBoom.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(p_113841_, sonicBoom.xRotO, sonicBoom.getXRot())));
        int i = 0;
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = 0.15625F;
        float f4 = 0.0F;
        float f5 = 0.15625F;
        float f6 = 0.15625F;
        float f7 = 0.3125F;
        float f8 = 0.05625F;

        poseStack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        poseStack.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack.translate(-4.0D, 0.0D, 0.0D);
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityCutout(this.getTextureLocation(sonicBoom)));
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();

        for(int j = 0; j < 4; ++j) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            this.vertex(matrix4f, matrix3f, vertexconsumer, -8, -8, 0, 0.0F, 0.0F, 0, 1, 0, p_113844_);
            this.vertex(matrix4f, matrix3f, vertexconsumer, 8, -8, 0, 1F, 0.0F, 0, 1, 0, p_113844_);
            this.vertex(matrix4f, matrix3f, vertexconsumer, 8, 8, 0, 1F, 1F, 0, 1, 0, p_113844_);
            this.vertex(matrix4f, matrix3f, vertexconsumer, -8, 8, 0, 0.0F, 1F, 0, 1, 0, p_113844_);
        }

        poseStack.popPose();
        super.render(sonicBoom, p_113840_, p_113841_, poseStack, multiBufferSource, p_113844_);
    }

    @Override
    public ResourceLocation getTextureLocation(T sonicBoom) {
        return SONIC_BOOM_LOCATION;
    }

    public void vertex(Matrix4f p_113826_, Matrix3f p_113827_, VertexConsumer p_113828_, int p_113829_, int p_113830_, int p_113831_, float p_113832_, float p_113833_, int p_113834_, int p_113835_, int p_113836_, int p_113837_) {
        p_113828_.vertex(p_113826_, (float)p_113829_, (float)p_113830_, (float)p_113831_).color(255, 255, 255, 255).uv(p_113832_, p_113833_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_113837_).normal(p_113827_, (float)p_113834_, (float)p_113836_, (float)p_113835_).endVertex();
    }
}