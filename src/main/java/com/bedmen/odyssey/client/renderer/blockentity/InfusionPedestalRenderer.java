package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.entity.InfusionPedestalBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfusionPedestalRenderer implements BlockEntityRenderer<InfusionPedestalBlockEntity> {

    public InfusionPedestalRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(InfusionPedestalBlockEntity infusionPedestalBlockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        int i = (int)infusionPedestalBlockEntity.getBlockPos().asLong();
        ItemStack itemStack = infusionPedestalBlockEntity.itemStack;
        if (itemStack != ItemStack.EMPTY) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 15.0d / 16.0D, 0.5D);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, i);
            poseStack.popPose();
        }
    }
}