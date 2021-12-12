package com.bedmen.odyssey.client.renderer.entity.layer;

import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseyPlayerItemInHandLayer<T extends Player, M extends EntityModel<T> & ArmedModel & HeadedModel> extends ItemInHandLayer<T, M> {
    private static final float X_ROT_MIN = (-(float)Math.PI / 6F);
    private static final float X_ROT_MAX = ((float)Math.PI / 2F);

    public OdysseyPlayerItemInHandLayer(RenderLayerParent<T, M> p_174516_) {
        super(p_174516_);
    }

    protected void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, ItemTransforms.TransformType transformType, HumanoidArm humanoidArm, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_174531_) {
        if (itemStack.is(Items.SPYGLASS) && livingEntity.getUseItem() == itemStack && livingEntity.swingTime == 0) {
            this.renderArmWithSpyglass(livingEntity, itemStack, humanoidArm, poseStack, multiBufferSource, p_174531_);
        } else if(!(itemStack.getItem() instanceof QuiverItem)) {
            super.renderArmWithItem(livingEntity, itemStack, transformType, humanoidArm, poseStack, multiBufferSource, p_174531_);
        }

    }

    private void renderArmWithSpyglass(LivingEntity p_174518_, ItemStack p_174519_, HumanoidArm p_174520_, PoseStack p_174521_, MultiBufferSource p_174522_, int p_174523_) {
        p_174521_.pushPose();
        ModelPart modelpart = this.getParentModel().getHead();
        float f = modelpart.xRot;
        modelpart.xRot = Mth.clamp(modelpart.xRot, (-(float)Math.PI / 6F), ((float)Math.PI / 2F));
        modelpart.translateAndRotate(p_174521_);
        modelpart.xRot = f;
        CustomHeadLayer.translateToHead(p_174521_, false);
        boolean flag = p_174520_ == HumanoidArm.LEFT;
        p_174521_.translate((double)((flag ? -2.5F : 2.5F) / 16.0F), -0.0625D, 0.0D);
        Minecraft.getInstance().getItemInHandRenderer().renderItem(p_174518_, p_174519_, ItemTransforms.TransformType.HEAD, false, p_174521_, p_174522_, p_174523_);
        p_174521_.popPose();
    }
}