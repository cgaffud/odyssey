package com.bedmen.odyssey.util;

import com.bedmen.odyssey.potions.FireEffect;
import com.bedmen.odyssey.potions.FireType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.Comparator;
import java.util.Optional;

public class RenderUtil {

    public static Optional<FireType> getStrongestFire(LivingEntity livingEntity){
        return livingEntity.getActiveEffects().stream()
                .filter(mobEffectInstance -> mobEffectInstance.getEffect() instanceof FireEffect)
                .map(mobEffectInstance -> ((FireEffect) mobEffectInstance.getEffect()).fireType).min(Comparator.comparingInt(Enum::ordinal));
    }

    public static void renderBlockOverlayModdedFire(PoseStack poseStack, FireType fireType) {
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableTexture();
        TextureAtlasSprite sprite = fireType.material0.sprite();
        RenderSystem.setShaderTexture(0, sprite.atlas().location());
        float f = sprite.getU0();
        float f1 = sprite.getU1();
        float f2 = (f + f1) / 2.0F;
        float f3 = sprite.getV0();
        float f4 = sprite.getV1();
        float f5 = (f3 + f4) / 2.0F;
        float f6 = sprite.uvShrinkRatio();
        float f7 = Mth.lerp(f6, f, f2);
        float f8 = Mth.lerp(f6, f1, f2);
        float f9 = Mth.lerp(f6, f3, f5);
        float f10 = Mth.lerp(f6, f4, f5);
        float f11 = 1.0F;

        for(int i = 0; i < 2; ++i) {
            poseStack.pushPose();
            poseStack.translate((double)((float)(-(i * 2 - 1)) * 0.24F), (double)-0.3F, 0.0D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees((float)(i * 2 - 1) * 10.0F));
            Matrix4f matrix4f = poseStack.last().pose();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            bufferbuilder.vertex(matrix4f, -0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f8, f10).endVertex();
            bufferbuilder.vertex(matrix4f, 0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f7, f10).endVertex();
            bufferbuilder.vertex(matrix4f, 0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f7, f9).endVertex();
            bufferbuilder.vertex(matrix4f, -0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f8, f9).endVertex();
            bufferbuilder.end();
            BufferUploader.end(bufferbuilder);
            poseStack.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }

    public static void renderExternalViewModdedFire(LivingEntity livingEntity, FireType fireType, PoseStack poseStack, MultiBufferSource multiBufferSource){
        TextureAtlasSprite sprite0 = fireType.material0.sprite();
        TextureAtlasSprite sprite1 = fireType.material1.sprite();
        poseStack.pushPose();
        float f = livingEntity.getBbWidth() * 1.4F;
        poseStack.scale(f, f, f);
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = livingEntity.getBbHeight() / f;
        float f4 = 0.0F;
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-entityRenderDispatcher.camera.getYRot()));
        poseStack.translate(0.0D, 0.0D, (double)(-0.3F + (float)((int)f3) * 0.02F));
        float f5 = 0.0F;
        int i = 0;
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(Sheets.cutoutBlockSheet());

        for(PoseStack.Pose posestack$pose = poseStack.last(); f3 > 0.0F; ++i) {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? sprite0 : sprite1;
            float f6 = textureatlassprite2.getU0();
            float f7 = textureatlassprite2.getV0();
            float f8 = textureatlassprite2.getU1();
            float f9 = textureatlassprite2.getV1();
            if (i / 2 % 2 == 0) {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }

            fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 0.0F - f4, f5, f8, f9);
            fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 0.0F - f4, f5, f6, f9);
            fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 1.4F - f4, f5, f6, f7);
            fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 1.4F - f4, f5, f8, f7);
            f3 -= 0.45F;
            f4 -= 0.45F;
            f1 *= 0.9F;
            f5 += 0.03F;
        }

        poseStack.popPose();
    }

    private static void fireVertex(PoseStack.Pose pose, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v) {
        vertexConsumer.vertex(pose.pose(), x, y, z).color(255, 255, 255, 255).uv(u, v).overlayCoords(0, 10).uv2(240).normal(pose.normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }

}
