package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.client.model.QuiverModel;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.items.aspect_items.QuiverItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class QuiverLayer<T extends Player, M extends PlayerModel<T>> extends RenderLayer<T, M> {
    private final QuiverModel<T> quiverModel;

    public QuiverLayer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.quiverModel = new QuiverModel<>(entityModelSet.bakeLayer(QuiverModel.LAYER_LOCATION));
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        HumanoidArm handSide = livingEntity.getMainArm();
        Optional<ItemStack> optionalQuiver = WeaponUtil.getQuiver(livingEntity);
        if (optionalQuiver.isPresent()) {
            ItemStack quiver = optionalQuiver.get();
            QuiverItem quiverItem = (QuiverItem)quiver.getItem();
            poseStack.pushPose();

            translateToLeg(handSide, poseStack);
            this.getParentModel().copyPropertiesTo(this.quiverModel);
            this.quiverModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(quiverItem.quiverType.textureResourceLocation), false, quiver.hasFoil());
            this.quiverModel.renderToBuffer(poseStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }

    public void translateToLeg(HumanoidArm pSide, PoseStack pMatrixStack) {
        PlayerModel<T> playerModel = this.getParentModel();
        boolean side = pSide == HumanoidArm.RIGHT;
        ModelPart modelrenderer = side ? playerModel.leftLeg : playerModel.rightLeg;
        if (playerModel.slim) {
            float f = side ? -1.0f : 1.0f;
            modelrenderer.x += f;
            modelrenderer.translateAndRotate(pMatrixStack);
            modelrenderer.x -= f;
        } else {
            modelrenderer.translateAndRotate(pMatrixStack);
        }
        pMatrixStack.translate(side ? 3.5/16.0 : -3.5/16.0,-20.0/16.0,0);
    }
}
