package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.PermafrostBigIcicleModel;
import com.bedmen.odyssey.entity.boss.permafrost.PermafrostBigIcicleEntity;
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
public class PermafrostBigIcicleRenderer extends EntityRenderer<PermafrostBigIcicleEntity> {
    private static final ResourceLocation PERMAFROST_BIG_ICICLE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/permafrost/permafrost.png");
    private final PermafrostBigIcicleModel<PermafrostBigIcicleEntity> model;

    public PermafrostBigIcicleRenderer(EntityRendererProvider.Context p_174368_) {
        super(p_174368_);
        this.model = new PermafrostBigIcicleModel<>(p_174368_.bakeLayer(PermafrostBigIcicleModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(PermafrostBigIcicleEntity p_114482_) {
        return this.PERMAFROST_BIG_ICICLE_LOCATION;
    }

    public void render(PermafrostBigIcicleEntity permafrostBigIcicleEntity, float p_115863_, float partialTicks, PoseStack poseStack, MultiBufferSource p_115866_, int p_115867_) {
        poseStack.pushPose();
        float ageInTicks = (float)permafrostBigIcicleEntity.tickCount + partialTicks;

        this.model.setupAnim(permafrostBigIcicleEntity, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);

        poseStack.translate(0F,-2.0f,0F);

//        System.out.printf("YP Recieved as: %g\n", permafrostBigIcicleEntity.getYRot());
        poseStack.mulPose(Vector3f.YP.rotationDegrees(permafrostBigIcicleEntity.getYRot()));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(permafrostBigIcicleEntity.getXRot()));

        poseStack.scale(1.25F,1.25F,1.25F);

        VertexConsumer vertexconsumer = p_115866_.getBuffer(this.model.renderType(PERMAFROST_BIG_ICICLE_LOCATION));
        this.model.renderToBuffer(poseStack, vertexconsumer, p_115867_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(permafrostBigIcicleEntity, p_115863_, partialTicks, poseStack, p_115866_, p_115867_);
    }

}
