package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.client.model.BoomerangModel;
import com.bedmen.odyssey.entity.projectile.Boomerang;
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
public class BoomerangRenderer extends EntityRenderer<Boomerang> {
    private final BoomerangModel boomerangModel;

    public BoomerangRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.boomerangModel = new BoomerangModel(context.bakeLayer(BoomerangModel.LAYER_LOCATION));
    }

    public void render(Boomerang boomerang, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(((float)boomerang.tickCount + partialTicks) * (boomerang.getLoyalty()+1) * 30));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(0));
        VertexConsumer ivertexbuilder = net.minecraft.client.renderer.entity.ItemRenderer.getFoilBufferDirect(bufferIn, this.boomerangModel.renderType(getTextureLocation(boomerang)), false, boomerang.isFoil());
        this.boomerangModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(boomerang, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(Boomerang boomerang) {
        return boomerang.getBoomerangType().getResourceLocation();
    }
}
