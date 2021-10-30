package com.bedmen.odyssey.client.renderer.entity.layers;

import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AmuletLayer<T extends PlayerEntity, M extends PlayerModel<T>> extends LayerRenderer<T, M> {

    public AmuletLayer(IEntityRenderer<T, M> p_i50942_1_) {
        super(p_i50942_1_);
    }

    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T playerEntity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if(playerEntity instanceof IOdysseyPlayer){
            ItemStack itemstack = ((IOdysseyPlayer)playerEntity).getTrinketSlot();
            this.renderChestWithItem(playerEntity, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, p_225628_1_, p_225628_2_, p_225628_3_);
        }
    }

    private void renderChestWithItem(PlayerEntity playerEntity, ItemStack itemStack, ItemCameraTransforms.TransformType p_229135_3_, MatrixStack matrixStack, IRenderTypeBuffer p_229135_6_, int p_229135_7_) {
        if (!itemStack.isEmpty()) {
            matrixStack.pushPose();
            PlayerModel<T> playerModel = this.getParentModel();
            ModelRenderer modelrenderer = playerModel.body;
            modelrenderer.translateAndRotate(matrixStack);
            matrixStack.scale(0.75f,0.75f,0.75f);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
            double d0 = 2d/16d;
            if(!playerEntity.getItemBySlot(EquipmentSlotType.CHEST).isEmpty()){
                d0 += 1d/16d;
            }
            matrixStack.translate(0d, -6d/16d, d0);
            Minecraft.getInstance().getItemInHandRenderer().renderItem(playerEntity, itemStack, p_229135_3_, false, matrixStack, p_229135_6_, p_229135_7_);
            matrixStack.popPose();
        }
    }
}
