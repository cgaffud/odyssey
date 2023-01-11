package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.player.IOdysseyPlayer;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyBowItem;
import com.bedmen.odyssey.items.odyssey_versions.OdysseyShieldItem;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.weapon.BowAbility;
import com.bedmen.odyssey.weapon.MeleeWeaponAbility;
import com.bedmen.odyssey.weapon.OdysseyMeleeWeapon;
import com.bedmen.odyssey.weapon.WeaponUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
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

    @Shadow public abstract void increaseScore(int p_36402_);

    private int attackStrengthTickerO;
    private boolean isSniperScoping;

    protected MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "RETURN"))
    public void onAddAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        compoundTag.putInt("AttackStrengthTickerO", this.attackStrengthTickerO);
        compoundTag.putBoolean("IsSniperScoping", this.isSniperScoping);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "RETURN"))
    public void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        this.attackStrengthTickerO = compoundTag.getInt("AttackStrengthTickerO");
        this.isSniperScoping = compoundTag.getBoolean("IsSniperScoping");
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("HEAD"), cancellable = true)
    private void onGetCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> cir) {
        if(WeaponUtil.isDualWielding(getPlayerEntity())){
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
        boolean isSniperScopingO = this.isSniperScoping;
        this.isSniperScoping = this.getMainHandItem().getItem() instanceof OdysseyBowItem odysseyBowItem && odysseyBowItem.hasAbility(BowAbility.SPYGLASS) && this.isShiftKeyDown();
        if(!isSniperScopingO && this.isSniperScoping){
            this.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
        } else if (isSniperScopingO && !this.isSniperScoping){
            this.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
        }
    }

    public boolean isSniperScoping() {
        return this.isSniperScoping;
    }

    protected void blockUsingShield(LivingEntity livingEntity) {
        super.blockUsingShield(livingEntity);
        if (livingEntity.getMainHandItem().getItem() instanceof OdysseyMeleeWeapon odysseyMeleeWeapon
        && odysseyMeleeWeapon.getMeleeWeaponClass().hasAbility(MeleeWeaponAbility.SHIELD_BASH)) {
            this.disableShield(true);
        }
    }

    public void disableShield(boolean isGuaranteed) {
        float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (isGuaranteed) {
            f += 0.75F;
        }

        if (this.random.nextFloat() < f) {
            Item useItem = this.getUseItem().getItem();
            int recoveryTime = useItem instanceof OdysseyShieldItem odysseyShieldItem ? odysseyShieldItem.getRecoveryTime() : 100;
            ITagManager<Item> itemITagManager = ForgeRegistries.ITEMS.tags();
            if(itemITagManager != null){
                for(Item item : itemITagManager.getTag(OdysseyItemTags.SHIELDS).stream().toList()){
                    getPlayerEntity().getCooldowns().addCooldown(item, recoveryTime);
                }
                this.stopUsingItem();
                this.level.broadcastEntityEvent(this, (byte)30);
            }
        }
    }

    public boolean isDamageSourceBlocked(DamageSource damageSource) {
        Entity entity = damageSource.getDirectEntity();
        boolean flag = false;
        if (entity instanceof AbstractArrow) {
            AbstractArrow abstractarrow = (AbstractArrow)entity;
            // Change from > 0 to > EnchantmentUtil.getImpenetrable(this)
            if (abstractarrow.getPierceLevel() > EnchantmentUtil.getImpenetrable(this)) {
                flag = true;
            }
        }

        if (!damageSource.isBypassArmor() && this.isBlocking() && !flag) {
            Vec3 vec32 = damageSource.getSourcePosition();
            if (vec32 != null) {
                Vec3 vec3 = this.getViewVector(1.0F);
                Vec3 vec31 = vec32.vectorTo(this.position()).normalize();
                vec31 = new Vec3(vec31.x, 0.0D, vec31.z);
                if (vec31.dot(vec3) < 0.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    private Player getPlayerEntity(){
        return (Player)(Object)this;
    }
}
