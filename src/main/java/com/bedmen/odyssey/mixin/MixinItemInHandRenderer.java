package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.equipment.DualWieldItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinItemInHandRenderer {

    @Shadow
    private void renderPlayerArm(PoseStack p_109347_, MultiBufferSource p_109348_, int p_109349_, float p_109350_, float p_109351_, HumanoidArm p_109352_) {}
    @Shadow
    private ItemStack offHandItem;
    @Shadow
    private void renderTwoHandedMap(PoseStack p_109340_, MultiBufferSource p_109341_, int p_109342_, float p_109343_, float p_109344_, float p_109345_) {}
    @Shadow
    private void renderOneHandedMap(PoseStack p_109354_, MultiBufferSource p_109355_, int p_109356_, float p_109357_, HumanoidArm p_109358_, float p_109359_, ItemStack p_109360_) {}
    @Shadow
    private void applyItemArmTransform(PoseStack p_109383_, HumanoidArm p_109384_, float p_109385_) {}
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    private void applyEatTransform(PoseStack p_109331_, float p_109332_, HumanoidArm p_109333_, ItemStack p_109334_) {}
    @Shadow
    private void applyItemArmAttackTransform(PoseStack p_109336_, HumanoidArm p_109337_, float p_109338_) {}
    @Shadow
    public void renderItem(LivingEntity p_109323_, ItemStack p_109324_, ItemTransforms.TransformType p_109325_, boolean p_109326_, PoseStack p_109327_, MultiBufferSource p_109328_, int p_109329_) {}
    @Shadow
    private float mainHandHeight;
    @Shadow
    private float oMainHandHeight;
    @Shadow
    private float offHandHeight;
    @Shadow
    private float oOffHandHeight;
    @Shadow
    private ItemStack mainHandItem;

    private static boolean isChargedCrossbow(ItemStack itemStack) {
        return itemStack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemStack);
    }

    private static ItemInHandRenderer.HandRenderSelection selectionUsingItemWhileHoldingBowLike(LocalPlayer p_172917_) {
        ItemStack itemstack = p_172917_.getUseItem();
        InteractionHand interactionhand = p_172917_.getUsedItemHand();
        if (!(itemstack.getItem() instanceof BowItem) && !(itemstack.getItem() instanceof CrossbowItem)) {
            return interactionhand == InteractionHand.MAIN_HAND && isChargedCrossbow(p_172917_.getOffhandItem()) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        } else {
            return ItemInHandRenderer.HandRenderSelection.onlyForHand(interactionhand);
        }
    }

    /**
     * @author JemBren
     */
    @Overwrite
    static ItemInHandRenderer.HandRenderSelection evaluateWhichHandsToRender(LocalPlayer p_172915_) {
        ItemStack itemstack = p_172915_.getMainHandItem();
        ItemStack itemstack1 = p_172915_.getOffhandItem();
        boolean flag = itemstack.getItem() instanceof BowItem || itemstack1.getItem() instanceof BowItem;
        boolean flag1 = itemstack.getItem() instanceof CrossbowItem || itemstack1.getItem() instanceof CrossbowItem;
        if (!flag && !flag1) {
            return ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        } else if (p_172915_.isUsingItem()) {
            return selectionUsingItemWhileHoldingBowLike(p_172915_);
        } else {
            return isChargedCrossbow(itemstack) ? ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_BOTH_HANDS;
        }
    }

    private void renderArmWithItem(AbstractClientPlayer p_109372_, float p_109373_, float p_109374_, InteractionHand p_109375_, float p_109376_, ItemStack p_109377_, float p_109378_, PoseStack p_109379_, MultiBufferSource p_109380_, int p_109381_) {
        if (!p_109372_.isScoping()) {
            boolean flag = p_109375_ == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidarm = flag ? p_109372_.getMainArm() : p_109372_.getMainArm().getOpposite();
            p_109379_.pushPose();
            if (p_109377_.isEmpty()) {
                if (flag && !p_109372_.isInvisible()) {
                    this.renderPlayerArm(p_109379_, p_109380_, p_109381_, p_109378_, p_109376_, humanoidarm);
                }
            } else if (p_109377_.is(Items.FILLED_MAP)) {
                if (flag && this.offHandItem.isEmpty()) {
                    this.renderTwoHandedMap(p_109379_, p_109380_, p_109381_, p_109374_, p_109378_, p_109376_);
                } else {
                    this.renderOneHandedMap(p_109379_, p_109380_, p_109381_, p_109378_, humanoidarm, p_109376_, p_109377_);
                }
            } else if (p_109377_.getItem() instanceof CrossbowItem) {
                boolean flag1 = CrossbowItem.isCharged(p_109377_);
                boolean flag2 = humanoidarm == HumanoidArm.RIGHT;
                int i = flag2 ? 1 : -1;
                if (p_109372_.isUsingItem() && p_109372_.getUseItemRemainingTicks() > 0 && p_109372_.getUsedItemHand() == p_109375_) {
                    this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                    p_109379_.translate((double)((float)i * -0.4785682F), (double)-0.094387F, (double)0.05731531F);
                    p_109379_.mulPose(Vector3f.XP.rotationDegrees(-11.935F));
                    p_109379_.mulPose(Vector3f.YP.rotationDegrees((float)i * 65.3F));
                    p_109379_.mulPose(Vector3f.ZP.rotationDegrees((float)i * -9.785F));
                    float f9 = (float)p_109377_.getUseDuration() - ((float)this.minecraft.player.getUseItemRemainingTicks() - p_109373_ + 1.0F);
                    float f13 = f9 / (float)CrossbowItem.getChargeDuration(p_109377_);
                    if (f13 > 1.0F) {
                        f13 = 1.0F;
                    }

                    if (f13 > 0.1F) {
                        float f16 = Mth.sin((f9 - 0.1F) * 1.3F);
                        float f3 = f13 - 0.1F;
                        float f4 = f16 * f3;
                        p_109379_.translate((double)(f4 * 0.0F), (double)(f4 * 0.004F), (double)(f4 * 0.0F));
                    }

                    p_109379_.translate((double)(f13 * 0.0F), (double)(f13 * 0.0F), (double)(f13 * 0.04F));
                    p_109379_.scale(1.0F, 1.0F, 1.0F + f13 * 0.2F);
                    p_109379_.mulPose(Vector3f.YN.rotationDegrees((float)i * 45.0F));
                } else {
                    float f = -0.4F * Mth.sin(Mth.sqrt(p_109376_) * (float)Math.PI);
                    float f1 = 0.2F * Mth.sin(Mth.sqrt(p_109376_) * ((float)Math.PI * 2F));
                    float f2 = -0.2F * Mth.sin(p_109376_ * (float)Math.PI);
                    p_109379_.translate((double)((float)i * f), (double)f1, (double)f2);
                    this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                    this.applyItemArmAttackTransform(p_109379_, humanoidarm, p_109376_);
                    if (flag1 && p_109376_ < 0.001F && flag) {
                        p_109379_.translate((double)((float)i * -0.641864F), 0.0D, 0.0D);
                        p_109379_.mulPose(Vector3f.YP.rotationDegrees((float)i * 10.0F));
                    }
                }

                this.renderItem(p_109372_, p_109377_, flag2 ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag2, p_109379_, p_109380_, p_109381_);
            } else {
                boolean flag3 = humanoidarm == HumanoidArm.RIGHT;
                if (p_109372_.isUsingItem() && p_109372_.getUseItemRemainingTicks() > 0 && p_109372_.getUsedItemHand() == p_109375_) {
                    int k = flag3 ? 1 : -1;
                    switch(p_109377_.getUseAnimation()) {
                        case NONE:
                            this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                            break;
                        case EAT:
                        case DRINK:
                            this.applyEatTransform(p_109379_, p_109373_, humanoidarm, p_109377_);
                            this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                            break;
                        case BLOCK:
                            this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                            break;
                        case BOW:
                            this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                            p_109379_.translate((double)((float)k * -0.2785682F), (double)0.18344387F, (double)0.15731531F);
                            p_109379_.mulPose(Vector3f.XP.rotationDegrees(-13.935F));
                            p_109379_.mulPose(Vector3f.YP.rotationDegrees((float)k * 35.3F));
                            p_109379_.mulPose(Vector3f.ZP.rotationDegrees((float)k * -9.785F));
                            float f8 = (float)p_109377_.getUseDuration() - ((float)this.minecraft.player.getUseItemRemainingTicks() - p_109373_ + 1.0F);
                            float f12 = f8 / 20.0F;
                            f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                            if (f12 > 1.0F) {
                                f12 = 1.0F;
                            }

                            if (f12 > 0.1F) {
                                float f15 = Mth.sin((f8 - 0.1F) * 1.3F);
                                float f18 = f12 - 0.1F;
                                float f20 = f15 * f18;
                                p_109379_.translate((double)(f20 * 0.0F), (double)(f20 * 0.004F), (double)(f20 * 0.0F));
                            }

                            p_109379_.translate((double)(f12 * 0.0F), (double)(f12 * 0.0F), (double)(f12 * 0.04F));
                            p_109379_.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
                            p_109379_.mulPose(Vector3f.YN.rotationDegrees((float)k * 45.0F));
                            break;
                        case SPEAR:
                            this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                            p_109379_.translate((double)((float)k * -0.5F), (double)0.7F, (double)0.1F);
                            p_109379_.mulPose(Vector3f.XP.rotationDegrees(-55.0F));
                            p_109379_.mulPose(Vector3f.YP.rotationDegrees((float)k * 35.3F));
                            p_109379_.mulPose(Vector3f.ZP.rotationDegrees((float)k * -9.785F));
                            float f7 = (float)p_109377_.getUseDuration() - ((float)this.minecraft.player.getUseItemRemainingTicks() - p_109373_ + 1.0F);
                            float f11 = f7 / 10.0F;
                            if (f11 > 1.0F) {
                                f11 = 1.0F;
                            }

                            if (f11 > 0.1F) {
                                float f14 = Mth.sin((f7 - 0.1F) * 1.3F);
                                float f17 = f11 - 0.1F;
                                float f19 = f14 * f17;
                                p_109379_.translate((double)(f19 * 0.0F), (double)(f19 * 0.004F), (double)(f19 * 0.0F));
                            }

                            p_109379_.translate(0.0D, 0.0D, (double)(f11 * 0.2F));
                            p_109379_.scale(1.0F, 1.0F, 1.0F + f11 * 0.2F);
                            p_109379_.mulPose(Vector3f.YN.rotationDegrees((float)k * 45.0F));
                    }
                } else if (p_109372_.isAutoSpinAttack()) {
                    this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                    int j = flag3 ? 1 : -1;
                    p_109379_.translate((double)((float)j * -0.4F), (double)0.8F, (double)0.3F);
                    p_109379_.mulPose(Vector3f.YP.rotationDegrees((float)j * 65.0F));
                    p_109379_.mulPose(Vector3f.ZP.rotationDegrees((float)j * -85.0F));
                } else {
                    float f5 = -0.4F * Mth.sin(Mth.sqrt(p_109376_) * (float)Math.PI);
                    float f6 = 0.2F * Mth.sin(Mth.sqrt(p_109376_) * ((float)Math.PI * 2F));
                    float f10 = -0.2F * Mth.sin(p_109376_ * (float)Math.PI);
                    int l = flag3 ? 1 : -1;
                    p_109379_.translate((double)((float)l * f5), (double)f6, (double)f10);
                    this.applyItemArmTransform(p_109379_, humanoidarm, p_109378_);
                    this.applyItemArmAttackTransform(p_109379_, humanoidarm, p_109376_);
                }

                this.renderItem(p_109372_, p_109377_, flag3 ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag3, p_109379_, p_109380_, p_109381_);
            }

            p_109379_.popPose();
        }
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
            float f = DualWieldItem.isDualWielding(this.minecraft.player) ? 1.0f : localplayer.getAttackStrengthScale(1.0F);
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
}
