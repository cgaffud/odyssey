package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.client.model.WeaverModel;
import com.bedmen.odyssey.client.renderer.entity.PassiveWeaverRenderer;
import com.bedmen.odyssey.entity.animal.PassiveWeaver;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WeaverHeldItemLayer extends RenderLayer<PassiveWeaver, WeaverModel<PassiveWeaver>> {
    private final ItemInHandRenderer itemInHandRenderer;

    public WeaverHeldItemLayer(RenderLayerParent<PassiveWeaver, WeaverModel<PassiveWeaver>> renderLayerParent, ItemInHandRenderer itemInHandRenderer) {
        super(renderLayerParent);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int p_117009_, PassiveWeaver passiveWeaver, float p_117011_, float p_117012_, float p_117013_, float p_117014_, float p_117015_, float p_117016_) {
        poseStack.pushPose();

        poseStack.translate((this.getParentModel()).head.x / 16.0F, (this.getParentModel()).head.y / 16.0F, (this.getParentModel()).head.z / 16.0F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(p_117015_));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(p_117016_));
        poseStack.translate(0F, (double) 0.15F, -0.4D);

        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));

        ItemStack itemstack = passiveWeaver.getMainHandItem();
        this.itemInHandRenderer.renderItem(passiveWeaver, itemstack, ItemTransforms.TransformType.GROUND, false, poseStack, multiBufferSource, p_117009_);
        poseStack.popPose();
    }
}