package com.bedmen.odyssey.client.renderer;

import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OdysseyItemInHandRenderer extends ItemInHandRenderer {

    public OdysseyItemInHandRenderer(Minecraft minecraft, EntityRenderDispatcher entityRenderDispatcher, ItemRenderer itemRenderer) {
        super(minecraft, entityRenderDispatcher, itemRenderer);
    }

    public void tick() {
        this.oMainHandHeight = this.mainHandHeight;
        this.oOffHandHeight = this.offHandHeight;
        LocalPlayer localplayer = this.minecraft.player;
        ItemStack itemstack = localplayer.getMainHandItem();
        ItemStack itemstack1 = localplayer.getOffhandItem();
        if (ItemStack.matches(this.mainHandItem, itemstack)) {
            this.mainHandItem = itemstack;
        }

        if (ItemStack.matches(this.offHandItem, itemstack1)) {
            this.offHandItem = itemstack1;
        }

        if (localplayer.isHandsBusy()) {
            this.mainHandHeight = Mth.clamp(this.mainHandHeight - 0.4F, 0.0F, 1.0F);
            this.offHandHeight = Mth.clamp(this.offHandHeight - 0.4F, 0.0F, 1.0F);
        } else {
            //Makes it so that when dual wielding hatchets the main hand doesn't move when offhand is swung
            float f = WeaponUtil.isDualWielding(localplayer) ? 1.0f : localplayer.getAttackStrengthScale(1.0F);
            boolean requipM = net.minecraftforge.client.ForgeHooksClient.shouldCauseReequipAnimation(this.mainHandItem, itemstack, localplayer.getInventory().selected);
            boolean requipO = net.minecraftforge.client.ForgeHooksClient.shouldCauseReequipAnimation(this.offHandItem, itemstack1, -1);

            if (!requipM && this.mainHandItem != itemstack)
                this.mainHandItem = itemstack;
            if (!requipO && this.offHandItem != itemstack1)
                this.offHandItem = itemstack1;

            this.mainHandHeight += Mth.clamp((!requipM ? f * f * f : 0.0F) - this.mainHandHeight, -0.4F, 0.4F);
            this.offHandHeight += Mth.clamp((float)(!requipO ? 1 : 0) - this.offHandHeight, -0.4F, 0.4F);
        }

        if (this.mainHandHeight < 0.1F) {
            this.mainHandItem = itemstack;
        }

        if (this.offHandHeight < 0.1F) {
            this.offHandItem = itemstack1;
        }

    }

    public void renderArmWithItem(AbstractClientPlayer abstractClientPlayer, float partialTick, float interpPitch, InteractionHand interactionHand, float p_109376_, ItemStack itemStack, float swingProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (!abstractClientPlayer.isScoping()) {
            boolean flag = interactionHand == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidarm = flag ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();
            poseStack.pushPose();
            if (itemStack.isEmpty()) {
                if (flag && !abstractClientPlayer.isInvisible()) {
                    this.renderPlayerArm(poseStack, multiBufferSource, packedLight, swingProgress, p_109376_, humanoidarm);
                }
            } else if (itemStack.is(Items.FILLED_MAP) || itemStack.is(ItemRegistry.FILLED_MAP.get())) {
                if (flag && this.offHandItem.isEmpty()) {
                    this.renderTwoHandedMap(poseStack, multiBufferSource, packedLight, interpPitch, swingProgress, p_109376_);
                } else {
                    this.renderOneHandedMap(poseStack, multiBufferSource, packedLight, swingProgress, humanoidarm, p_109376_, itemStack);
                }
            } else if (itemStack.is(OdysseyItemTags.CROSSBOWS)) {
                boolean flag1 = CrossbowItem.isCharged(itemStack);
                boolean flag2 = humanoidarm == HumanoidArm.RIGHT;
                int i = flag2 ? 1 : -1;
                if (abstractClientPlayer.isUsingItem() && abstractClientPlayer.getUseItemRemainingTicks() > 0 && abstractClientPlayer.getUsedItemHand() == interactionHand) {
                    this.applyItemArmTransform(poseStack, humanoidarm, swingProgress);
                    poseStack.translate((double)((float)i * -0.4785682F), (double)-0.094387F, (double)0.05731531F);
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(-11.935F));
                    poseStack.mulPose(Vector3f.YP.rotationDegrees((float)i * 65.3F));
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees((float)i * -9.785F));
                    float f9 = (float)itemStack.getUseDuration() - ((float)this.minecraft.player.getUseItemRemainingTicks() - partialTick + 1.0F);
                    float f13 = f9 / (float)CrossbowItem.getChargeDuration(itemStack);
                    if (f13 > 1.0F) {
                        f13 = 1.0F;
                    }

                    if (f13 > 0.1F) {
                        float f16 = Mth.sin((f9 - 0.1F) * 1.3F);
                        float f3 = f13 - 0.1F;
                        float f4 = f16 * f3;
                        poseStack.translate((double)(f4 * 0.0F), (double)(f4 * 0.004F), (double)(f4 * 0.0F));
                    }

                    poseStack.translate((double)(f13 * 0.0F), (double)(f13 * 0.0F), (double)(f13 * 0.04F));
                    poseStack.scale(1.0F, 1.0F, 1.0F + f13 * 0.2F);
                    poseStack.mulPose(Vector3f.YN.rotationDegrees((float)i * 45.0F));
                } else {
                    float f = -0.4F * Mth.sin(Mth.sqrt(p_109376_) * (float)Math.PI);
                    float f1 = 0.2F * Mth.sin(Mth.sqrt(p_109376_) * ((float)Math.PI * 2F));
                    float f2 = -0.2F * Mth.sin(p_109376_ * (float)Math.PI);
                    poseStack.translate((double)((float)i * f), (double)f1, (double)f2);
                    this.applyItemArmTransform(poseStack, humanoidarm, swingProgress);
                    this.applyItemArmAttackTransform(poseStack, humanoidarm, p_109376_);
                    if (flag1 && p_109376_ < 0.001F && flag) {
                        poseStack.translate((double)((float)i * -0.641864F), 0.0D, 0.0D);
                        poseStack.mulPose(Vector3f.YP.rotationDegrees((float)i * 10.0F));
                    }
                }

                this.renderItem(abstractClientPlayer, itemStack, flag2 ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag2, poseStack, multiBufferSource, packedLight);
            } else {
                boolean flag3 = humanoidarm == HumanoidArm.RIGHT;
                if (abstractClientPlayer.isUsingItem() && abstractClientPlayer.getUseItemRemainingTicks() > 0 && abstractClientPlayer.getUsedItemHand() == interactionHand) {
                    int k = flag3 ? 1 : -1;
                    if (!net.minecraftforge.client.extensions.common.IClientItemExtensions.of(itemStack).applyForgeHandTransform(poseStack, minecraft.player, humanoidarm, itemStack, partialTick, swingProgress, p_109376_)) {
                        switch (itemStack.getUseAnimation()) {
                            case NONE, CUSTOM, BLOCK -> this.applyItemArmTransform(poseStack, humanoidarm, swingProgress);
                            case EAT, DRINK -> {
                                this.applyEatTransform(poseStack, partialTick, humanoidarm, itemStack);
                                this.applyItemArmTransform(poseStack, humanoidarm, swingProgress);
                            }
                            case BOW -> {
                                this.applyItemArmTransform(poseStack, humanoidarm, swingProgress);
                                poseStack.translate((double) ((float) k * -0.2785682F), (double) 0.18344387F, (double) 0.15731531F);
                                poseStack.mulPose(Vector3f.XP.rotationDegrees(-13.935F));
                                poseStack.mulPose(Vector3f.YP.rotationDegrees((float) k * 35.3F));
                                poseStack.mulPose(Vector3f.ZP.rotationDegrees((float) k * -9.785F));
                                float f8 = (float) itemStack.getUseDuration() - ((float) this.minecraft.player.getUseItemRemainingTicks() - partialTick + 1.0F);
                                float f12 = f8 / 20.0F;
                                f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                                if (f12 > 1.0F) {
                                    f12 = 1.0F;
                                }
                                if (f12 > 0.1F) {
                                    float f15 = Mth.sin((f8 - 0.1F) * 1.3F);
                                    float f18 = f12 - 0.1F;
                                    float f20 = f15 * f18;
                                    poseStack.translate((double) (f20 * 0.0F), (double) (f20 * 0.004F), (double) (f20 * 0.0F));
                                }
                                poseStack.translate((double) (f12 * 0.0F), (double) (f12 * 0.0F), (double) (f12 * 0.04F));
                                poseStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
                                poseStack.mulPose(Vector3f.YN.rotationDegrees((float) k * 45.0F));
                            }
                            case SPEAR -> {
                                this.applyItemArmTransform(poseStack, humanoidarm, swingProgress);
                                poseStack.translate((double) ((float) k * -0.5F), (double) 0.7F, (double) 0.1F);
                                poseStack.mulPose(Vector3f.XP.rotationDegrees(-55.0F));
                                poseStack.mulPose(Vector3f.YP.rotationDegrees((float) k * 35.3F));
                                poseStack.mulPose(Vector3f.ZP.rotationDegrees((float) k * -9.785F));
                                float f7 = (float) itemStack.getUseDuration() - ((float) this.minecraft.player.getUseItemRemainingTicks() - partialTick + 1.0F);
                                float f11 = f7 / 10.0F;
                                if (f11 > 1.0F) {
                                    f11 = 1.0F;
                                }
                                if (f11 > 0.1F) {
                                    float f14 = Mth.sin((f7 - 0.1F) * 1.3F);
                                    float f17 = f11 - 0.1F;
                                    float f19 = f14 * f17;
                                    poseStack.translate((double) (f19 * 0.0F), (double) (f19 * 0.004F), (double) (f19 * 0.0F));
                                }
                                poseStack.translate(0.0D, 0.0D, (double) (f11 * 0.2F));
                                poseStack.scale(1.0F, 1.0F, 1.0F + f11 * 0.2F);
                                poseStack.mulPose(Vector3f.YN.rotationDegrees((float) k * 45.0F));
                            }
                        }
                    }
                } else if (abstractClientPlayer.isAutoSpinAttack()) {
                    this.applyItemArmTransform(poseStack, humanoidarm, swingProgress);
                    int j = flag3 ? 1 : -1;
                    poseStack.translate((double)((float)j * -0.4F), (double)0.8F, (double)0.3F);
                    poseStack.mulPose(Vector3f.YP.rotationDegrees((float)j * 65.0F));
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees((float)j * -85.0F));
                } else {
                    float f5 = -0.4F * Mth.sin(Mth.sqrt(p_109376_) * (float)Math.PI);
                    float f6 = 0.2F * Mth.sin(Mth.sqrt(p_109376_) * ((float)Math.PI * 2F));
                    float f10 = -0.2F * Mth.sin(p_109376_ * (float)Math.PI);
                    int l = flag3 ? 1 : -1;
                    poseStack.translate((double)((float)l * f5), (double)f6, (double)f10);
                    this.applyItemArmTransform(poseStack, humanoidarm, swingProgress);
                    this.applyItemArmAttackTransform(poseStack, humanoidarm, p_109376_);
                }

                this.renderItem(abstractClientPlayer, itemStack, flag3 ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag3, poseStack, multiBufferSource, packedLight);
            }

            poseStack.popPose();
        }
    }

    public void renderHandsWithItems(float p_109315_, PoseStack p_109316_, MultiBufferSource.BufferSource p_109317_, LocalPlayer p_109318_, int p_109319_) {
        float f = p_109318_.getAttackAnim(p_109315_);
        InteractionHand interactionhand = MoreObjects.firstNonNull(p_109318_.swingingArm, InteractionHand.MAIN_HAND);
        float f1 = Mth.lerp(p_109315_, p_109318_.xRotO, p_109318_.getXRot());
        ItemInHandRenderer.HandRenderSelection iteminhandrenderer$handrenderselection = evaluateWhichHandsToRender(p_109318_);
        float f2 = Mth.lerp(p_109315_, p_109318_.xBobO, p_109318_.xBob);
        float f3 = Mth.lerp(p_109315_, p_109318_.yBobO, p_109318_.yBob);
        p_109316_.mulPose(Vector3f.XP.rotationDegrees((p_109318_.getViewXRot(p_109315_) - f2) * 0.1F));
        p_109316_.mulPose(Vector3f.YP.rotationDegrees((p_109318_.getViewYRot(p_109315_) - f3) * 0.1F));
        if (iteminhandrenderer$handrenderselection.renderMainHand) {
            float f4 = interactionhand == InteractionHand.MAIN_HAND ? f : 0.0F;
            float f5 = 1.0F - Mth.lerp(p_109315_, this.oMainHandHeight, this.mainHandHeight);
            if(!net.minecraftforge.client.ForgeHooksClient.renderSpecificFirstPersonHand(InteractionHand.MAIN_HAND, p_109316_, p_109317_, p_109319_, p_109315_, f1, f4, f5, this.mainHandItem))
                this.renderArmWithItem(p_109318_, p_109315_, f1, InteractionHand.MAIN_HAND, f4, this.mainHandItem, f5, p_109316_, p_109317_, p_109319_);
        }

        if (iteminhandrenderer$handrenderselection.renderOffHand) {
            float f6 = interactionhand == InteractionHand.OFF_HAND ? f : 0.0F;
            float f7 = 1.0F - Mth.lerp(p_109315_, this.oOffHandHeight, this.offHandHeight);
            if(!net.minecraftforge.client.ForgeHooksClient.renderSpecificFirstPersonHand(InteractionHand.OFF_HAND, p_109316_, p_109317_, p_109319_, p_109315_, f1, f6, f7, this.offHandItem))
                this.renderArmWithItem(p_109318_, p_109315_, f1, InteractionHand.OFF_HAND, f6, this.offHandItem, f7, p_109316_, p_109317_, p_109319_);
        }

        p_109317_.endBatch();
    }

    static ItemInHandRenderer.HandRenderSelection evaluateWhichHandsToRender(LocalPlayer localPlayer) {
        ItemStack itemstack = localPlayer.getMainHandItem();
        ItemStack itemstack1 = localPlayer.getOffhandItem();
        boolean flag = itemstack.is(OdysseyItemTags.BOWS) || itemstack1.is(OdysseyItemTags.BOWS);
        boolean flag1 = itemstack.is(OdysseyItemTags.CROSSBOWS) || itemstack1.is(OdysseyItemTags.CROSSBOWS);
        if (!flag && !flag1) {
            return ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        } else if (localPlayer.isUsingItem()) {
            return selectionUsingItemWhileHoldingBowLike(localPlayer);
        } else {
            return isChargedCrossbow(itemstack) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        }
    }

    private static ItemInHandRenderer.HandRenderSelection selectionUsingItemWhileHoldingBowLike(LocalPlayer localPlayer) {
        ItemStack itemstack = localPlayer.getUseItem();
        InteractionHand interactionhand = localPlayer.getUsedItemHand();
        if (!itemstack.is(OdysseyItemTags.BOWS) && !itemstack.is(OdysseyItemTags.CROSSBOWS)) {
            return interactionhand == InteractionHand.MAIN_HAND && isChargedCrossbow(localPlayer.getOffhandItem()) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        } else {
            return ItemInHandRenderer.HandRenderSelection.onlyForHand(interactionhand);
        }
    }

    private static boolean isChargedCrossbow(ItemStack itemStack) {
        return itemStack.is(OdysseyItemTags.CROSSBOWS) && CrossbowItem.isCharged(itemStack);
    }
}
