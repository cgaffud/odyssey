package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.PermafrostBigIcicleModel;
import com.bedmen.odyssey.client.model.PermafrostSpawnerIcicleModel;
import com.bedmen.odyssey.entity.boss.permafrost.PermafrostBigIcicleEntity;
import com.bedmen.odyssey.entity.boss.permafrost.PermafrostSpawnerIcicle;
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
public class PermafrostSpawnerIcicleRenderer extends EntityRenderer<PermafrostSpawnerIcicle> {
    private static final ResourceLocation PERMAFROST_SPAWNER_ICICLE_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/permafrost/spawner.png");
    private final PermafrostSpawnerIcicleModel<PermafrostSpawnerIcicle> model;

    public PermafrostSpawnerIcicleRenderer(EntityRendererProvider.Context p_174368_) {
        super(p_174368_);
        this.model = new PermafrostSpawnerIcicleModel<>(p_174368_.bakeLayer(PermafrostSpawnerIcicleModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(PermafrostSpawnerIcicle p_114482_) {
        return this.PERMAFROST_SPAWNER_ICICLE_LOCATION;
    }

    public void render(PermafrostSpawnerIcicle permafrostSpawnerIcicle, float p_115863_, float partialTicks, PoseStack poseStack, MultiBufferSource p_115866_, int p_115867_) {
        poseStack.pushPose();
        float ageInTicks = (float)permafrostSpawnerIcicle.tickCount + partialTicks;

        poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));

        this.model.setupAnim(permafrostSpawnerIcicle, 0.0F, 0.0F, ageInTicks, 0.0F, 0.0F);

        poseStack.translate(0F,-1.25f,0F);

//        System.out.printf("YP Recieved as: %g\n", permafrostSpawnerIcicle.getYRot());
        poseStack.mulPose(Vector3f.YP.rotationDegrees(permafrostSpawnerIcicle.getYRot()));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(permafrostSpawnerIcicle.getXRot()));

        poseStack.scale(1.25F,1.25F,1.25F);

        VertexConsumer vertexconsumer = p_115866_.getBuffer(this.model.renderType(PERMAFROST_SPAWNER_ICICLE_LOCATION));
        this.model.renderToBuffer(poseStack, vertexconsumer, p_115867_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(permafrostSpawnerIcicle, p_115863_, partialTicks, poseStack, p_115866_, p_115867_);
    }

}
