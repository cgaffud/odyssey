package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.modifier.ModifierUtil;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.combat.SmackPush;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements OdysseyLivingEntity {
    private static final String FLIGHT_VALUE_TAG = "FlightValue";
    private static final String GLIDING_LEVEL_TAG = "GlidingLevel";
    private static final String SLOW_FALL_TAG = "HasSlowFall";
    private int glidingLevel = 0;
    private boolean hasSlowFall = false;
    private int flightValue = 0;
    private SmackPush smackPush = new SmackPush();
    private float nextKnockbackModifier = 1.0f;
    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public boolean hasEffect(MobEffect mobEffect) {return false;}

    @Shadow
    public MobEffectInstance getEffect(MobEffect mobEffect) {return null;}

    @Shadow public abstract Iterable<ItemStack> getArmorSlots();

    @Shadow public abstract CombatTracker getCombatTracker();

    @Shadow public abstract float getMaxHealth();

    public void setAirSupply(int amount) {
        super.setAirSupply(Integer.max(-20, amount));
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    public boolean onBaseTick$isEyeInFluid(LivingEntity livingEntity, TagKey<Fluid> tagkey){
        return livingEntity.isEyeInFluid(tagkey) || this.hasEffect(EffectRegistry.DROWNING.get());
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    public boolean onBaseTick$is(BlockState blockState, Block block){
        return blockState.is(block) && !this.hasEffect(EffectRegistry.DROWNING.get());
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "RETURN"))
    public void onAddAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        compoundTag.putInt(FLIGHT_VALUE_TAG, this.flightValue);
        compoundTag.putBoolean(SLOW_FALL_TAG, this.hasSlowFall);
        compoundTag.putInt(GLIDING_LEVEL_TAG, this.glidingLevel);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "RETURN"))
    public void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        this.flightValue = compoundTag.getInt(FLIGHT_VALUE_TAG);
        this.hasSlowFall = compoundTag.getBoolean(SLOW_FALL_TAG);
        this.glidingLevel = compoundTag.getInt(GLIDING_LEVEL_TAG);
    }

    public void setFlightLevels(boolean hasSlowFall, int glidingLevel){
        this.hasSlowFall = hasSlowFall;
        this.glidingLevel = glidingLevel;
    }

    public void incrementFlight(){
        int amount = this.hasSlowFall ? 1 : this.glidingLevel;
        this.flightValue = Mth.clamp(this.flightValue + amount, 0, this.getMaxFlight());
    }

    public void decrementFlight(){
        int amount = this.hasSlowFall ? 5 : 1;
        this.flightValue = Mth.clamp(this.flightValue - amount, 0, this.getMaxFlight());
    }

    public int getFlightValue(){
        return this.flightValue;
    }

    public int getMaxFlight(){
        return this.hasSlowFall ? 100 : this.glidingLevel * 20;
    }

    public SmackPush getSmackPush(){
        return this.smackPush;
    }

    public void setSmackPush(SmackPush smackPush){
        this.smackPush = smackPush;
    }

    public float getNextKnockbackModifier(){
        return this.nextKnockbackModifier;
    }

    public void setNextKnockbackModifier(float nextKnockbackModifier){
        this.nextKnockbackModifier = nextKnockbackModifier;
    }

    protected float getDamageAfterMagicAbsorb(DamageSource damageSource, float amount) {
        if (damageSource.isBypassMagic()) {
            return amount;
        } else {
            if (this.hasEffect(MobEffects.DAMAGE_RESISTANCE) && damageSource != DamageSource.OUT_OF_WORLD) {
                int i = (this.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = amount * (float)j;
                float f1 = amount;
                amount = Math.max(f / 25.0F, 0.0F);
                float f2 = f1 - amount;
                if (f2 > 0.0F && f2 < 3.4028235E37F) {
                    if (this.getLivingEntity() instanceof ServerPlayer serverPlayer) {
                        serverPlayer.awardStat(Stats.CUSTOM.get(Stats.DAMAGE_RESISTED), Math.round(f2 * 10.0F));
                    } else if (damageSource.getEntity() instanceof ServerPlayer damageSourceServerPlayer) {
                        damageSourceServerPlayer.awardStat(Stats.CUSTOM.get(Stats.DAMAGE_DEALT_RESISTED), Math.round(f2 * 10.0F));
                    }
                }
            }

            if (amount <= 0.0F) {
                return 0.0F;
            } else {
                float protectionArmor = ModifierUtil.getProtectionModifierStrength(this.getArmorSlots(), damageSource);
                if (protectionArmor > 0.0f) {
                    amount = CombatRules.getDamageAfterAbsorb(amount, protectionArmor, 0.0f);
                }

                return amount;
            }
        }
    }

    private LivingEntity getLivingEntity(){
        return (LivingEntity)(Object)this;
    }
}