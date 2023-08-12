package com.bedmen.odyssey.util;

import com.bedmen.odyssey.effect.FireType;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RenderUtil {

    public static FireType getStrongestFireType(LivingEntity livingEntity){
        if(livingEntity instanceof OdysseyLivingEntity odysseyLivingEntity){
            return odysseyLivingEntity.getFireType();
        }
        return FireType.NONE;
    }

    public static void renderFireTypeBlockOverlay(PoseStack poseStack, FireType fireType) {
        if(fireType.isNone()){
            return;
        }
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
            BufferUploader.drawWithShader(bufferbuilder.end());
            poseStack.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
    }

    public static void renderFireTypeExternalView(LivingEntity livingEntity, FireType fireType, PoseStack poseStack, MultiBufferSource multiBufferSource){
        if(fireType.isNone()){
            return;
        }
        TextureAtlasSprite sprite0 = fireType.material0.sprite();
        TextureAtlasSprite sprite1 = fireType.material1.sprite();
        poseStack.pushPose();
        float f = livingEntity.getBbWidth() * 1.4F;
        poseStack.scale(f, f, f);
        float f1 = 0.5F;
        float f3 = livingEntity.getBbHeight() / f;
        float f4 = 0.0F;
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-entityRenderDispatcher.camera.getYRot()));
        poseStack.translate(0.0D, 0.0D, -0.3F + (float)((int)f3) * 0.02F);
        float f5 = 0.0F;
        int i = 0;
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(Sheets.translucentCullBlockSheet());

        for(PoseStack.Pose posestack$pose = poseStack.last(); f3 > 0.0F; ++i) {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? sprite0 : sprite1;
            float u0 = textureatlassprite2.getU0();
            float v0 = textureatlassprite2.getV0();
            float u1 = textureatlassprite2.getU1();
            float v1 = textureatlassprite2.getV1();
            if (i / 2 % 2 == 0) {
                float f10 = u1;
                u1 = u0;
                u0 = f10;
            }

            fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 0.0F - f4, f5, u1, v1);
            fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 0.0F - f4, f5, u0, v1);
            fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 1.4F - f4, f5, u0, v0);
            fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 1.4F - f4, f5, u1, v0);
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

    public static void xShapeProjectileVertices(PoseStack poseStack, VertexConsumer vertexConsumer, float x, float y, float u, float v, float du, float dv, int packedLight){
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        for(int j = 0; j < 4; ++j) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            xShapeProjectileVertex(matrix4f, matrix3f, vertexConsumer, -x/2f, -y/2f, 0, u, v, packedLight);
            xShapeProjectileVertex(matrix4f, matrix3f, vertexConsumer, x/2f, -y/2f, 0, u+du, v, packedLight);
            xShapeProjectileVertex(matrix4f, matrix3f, vertexConsumer, x/2f, y/2f, 0, u+du, v+dv, packedLight);
            xShapeProjectileVertex(matrix4f, matrix3f, vertexConsumer, -x/2f, y/2f, 0, u, v+dv, packedLight);
        }
    }

    private static void xShapeProjectileVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, int packedLight) {
        vertexConsumer.vertex(matrix4f, x, y, z).color(255, 255, 255, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix3f, 0, 1, 0).endVertex();
    }

    public static ItemInHandRenderer.HandRenderSelection evaluateWhichHandsToRender(LocalPlayer localPlayer) {
        ItemStack itemstack = localPlayer.getMainHandItem();
        ItemStack itemstack1 = localPlayer.getOffhandItem();
        boolean flag = itemstack.is(OdysseyItemTags.BOWS) || itemstack1.is(OdysseyItemTags.BOWS);
        boolean flag1 = itemstack.is(OdysseyItemTags.CROSSBOWS) || itemstack1.is(OdysseyItemTags.CROSSBOWS);
        if (!flag && !flag1) {
            return ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        } else if (localPlayer.isUsingItem()) {
            return selectionUsingItemWhileHoldingBowLike(localPlayer);
        } else {
            return isChargedCrossbow(itemstack) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        }
    }

    private static ItemInHandRenderer.HandRenderSelection selectionUsingItemWhileHoldingBowLike(LocalPlayer localPlayer) {
        ItemStack itemstack = localPlayer.getUseItem();
        InteractionHand interactionhand = localPlayer.getUsedItemHand();
        if (!itemstack.is(OdysseyItemTags.BOWS) && !itemstack.is(OdysseyItemTags.CROSSBOWS)) {
            return interactionhand == InteractionHand.MAIN_HAND && isChargedCrossbow(localPlayer.getOffhandItem()) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        } else {
            return ItemInHandRenderer.HandRenderSelection.onlyForHand(interactionhand);
        }
    }

    private static boolean isChargedCrossbow(ItemStack itemStack) {
        return itemStack.is(OdysseyItemTags.CROSSBOWS) && CrossbowItem.isCharged(itemStack);
    }

}
