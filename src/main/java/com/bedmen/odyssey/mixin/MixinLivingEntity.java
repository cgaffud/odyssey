package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.combat.CombatUtil;
import com.bedmen.odyssey.combat.SetBonusAbility;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.modifier.ModifierUtil;
import com.bedmen.odyssey.modifier.Modifiers;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.combat.SmackPush;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.google.common.base.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Optional;

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

    @Shadow public boolean hasEffect(MobEffect mobEffect) {return false;}
    @Shadow public MobEffectInstance getEffect(MobEffect mobEffect) {return null;}
    @Shadow public abstract Iterable<ItemStack> getArmorSlots();
    @Shadow public float oAttackAnim;
    @Shadow public float attackAnim;
    @Shadow public Optional<BlockPos> getSleepingPos() {return Optional.empty();}
    @Shadow private void setPosToBed(BlockPos p_21081_) {}
    @Shadow public boolean canSpawnSoulSpeedParticle() {return false;}
    @Shadow protected void spawnSoulSpeedParticle() {}
    @Shadow public boolean canBreatheUnderwater() {return false;}
    @Shadow public int decreaseAirSupply(int p_21303_) {return 0;}
    @Shadow protected int increaseAirSupply(int p_21307_) {return 0;}
    @Shadow public BlockPos lastPos;
    @Shadow protected void onChangedBlock(BlockPos p_21175_) {}
    @Shadow public int hurtTime;
    @Shadow public boolean isDeadOrDying() {return false;}
    @Shadow protected void tickDeath() {}
    @Shadow protected int lastHurtByPlayerTime;
    @Shadow protected Player lastHurtByPlayer;
    @Shadow private LivingEntity lastHurtMob;
    @Shadow private LivingEntity lastHurtByMob;
    @Shadow public void setLastHurtByMob(@Nullable LivingEntity p_21039_) {}
    @Shadow private int lastHurtByMobTimestamp;
    @Shadow protected void tickEffects() {}
    @Shadow protected float animStep;
    @Shadow protected float animStepO;
    @Shadow public float yBodyRot;
    @Shadow public float yBodyRotO;
    @Shadow public float yHeadRot;
    @Shadow public float yHeadRotO;

    @Shadow @Nullable public abstract LivingEntity getKillCredit();

    public void baseTick() {
        LivingEntity livingEntity = this.getLivingEntity();
        this.oAttackAnim = this.attackAnim;
        if (this.firstTick) {
            this.getSleepingPos().ifPresent(this::setPosToBed);
        }

        if (this.canSpawnSoulSpeedParticle()) {
            this.spawnSoulSpeedParticle();
        }

        super.baseTick();
        this.level.getProfiler().push("livingEntityBaseTick");
        if (this.fireImmune() || this.level.isClientSide) {
            this.clearFire();
        }

        if (this.isAlive()) {
            boolean flag = livingEntity instanceof Player;
            if (this.isInWall()) {
                this.hurt(DamageSource.IN_WALL, 1.0F);
            } else if (flag && !this.level.getWorldBorder().isWithinBounds(this.getBoundingBox())) {
                double d0 = this.level.getWorldBorder().getDistanceToBorder(this) + this.level.getWorldBorder().getDamageSafeZone();
                if (d0 < 0.0D) {
                    double d1 = this.level.getWorldBorder().getDamagePerBlock();
                    if (d1 > 0.0D) {
                        this.hurt(DamageSource.IN_WALL, (float)Math.max(1, Mth.floor(-d0 * d1)));
                    }
                }
            }

            int drowningAmount = this.isEyeInFluid(FluidTags.WATER) && !this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ())).is(Blocks.BUBBLE_COLUMN) ? 1 : 0;
            drowningAmount += livingEntity.hasEffect(EffectRegistry.DROWNING.get()) ? livingEntity.getEffect(EffectRegistry.DROWNING.get()).getAmplifier() + 1 : 0;
            if (drowningAmount > 0) {
                boolean cannotBreatheUnderWater = !this.canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing(livingEntity) && (!flag || !((Player)livingEntity).getAbilities().invulnerable);
                if (cannotBreatheUnderWater) {
                    float respirationStrength = 1.0f + ModifierUtil.getFloatModifierValueFromArmor(livingEntity, Modifiers.RESPIRATION);
                    float airLossChance = 1.0f / respirationStrength;
                    for(int i = 0; i < drowningAmount; i++){
                        if(airLossChance >= livingEntity.getRandom().nextFloat()){
                            this.setAirSupply(this.decreaseAirSupply(this.getAirSupply()));
                        }
                    }
                    if (this.getAirSupply() <= -20) {
                        this.setAirSupply(0);
                        Vec3 vec3 = this.getDeltaMovement();

                        for(int i = 0; i < 8; ++i) {
                            double d2 = this.random.nextDouble() - this.random.nextDouble();
                            double d3 = this.random.nextDouble() - this.random.nextDouble();
                            double d4 = this.random.nextDouble() - this.random.nextDouble();
                            this.level.addParticle(ParticleTypes.BUBBLE, this.getX() + d2, this.getY() + d3, this.getZ() + d4, vec3.x, vec3.y, vec3.z);
                        }

                        this.hurt(DamageSource.DROWN, 2.0F);
                        if(drowningAmount > 2){
                            int invulnerabilityFrames = 20 / drowningAmount;
                            this.invulnerableTime = 10 + invulnerabilityFrames;
                            this.hurtTime = invulnerabilityFrames;
                        }
                    }
                }

                if (!this.level.isClientSide && this.isPassenger() && this.getVehicle() != null && !this.getVehicle().canBeRiddenInWater(this)) {
                    this.stopRiding();
                }
            } else if (this.getAirSupply() < this.getMaxAirSupply()) {
                this.setAirSupply(this.increaseAirSupply(this.getAirSupply()));
            }

            if (!this.level.isClientSide) {
                BlockPos blockpos = this.blockPosition();
                if (!Objects.equal(this.lastPos, blockpos)) {
                    this.lastPos = blockpos;
                    this.onChangedBlock(blockpos);
                }
            }
        }

        if (this.isAlive() && (this.isInWaterRainOrBubble() || this.isInPowderSnow)) {
            if (!this.level.isClientSide && this.wasOnFire) {
                this.playEntityOnFireExtinguishedSound();
            }

            this.clearFire();
        }

        if (this.hurtTime > 0) {
            --this.hurtTime;
        }

        if (this.invulnerableTime > 0 && !(livingEntity instanceof ServerPlayer)) {
            --this.invulnerableTime;
        }

        if (this.isDeadOrDying() && this.level.shouldTickDeath(this)) {
            this.tickDeath();
        }

        if (this.lastHurtByPlayerTime > 0) {
            --this.lastHurtByPlayerTime;
        } else {
            this.lastHurtByPlayer = null;
        }

        if (this.lastHurtMob != null && !this.lastHurtMob.isAlive()) {
            this.lastHurtMob = null;
        }

        if (this.lastHurtByMob != null) {
            if (!this.lastHurtByMob.isAlive()) {
                this.setLastHurtByMob((LivingEntity)null);
            } else if (this.tickCount - this.lastHurtByMobTimestamp > 100) {
                this.setLastHurtByMob((LivingEntity)null);
            }
        }

        this.tickEffects();
        this.animStepO = this.animStep;
        this.yBodyRotO = this.yBodyRot;
        this.yHeadRotO = this.yHeadRot;
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
        this.level.getProfiler().pop();
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
                float protectionArmor = 5.0f * ModifierUtil.getProtectionModifierStrength(this.getLivingEntity(), damageSource);
                if (protectionArmor > 0.0f) {
                    amount = CombatRules.getDamageAfterAbsorb(amount, protectionArmor, 0.0f);
                }

                return amount;
            }
        }
    }

    public boolean canFreeze() {
        if (this.isSpectator()) {
            return false;
        } else {
            LivingEntity livingEntity = this.getLivingEntity();
            boolean itemTagNotFreezeImmune = !livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
                    && !livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
                    && !livingEntity.getItemBySlot(EquipmentSlot.LEGS).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
                    && !livingEntity.getItemBySlot(EquipmentSlot.FEET).is(ItemTags.FREEZE_IMMUNE_WEARABLES);
            boolean modifierNotFreezeImmune = ModifierUtil.getIntegerModifierValueFromArmor(livingEntity, Modifiers.FREEZE_IMMUNITY) <= 0;
            return itemTagNotFreezeImmune && modifierNotFreezeImmune && super.canFreeze();
        }
    }

    private LivingEntity getLivingEntity(){
        return (LivingEntity)(Object)this;
    }
}