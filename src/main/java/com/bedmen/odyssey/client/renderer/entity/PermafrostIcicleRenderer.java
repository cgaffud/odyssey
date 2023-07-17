package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.boss.permafrost.PermafrostIcicleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PermafrostIcicleRenderer extends EntityRenderer<PermafrostIcicleEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/projectiles/permafrost_icicle.png");

    public PermafrostIcicleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(PermafrostIcicleEntity icicleEntity, float p_225623_2_, float p_225623_3_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_225623_6_) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(p_225623_3_, icicleEntity.yRotO, icicleEntity.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(p_225623_3_, icicleEntity.xRotO, icicleEntity.getXRot())));
        float f9 = 0;
        if (f9 > 0.0F) {
            float f10 = -Mth.sin(f9 * 3.0F) * f9;
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(f10));
        }

        poseStack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        poseStack.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack.translate(-4.0D, 0.0D, 0.0D);
        VertexConsumer ivertexbuilder = multiBufferSource.getBuffer(RenderType.entityCutout(this.getTextureLocation(icicleEntity)));
        PoseStack.Pose matrixstack$entry = poseStack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        for(int j = 0; j < 3; j++){
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12+8*j, -2, -2, 0.0F+0.1875F*j, 0.1875F, -1, 0, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12+8*j, -2, 2, 0.1875F+0.1875F*j, 0.1875F, -1, 0, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12+8*j, 2, 2, 0.1875F+0.1875F*j, 0.375F, -1, 0, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12+8*j, 2, -2, 0.0F+0.1875F*j, 0.375F, -1, 0, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12+8*j, 2, -2, 0.0F+0.1875F*j, 0.1875F, 1, 0, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12+8*j, 2, 2, 0.1875F+0.1875F*j, 0.1875F, 1, 0, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12+8*j, -2, 2, 0.1875F+0.1875F*j, 0.375F, 1, 0, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12+8*j, -2, -2, 0.0F+0.1875F*j, 0.375F, 1, 0, 0, p_225623_6_);
        }

        for(int j = 0; j < 4; ++j) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12, -2, 0, 0.0F, 0.0F, 0, 1, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, 12, -2, 0, 0.75F, 0.0F, 0, 1, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, 12, 2, 0, 0.75F, 0.1875F, 0, 1, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12, 2, 0, 0.0F, 0.1875F, 0, 1, 0, p_225623_6_);
        }

        poseStack.popPose();
        super.render(icicleEntity, p_225623_2_, p_225623_3_, poseStack, multiBufferSource, p_225623_6_);
    }

    public void vertex(Matrix4f p_229039_1_, Matrix3f p_229039_2_, VertexConsumer p_229039_3_, int p_229039_4_, int p_229039_5_, int p_229039_6_, float xTextureOffset, float yTextureOffset, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_) {
        p_229039_3_.vertex(p_229039_1_, (float)p_229039_4_, (float)p_229039_5_, (float)p_229039_6_).color(255, 255, 255, 255).uv(xTextureOffset, yTextureOffset).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229039_12_).normal(p_229039_2_, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_).endVertex();
    }

    public ResourceLocation getTextureLocation(PermafrostIcicleEntity p_110775_1_) {
        return TEXTURE_LOCATION;
    }
}