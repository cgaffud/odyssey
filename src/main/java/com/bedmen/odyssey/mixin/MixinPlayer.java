package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.items.equipment.DualWieldItem;
import com.bedmen.odyssey.items.equipment.SniperBowItem;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.util.WeaponUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements IOdysseyPlayer {

    @Shadow
    public ItemCooldowns getCooldowns() {return null;}
    @Shadow
    public void awardStat(Stat<?> p_36247_) {}
    @Shadow
    public float getCurrentItemAttackStrengthDelay() {return 0.0f;}

    private int attackStrengthTickerO;
    private boolean isSniperScopingO;
    private boolean isSniperScoping;

    protected MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("HEAD"), cancellable = true)
    private void onGetCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> cir) {
        if(DualWieldItem.isDualWielding(getPlayerEntity())){
            cir.setReturnValue((float)(1.0D / this.getAttributeValue(Attributes.ATTACK_SPEED) * 10.0D));
            cir.cancel();
        }
    }

    //Todo shield
//    public void disableShield(boolean b) {
//        float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
//        if (b) {
//            f += 0.75F;
//        }
//
//        if (this.random.nextFloat() < f) {
//            int recoveryTime = this.getUseItem().getItem() instanceof OdysseyShieldItem odysseyShieldItem ? odysseyShieldItem.getRecoveryTime() : WeaponUtil.DEFAULT_RECOVERY_TIME;
//            for(Item item : OdysseyItemTags.SHIELDS.getValues()){
//                this.getCooldowns().addCooldown(item, recoveryTime);
//            }
//            this.stopUsingItem();
//            this.level.broadcastEntityEvent(this, (byte)30);
//        }
//    }

    //Todo shield
    /**
     * In vanilla damage of atleast 3 is required to hurt a shield. Now any amount of damage hurts a shield
     */
//    protected void hurtCurrentlyUsedShield(float p_36383_) {
//        if (this.useItem.canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK)) {
//            if (!this.level.isClientSide) {
//                this.awardStat(Stats.ITEM_USED.get(this.useItem.getItem()));
//            }
//
//            int i = 1 + Mth.floor(p_36383_);
//            InteractionHand interactionhand = this.getUsedItemHand();
//            this.useItem.hurtAndBreak(i, this, (p_36149_) -> {
//                p_36149_.broadcastBreakEvent(interactionhand);
//                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(getPlayerEntity(), this.useItem, interactionhand);
//            });
//            if (this.useItem.isEmpty()) {
//                if (interactionhand == InteractionHand.MAIN_HAND) {
//                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
//                } else {
//                    this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
//                }
//
//                this.useItem = ItemStack.EMPTY;
//                this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
//            }
//
//        }
//    }


    @Inject(method = "resetAttackStrengthTicker", at = @At(value = "HEAD"))
    private void onResetAttackStrengthTicker(CallbackInfo ci) {
        this.attackStrengthTickerO = this.attackStrengthTicker;
    }

    @Inject(method = "isScoping", at = @At(value = "HEAD"), cancellable = true)
    private void onIsScoping(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue((this.isUsingItem() && this.getUseItem().is(Items.SPYGLASS)) || this.isSniperScoping());
        cir.cancel();
    }

    public float getAttackStrengthScaleO() {
        return Mth.clamp(((float)this.attackStrengthTickerO + 0.5f) / this.getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F);
    }

    public void updateSniperScoping() {
        this.isSniperScopingO = this.isSniperScoping;
        this.isSniperScoping = this.getMainHandItem().getItem() instanceof SniperBowItem && this.isShiftKeyDown();
        if(!this.isSniperScopingO && this.isSniperScoping){
            this.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
        } else if (this.isSniperScopingO && !this.isSniperScoping){
            this.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
        }
    }

    public boolean isSniperScoping() {
        return this.isSniperScoping;
    }

    private Player getPlayerEntity(){
        return (Player)(Object)this;
    }
}
