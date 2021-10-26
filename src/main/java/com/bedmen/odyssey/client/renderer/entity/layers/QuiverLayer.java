package com.bedmen.odyssey.client.renderer.entity.layers;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.client.renderer.entity.model.QuiverModel;
import com.bedmen.odyssey.items.QuiverItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuiverLayer<T extends PlayerEntity, M extends PlayerModel<T>> extends LayerRenderer<T, M> {
    private static final ResourceLocation QUIVER_LOCATION = new ResourceLocation(Odyssey.MOD_ID, "textures/entity/quiver.png");
    private final QuiverModel<T> quiverModel = new QuiverModel<>();

    public QuiverLayer(IEntityRenderer<T, M> p_i50942_1_) {
        super(p_i50942_1_);
    }

    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        HandSide handSide = pLivingEntity.getMainArm();
        ItemStack itemstack = pLivingEntity.getOffhandItem();
        if (shouldRender(itemstack)) {
            pMatrixStack.pushPose();

            translateToLeg(handSide, pMatrixStack);
            this.getParentModel().copyPropertiesTo(this.quiverModel);
            this.quiverModel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            IVertexBuilder ivertexbuilder = ItemRenderer.getArmorFoilBuffer(pBuffer, RenderType.armorCutoutNoCull(QUIVER_LOCATION), false, itemstack.hasFoil());
            this.quiverModel.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pMatrixStack.popPose();
        }
    }

    public void translateToLeg(HandSide pSide, MatrixStack pMatrixStack) {
        PlayerModel<T> playerModel = this.getParentModel();
        boolean side = pSide == HandSide.RIGHT;
        ModelRenderer modelrenderer = side ? playerModel.leftLeg : playerModel.rightLeg;
        if (playerModel.slim) {
            float f = side ? -1.0f : 1.0f;
            modelrenderer.x += f;
            modelrenderer.translateAndRotate(pMatrixStack);
            modelrenderer.x -= f;
        } else {
            modelrenderer.translateAndRotate(pMatrixStack);
        }
        pMatrixStack.translate(side ? 3.0/16.0 : -3.0/16.0,-20.0/16.0,0);
    }

    public boolean shouldRender(ItemStack stack) {
        return stack.getItem() instanceof QuiverItem;
    }
}
