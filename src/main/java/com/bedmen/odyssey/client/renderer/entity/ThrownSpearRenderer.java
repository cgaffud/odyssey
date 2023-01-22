package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.model.SpearModel;
import com.bedmen.odyssey.entity.projectile.ThrownSpear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ThrownSpearRenderer extends EntityRenderer<ThrownSpear> {
    private final SpearModel model;

    public ThrownSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SpearModel(context.bakeLayer(SpearModel.LAYER_LOCATION));
    }

    public void render(ThrownSpear thrownSpear, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, thrownSpear.yRotO, thrownSpear.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, thrownSpear.xRotO, thrownSpear.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, this.model.renderType(this.getTextureLocation(thrownSpear)), false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(thrownSpear, entityYaw, partialTicks, poseStack, multiBufferSource, packedLightIn);
    }

    public ResourceLocation getTextureLocation(ThrownSpear thrownSpear) {
        return thrownSpear.getSpearType().entityTexture;
    }
}
