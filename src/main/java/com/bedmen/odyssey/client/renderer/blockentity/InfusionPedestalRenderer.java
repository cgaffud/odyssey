package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.entity.AbstractInfusionPedestalBlockEntity;
import com.bedmen.odyssey.block.entity.InfusionPedestalBlockEntity;
import com.google.common.hash.Hashing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class InfusionPedestalRenderer implements BlockEntityRenderer<AbstractInfusionPedestalBlockEntity> {

    public InfusionPedestalRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(AbstractInfusionPedestalBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        int positionNumber =  (int)blockEntity.getBlockPos().asLong();
        ItemStack itemStack = blockEntity.getItemStackOriginal();
        Direction direction = blockEntity.itemRenderDirection;
        if (itemStack != ItemStack.EMPTY) {
            Random psuedoRandom = new Random(positionNumber);
            float scale = blockEntity instanceof InfusionPedestalBlockEntity infusionPedestalBlockEntity ? infusionPedestalBlockEntity.getItemRenderScale() : 1.0f;
            for(int i = 0; i < itemStack.getCount(); i++){
                Vec3 offset = i == 0 ? Vec3.ZERO : new Vec3(0.5d * psuedoRandom.nextDouble() - 0.25d, (double)i / 256.0d, 0.5d * psuedoRandom.nextDouble() - 0.25d);
                this.renderItemWithOffset(itemStack, direction, offset, scale, poseStack, multiBufferSource, packedLight, packedOverlay, positionNumber);
            }
        }
    }

    private void renderItemWithOffset(ItemStack itemStack, Direction direction, Vec3 offset, float scale, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, int positionNumber){
        poseStack.pushPose();
        poseStack.translate(0.5D, 15.0d / 16.0D, 0.5D);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(offset.x, offset.y, offset.z);
        float yAngle = (-direction.get2DDataValue() % 4) * 90.0f;
        poseStack.mulPose(Vector3f.YP.rotationDegrees(yAngle));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, positionNumber);
        poseStack.popPose();
    }

}