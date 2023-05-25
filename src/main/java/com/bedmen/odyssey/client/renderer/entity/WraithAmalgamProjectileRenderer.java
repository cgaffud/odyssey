package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.WraithAmalgamProjectileModel;
import com.bedmen.odyssey.entity.projectile.WraithAmalgamProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WraithAmalgamProjectileRenderer extends EntityRenderer<WraithAmalgamProjectile> {
    private static final ResourceLocation WRAITH_AMALGAM_PROJECTILE_LOCATION = new ResourceLocation(Odyssey.MOD_ID,"textures/entity/wraith/amalgam_projectile.png");
    private final WraithAmalgamProjectileModel<WraithAmalgamProjectile> model;

    public WraithAmalgamProjectileRenderer(EntityRendererProvider.Context p_174368_) {
        super(p_174368_);
        this.model = new WraithAmalgamProjectileModel<>(p_174368_.bakeLayer(WraithAmalgamProjectileModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(WraithAmalgamProjectile p_114482_) {
        return this.WRAITH_AMALGAM_PROJECTILE_LOCATION;
    }

    public void render(WraithAmalgamProjectile wraithAmalgamProjectile, float p_115863_, float partialTicks, PoseStack poseStack, MultiBufferSource p_115866_, int p_115867_) {
        poseStack.pushPose();
//        p_115865_.scale(-0.5F, -0.5F, 0.5F);
        float ageInTicks = (float)wraithAmalgamProjectile.tickCount + partialTicks;
//        Vec3 v = wraithAmalgamProjectile.getDeltaMovement().normalize();


        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, wraithAmalgamProjectile.yRotO, wraithAmalgamProjectile.getYRot()) - 90.0F));
//        poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
//        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, wraithAmalgamProjectile.xRotO, wraithAmalgamProjectile.getXRot()) + 90.0F));
//        poseStack.translate(D, -0D, 0D);


        this.model.setupAnim(wraithAmalgamProjectile, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = p_115866_.getBuffer(this.model.renderType(WRAITH_AMALGAM_PROJECTILE_LOCATION));
        poseStack.translate(-0D, -0.D, 0D);
        this.model.renderToBuffer(poseStack, vertexconsumer, p_115867_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.scale(1.5F, 1.5F, 1.5F);
        poseStack.popPose();
        super.render(wraithAmalgamProjectile, p_115863_, partialTicks, poseStack, p_115866_, p_115867_);
    }

}
