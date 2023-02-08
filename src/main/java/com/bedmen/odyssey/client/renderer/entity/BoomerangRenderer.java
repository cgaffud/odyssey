package com.bedmen.odyssey.client.renderer.entity;

import com.bedmen.odyssey.entity.projectile.Boomerang;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BoomerangRenderer extends EntityRenderer<Boomerang> {
    private final ItemRenderer itemRenderer;

    public BoomerangRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(Boomerang boomerang, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        float spinRotation = boomerang.getSpinRotation(partialTicks);
        matrixStackIn.pushPose();
        matrixStackIn.translate(1/4d * Mth.sin((float) (spinRotation*Math.PI/180d)),1d/32d,-1/4d * Mth.cos((float) (spinRotation*Math.PI/180d)));
        matrixStackIn.scale(2f, 2f, 2f);
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90f));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(spinRotation));
        ItemStack itemStack = boomerang.getThrownStack();
        BakedModel bakedmodel = this.itemRenderer.getModel(itemStack, boomerang.level, (LivingEntity)null, boomerang.getId());
        this.itemRenderer.render(itemStack, ItemTransforms.TransformType.GROUND, false, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, bakedmodel);
        matrixStackIn.popPose();
    }

    /**
     * Should never be called because we render the boomerang using the item texture.
     * Has to be here because EntityRenderer requires it be defined.
     */
    public ResourceLocation getTextureLocation(Boomerang boomerang) {
        return null;
    }
}
