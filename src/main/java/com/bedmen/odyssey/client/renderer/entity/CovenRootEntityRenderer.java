package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.CovenRootModel;
import com.bedmen.odyssey.entity.boss.coven.CovenRootEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CovenRootEntityRenderer extends EntityRenderer<CovenRootEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/coven/coven_root.png");
    private final CovenRootModel<CovenRootEntity> model;

    public CovenRootEntityRenderer(EntityRendererProvider.Context p_174100_) {
        super(p_174100_);
        this.model = new CovenRootModel<>(p_174100_.bakeLayer(CovenRootModel.LAYER_LOCATION));
    }

    public void render(CovenRootEntity p_114528_, float p_114529_, float p_114530_, PoseStack p_114531_, MultiBufferSource p_114532_, int p_114533_) {
        float f = p_114528_.getAnimationProgress(p_114530_);
        if (f != 0.0F) {
            float f1 = 2.0F;
            if (f > 0.9F) {
                f1 *= (1.0F - f) / 0.1F;
            }

            p_114531_.pushPose();
            p_114531_.mulPose(Vector3f.YP.rotationDegrees(90.0F - p_114528_.getYRot()));
            p_114531_.scale(-f1, -f1, f1);
            float f2 = 0.03125F;
            p_114531_.translate(0.0D, -0.276D, 0.0D);
            p_114531_.scale(0.5F, 0.5F, 0.5F);
            this.model.setupAnim(p_114528_, f, 0.0F, 0.0F, p_114528_.getYRot(), p_114528_.getXRot());
            VertexConsumer vertexconsumer = p_114532_.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(p_114531_, vertexconsumer, p_114533_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            p_114531_.popPose();
            super.render(p_114528_, p_114529_, p_114530_, p_114531_, p_114532_, p_114533_);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CovenRootEntity p_114482_) {
        return TEXTURE_LOCATION;
    }
}