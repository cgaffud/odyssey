package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.client.model.QuiverModel;
import com.bedmen.odyssey.items.QuiverItem;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class QuiverLayer<T extends Player, M extends PlayerModel<T>> extends RenderLayer<T, M> {
    private final QuiverModel<T> quiverModel;

    public QuiverLayer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.quiverModel = new QuiverModel<>(entityModelSet.bakeLayer(QuiverModel.LAYER_LOCATION));
    }

    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        HumanoidArm handSide = pLivingEntity.getMainArm();
        ItemStack itemstack = pLivingEntity.getOffhandItem();
        Item item = itemstack.getItem();
        if (item instanceof QuiverItem) {
            pMatrixStack.pushPose();

            translateToLeg(handSide, pMatrixStack);
            this.getParentModel().copyPropertiesTo(this.quiverModel);
            this.quiverModel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(pBuffer, RenderType.armorCutoutNoCull(((QuiverItem) item).getQuiverType().getResourceLocation()), false, itemstack.hasFoil());
            this.quiverModel.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pMatrixStack.popPose();
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
