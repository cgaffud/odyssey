package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.projectile.DripstoneShard;
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
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DripstoneShardRenderer<T extends DripstoneShard> extends EntityRenderer<T> {

    private static final ResourceLocation DRIPSTONE_SHARD_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/projectiles/dripstone_shard.png");
    public DripstoneShardRenderer(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    public void render(T dripstoneShard, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, dripstoneShard.yRotO, dripstoneShard.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, dripstoneShard.xRotO, dripstoneShard.getXRot())));
        float scale = 1/16f;

        poseStack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-4.0D, 0.0D, 0.0D);
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityCutout(this.getTextureLocation(dripstoneShard)));

        RenderUtil.xShapeProjectileVertices(poseStack, vertexconsumer, 11, 8, 0, 0, 11f/32f, 8f/32f, packedLight);

        poseStack.popPose();
        super.render(dripstoneShard, yRot, partialTicks, poseStack, multiBufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DripstoneShard p_114482_) {
        return DRIPSTONE_SHARD_LOCATION;
    }
}