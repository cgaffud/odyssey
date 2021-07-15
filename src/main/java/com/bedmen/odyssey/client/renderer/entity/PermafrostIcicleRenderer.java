package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.projectile.PermafrostIcicleEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PermafrostIcicleRenderer extends EntityRenderer<PermafrostIcicleEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/permafrost/icicle.png");
    //private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public PermafrostIcicleRenderer(EntityRendererManager p_i46129_1_) {
        super(p_i46129_1_);
    }

    public void render(PermafrostIcicleEntity icicleEntity, float p_225623_2_, float p_225623_3_, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {
        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(p_225623_3_, icicleEntity.yRotO, icicleEntity.yRot) - 90.0F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(p_225623_3_, icicleEntity.xRotO, icicleEntity.xRot)));
        float f9 = 0;
        if (f9 > 0.0F) {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(f10));
        }

        matrixStack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        matrixStack.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStack.translate(-4.0D, 0.0D, 0.0D);
        IVertexBuilder ivertexbuilder = renderTypeBuffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(icicleEntity)));
        MatrixStack.Entry matrixstack$entry = matrixStack.last();
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
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12, -2, 0, 0.0F, 0.0F, 0, 1, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, 12, -2, 0, 0.75F, 0.0F, 0, 1, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, 12, 2, 0, 0.75F, 0.1875F, 0, 1, 0, p_225623_6_);
            this.vertex(matrix4f, matrix3f, ivertexbuilder, -12, 2, 0, 0.0F, 0.1875F, 0, 1, 0, p_225623_6_);
        }

        matrixStack.popPose();
        super.render(icicleEntity, p_225623_2_, p_225623_3_, matrixStack, renderTypeBuffer, p_225623_6_);
    }

    public void vertex(Matrix4f p_229039_1_, Matrix3f p_229039_2_, IVertexBuilder p_229039_3_, int p_229039_4_, int p_229039_5_, int p_229039_6_, float xTextureOffset, float yTextureOffset, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_) {
        p_229039_3_.vertex(p_229039_1_, (float)p_229039_4_, (float)p_229039_5_, (float)p_229039_6_).color(255, 255, 255, 255).uv(xTextureOffset, yTextureOffset).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229039_12_).normal(p_229039_2_, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_).endVertex();
    }

    public ResourceLocation getTextureLocation(PermafrostIcicleEntity p_110775_1_) {
        return TEXTURE_LOCATION;
    }
}