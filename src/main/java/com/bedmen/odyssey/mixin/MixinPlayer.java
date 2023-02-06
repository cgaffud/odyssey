package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.entity.player.OdysseyPlayer;
import com.bedmen.odyssey.entity.player.permabuff.Permabuff;
import com.bedmen.odyssey.entity.player.permabuff.PermabuffMap;
import com.bedmen.odyssey.items.aspect_items.AspectShieldItem;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
public abstract class MixinPlayer extends LivingEntity implements OdysseyPlayer {

    private static final EntityDataAccessor<PermabuffMap> DATA_PERMABUFF_MAP = SynchedEntityData.defineId(Player.class, OdysseyDataSerializers.PERMABUFF_MAP);

    @Shadow
    public void awardStat(Stat<?> p_36247_) {}
    @Shadow
    public float getCurrentItemAttackStrengthDelay() {return 0.0f;}

    @Shadow public abstract void playSound(SoundEvent p_36137_, float p_36138_, float p_36139_);

    private int attackStrengthTickerO;
    private boolean isSniperScoping;
    private static final String PERMABUFF_MAP_TAG = Odyssey.MOD_ID + ":PermabuffMap";

    protected MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "defineSynchedData", at = @At(value = "RETURN"))
    public void onDefineSynchedData(CallbackInfo ci){
        this.entityData.define(DATA_PERMABUFF_MAP, new PermabuffMap());
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "RETURN"))
    public void onAddAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        compoundTag.putInt("AttackStrengthTickerO", this.attackStrengthTickerO);
        compoundTag.putBoolean("IsSniperScoping", this.isSniperScoping);
        CompoundTag permabuffMapTag =  this.getPermabuffMap().toCompoundTag();
        compoundTag.put(PERMABUFF_MAP_TAG, permabuffMapTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "RETURN"))
    public void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        this.attackStrengthTickerO = compoundTag.getInt("AttackStrengthTickerO");
        this.isSniperScoping = compoundTag.getBoolean("IsSniperScoping");
        if(compoundTag.contains(PERMABUFF_MAP_TAG)){
            this.setPermabuffMap(PermabuffMap.fromCompoundTag(compoundTag.getCompound(PERMABUFF_MAP_TAG), PermabuffMap.class));
        }
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("HEAD"), cancellable = true)
    private void onGetCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> cir) {
        if(WeaponUtil.isDualWielding(getPlayer())){
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
        ItemStack itemStack = this.getMainHandItem();
        this.isSniperScoping = AspectUtil.hasBooleanAspect(itemStack, Aspects.SPYGLASS) && this.isShiftKeyDown();
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
        ItemStack itemStack = livingEntity.getMainHandItem();
        if (AspectUtil.hasBooleanAspect(itemStack, Aspects.SHIELD_BASH)) {
            this.disableShield(true);
        }
    }

    public void disableShield(boolean isGuaranteed) {
        float f = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (isGuaranteed) {
            f += 0.75F;
        }

        if (this.random.nextFloat() < f) {
            ItemStack shield = this.getUseItem();
            Item shieldItem = shield.getItem();
            int recoveryTime = shieldItem instanceof AspectShieldItem aspectShieldItem ? aspectShieldItem.getRecoveryTime(shield) : 100;
            ITagManager<Item> itemITagManager = ForgeRegistries.ITEMS.tags();
            if(itemITagManager != null){
                for(Item item : itemITagManager.getTag(OdysseyItemTags.SHIELDS).stream().toList()){
                    getPlayer().getCooldowns().addCooldown(item, recoveryTime);
                }
                this.stopUsingItem();
                this.level.broadcastEntityEvent(this, (byte)30);
            }
        }
    }

    public boolean isDamageSourceBlocked(DamageSource damageSource) {
        if (!damageSource.isBypassArmor() && this.isBlocking()) {
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

    public int getPermabuffValue(Permabuff permabuff){
        return this.getPermabuffMap().getNonNull(permabuff);
    }

    public PermabuffMap getPermabuffMap(){
        return this.entityData.get(DATA_PERMABUFF_MAP).copy();
    }

    public void setPermabuffMap(PermabuffMap permabuffMap){
        this.entityData.set(DATA_PERMABUFF_MAP, permabuffMap.copy());
    }

    public void addPermabuffMap(PermabuffMap permabuffMap){
        this.entityData.set(DATA_PERMABUFF_MAP, this.getPermabuffMap().combine(permabuffMap));
    }

    private Player getPlayer(){
        return (Player)(Object)this;
    }
}
