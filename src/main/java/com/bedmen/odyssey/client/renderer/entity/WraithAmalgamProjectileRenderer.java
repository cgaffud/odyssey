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
        float ageInTicks = (float)wraithAmalgamProjectile.tickCount + partialTicks;

        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

        this.model.setupAnim(wraithAmalgamProjectile, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = p_115866_.getBuffer(this.model.renderType(WRAITH_AMALGAM_PROJECTILE_LOCATION));
        poseStack.translate(-0.025D, -1.25D, 0D);

        poseStack.mulPose(Vector3f.YP.rotationDegrees(wraithAmalgamProjectile.getYRot()));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(wraithAmalgamProjectile.getXRot()));
        poseStack.scale(0.75F,0.75F,0.75F);
        this.model.renderToBuffer(poseStack, vertexconsumer, p_115867_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//        poseStack.scale(1.5F, 1.5F, 1.5F);
        poseStack.popPose();
        super.render(wraithAmalgamProjectile, p_115863_, partialTicks, poseStack, p_115866_, p_115867_);
    }

}
