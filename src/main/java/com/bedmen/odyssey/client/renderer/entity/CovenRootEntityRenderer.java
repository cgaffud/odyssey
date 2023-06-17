package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.CovenRootModel;
import com.bedmen.odyssey.entity.boss.coven.CovenRootEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CovenRootEntityRenderer extends EntityRenderer<CovenRootEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/coven/coven_root.png");
    private final CovenRootModel<CovenRootEntity> model;

    public CovenRootEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CovenRootModel<>(context.bakeLayer(CovenRootModel.LAYER_LOCATION));
    }

    public void render(CovenRootEntity covenRootEntity, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        float f = covenRootEntity.getAnimationProgress(partialTicks);
        if (f != 0.0F) {
            float f1 = 2.0F;
            if (f > 0.9F) {
                f1 *= (1.0F - f) / 0.1F;
            }

            poseStack.pushPose();
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F - covenRootEntity.getYRot()));
            poseStack.scale(-f1, -f1, f1);
            float f2 = 0.03125F;
            poseStack.translate(0.0D, -0.276D, 0.0D);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            this.model.setupAnim(covenRootEntity, f, 0.0F, 0.0F, covenRootEntity.getYRot(), covenRootEntity.getXRot());
            VertexConsumer vertexconsumer = multiBufferSource.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
            super.render(covenRootEntity, yRot, partialTicks, poseStack, multiBufferSource, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CovenRootEntity covenRootEntity) {
        return TEXTURE_LOCATION;
    }
}