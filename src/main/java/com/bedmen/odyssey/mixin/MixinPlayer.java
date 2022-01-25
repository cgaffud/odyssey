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
