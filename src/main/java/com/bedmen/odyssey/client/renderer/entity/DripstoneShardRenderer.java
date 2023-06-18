package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.DripstoneShardModel;
import com.bedmen.odyssey.client.model.WraithAmalgamProjectileModel;
import com.bedmen.odyssey.entity.projectile.DripstoneShard;
import com.bedmen.odyssey.entity.projectile.WraithAmalgamProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DripstoneShardRenderer extends EntityRenderer<DripstoneShard> {

    private static final ResourceLocation DRIPSTONE_SHARD_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/projectiles/dripstone_shard.png");
    private final DripstoneShardModel<DripstoneShard> model;

    public DripstoneShardRenderer(EntityRendererProvider.Context p_174368_) {
        super(p_174368_);
        this.model = new DripstoneShardModel<>(p_174368_.bakeLayer(DripstoneShardModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(DripstoneShard p_114482_) {
        return this.DRIPSTONE_SHARD_LOCATION;
    }

    public void render(DripstoneShard dripstoneShard, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn) {
        poseStack.pushPose();
        poseStack.translate(0,-1,0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, dripstoneShard.yRotO, dripstoneShard.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, dripstoneShard.xRotO, dripstoneShard.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, model.renderType(this.getTextureLocation(dripstoneShard)), false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(dripstoneShard, entityYaw, partialTicks, poseStack, multiBufferSource, packedLightIn);
    }
}
