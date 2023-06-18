package com.bedmen.odyssey.client.renderer.blockentity;

import com.bedmen.odyssey.block.entity.AbstractInfusionPedestalBlockEntity;
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

import java.util.Random;

public abstract class AbstractInfusionPedestalRenderer<T extends AbstractInfusionPedestalBlockEntity> implements BlockEntityRenderer<T> {

    public AbstractInfusionPedestalRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        int positionNumber =  (int)blockEntity.getBlockPos().asLong();
        ItemStack itemStack = blockEntity.getItemStackOriginal();
        Direction direction = blockEntity.itemRenderDirection;
        if (itemStack != ItemStack.EMPTY) {
            Random psuedoRandom = new Random(positionNumber);
            float itemScale = this.getItemScale(blockEntity);
            for(int i = 0; i < itemStack.getCount(); i++){
                float activeItemScale =  i >= itemStack.getCount() - this.getCountToShrink(blockEntity) ? itemScale : 1.0f;
                Vec3 offset = i == 0 ? Vec3.ZERO : new Vec3(0.5d * psuedoRandom.nextDouble() - 0.25d, (double)i / 256.0d, 0.5d * psuedoRandom.nextDouble() - 0.25d);
                this.renderItemWithOffset(itemStack, direction, offset, activeItemScale, poseStack, multiBufferSource, packedLight, packedOverlay, positionNumber);
            }
        }
    }

    protected abstract float getItemScale(T blockEntity);

    protected abstract int getCountToShrink(T blockEntity);

    protected void renderItemWithOffset(ItemStack itemStack, Direction direction, Vec3 offset, float itemScale, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, int positionNumber){
        poseStack.pushPose();
        poseStack.translate(0.5D, 15.0d / 16.0D, 0.5D);
        poseStack.scale(itemScale, itemScale, itemScale);
        poseStack.translate(offset.x, offset.y, offset.z);
        float yAngle = (-direction.get2DDataValue() % 4) * 90.0f;
        poseStack.mulPose(Vector3f.YP.rotationDegrees(yAngle));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, positionNumber);
        poseStack.popPose();
    }

}
