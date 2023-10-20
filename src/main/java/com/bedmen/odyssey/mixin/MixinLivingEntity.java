package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolder;
import com.bedmen.odyssey.aspect.encapsulator.AspectHolderType;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.combat.SmackPush;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.bedmen.odyssey.combat.damagesource.OdysseyDamageSource;
import com.bedmen.odyssey.effect.FireType;
import com.bedmen.odyssey.entity.OdysseyLivingEntity;
import com.bedmen.odyssey.items.aspect_items.ParryableWeaponItem;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.util.RenderUtil;
import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements OdysseyLivingEntity {
    private static final EntityDataAccessor<FireType> DATA_FIRE_TYPE = SynchedEntityData.defineId(LivingEntity.class, OdysseyDataSerializers.FIRE_TYPE);
    private static final EntityDataAccessor<Float> DATA_TEMPERATURE = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_SHIELD_METER = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<AspectHolder> DATA_PERMABUFF_HOLDER = SynchedEntityData.defineId(LivingEntity.class, OdysseyDataSerializers.ASPECT_HOLDER);
    private static final String FLIGHT_VALUE_TAG = "FlightValue";
    private static final String GLIDING_LEVEL_TAG = "GlidingLevel";
    private static final String SLOW_FALL_TAG = "HasSlowFall";
    private static final String FIRE_TYPE_TAG = "FireType";
    private static final String TEMPERATURE_TAG = "Temperature";
    private static final String SHIELD_METER_TAG = "ShieldMeter";

    private static final String PERMABUFF_STRING = "PermaBuff";
    private static final String TEMPBUFF_STRING = "TempBuff";

    private static final String PERMABUFF_HOLDER_TAG = "PermaBuffHolder";

    private float shieldMeterO = WeaponUtil.SHIELD_METER_MAX;
    private int glidingLevel = 0;
    private boolean hasSlowFall = false;
    private int flightValue = 0;
    private SmackPush smackPush = new SmackPush();
    private final List<Float> knockbackQueue = new ArrayList<>();
    private Optional<Integer> trueHurtTime = Optional.empty();
    private AspectHolder tempBuffHolder = new AspectHolder(List.of(), AspectHolderType.TEMPBUFF);
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
    @Shadow public boolean isUsingItem() {return false;}
    @Shadow protected ItemStack useItem;

    @Shadow protected abstract void dropAllDeathLoot(DamageSource p_21192_);

    @Shadow protected abstract boolean shouldTriggerItemUseEffects();

    @Shadow public abstract ItemStack getUseItem();

    @Shadow public abstract boolean isAlive();

    @Shadow protected abstract void verifyEquippedItem(ItemStack p_181123_);

    @Shadow public abstract Optional<BlockPos> getLastClimbablePos();

    @Shadow public abstract boolean isAutoSpinAttack();
    @Shadow public AttributeMap getAttributes() {return null;}

    @Shadow public abstract void aiStep();

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    public void onDefineSynchedData(CallbackInfo ci) {
        this.entityData.define(DATA_FIRE_TYPE, FireType.NONE);
        this.entityData.define(DATA_TEMPERATURE, 0.0f);
        this.entityData.define(DATA_SHIELD_METER, WeaponUtil.SHIELD_METER_MAX);
        this.entityData.define(DATA_PERMABUFF_HOLDER, new AspectHolder(new ArrayList<>(), AspectHolderType.PERMABUFF));
    }

    public AspectHolder getPermaBuffHolder(){
        return this.entityData.get(DATA_PERMABUFF_HOLDER).copy();
    }

    public void setPermaBuffHolder(AspectHolder newPermaBuffHolder){
        Multimap<Attribute, AttributeModifier> oldMultimap = HashMultimap.create();
        Multimap<Attribute, AttributeModifier> newMultimap = HashMultimap.create();
        AspectUtil.fillAttributeMultimap(this.getPermaBuffHolder(), PERMABUFF_STRING, oldMultimap);
        AspectUtil.fillAttributeMultimap(newPermaBuffHolder, PERMABUFF_STRING, newMultimap);
        this.getAttributes().removeAttributeModifiers(oldMultimap);
        this.getAttributes().addTransientAttributeModifiers(newMultimap);
        this.entityData.set(DATA_PERMABUFF_HOLDER, newPermaBuffHolder.copy());
    }

    public void setPermaBuff(AspectInstance<?> aspectInstance){
        List<AspectInstance<?>> aspectInstanceList = new ArrayList<>(this.getPermaBuffHolder().map.values());
        if(aspectInstanceList.stream().anyMatch(oldAspectInstance -> oldAspectInstance.aspect == aspectInstance.aspect)){
            aspectInstanceList = aspectInstanceList.stream().map(oldAspectInstance -> {
                if(oldAspectInstance.aspect == aspectInstance.aspect){
                    if(aspectInstance.value.equals(aspectInstance.aspect.getBase())){
                        return null;
                    }
                    return aspectInstance;
                }
                return oldAspectInstance;
            }).filter(java.util.Objects::nonNull).collect(Collectors.toList());
        } else {
            aspectInstanceList.add(aspectInstance);
        }
        AspectHolder permabuffHolder = new AspectHolder(aspectInstanceList, AspectHolderType.PERMABUFF);
        this.setPermaBuffHolder(permabuffHolder);
    }

    public void addPermaBuffs(List<AspectInstance<?>> permabuffList){
        List<AspectInstance<?>> aspectInstanceList = new ArrayList<>(this.getPermaBuffHolder().map.values());
        permabuffList.forEach(aspectInstance -> AspectUtil.addInstance(aspectInstanceList, aspectInstance));
        this.setPermaBuffHolder(new AspectHolder(aspectInstanceList, AspectHolderType.PERMABUFF));
    }

    public AspectHolder getTempBuffHolder(){
        return this.tempBuffHolder;
    }
    public void setTempBuffs(List<AspectInstance<?>> aspectInstanceList) {
        AspectHolder oldTempBuffHolder = this.tempBuffHolder;
        this.tempBuffHolder = new AspectHolder(aspectInstanceList, AspectHolderType.TEMPBUFF);
        Multimap<Attribute, AttributeModifier> oldMultimap = HashMultimap.create();
        Multimap<Attribute, AttributeModifier> newMultimap = HashMultimap.create();
        AspectUtil.fillAttributeMultimap(oldTempBuffHolder, TEMPBUFF_STRING, oldMultimap);
        AspectUtil.fillAttributeMultimap(this.tempBuffHolder, TEMPBUFF_STRING, newMultimap);
        this.getAttributes().removeAttributeModifiers(oldMultimap);
        this.getAttributes().addTransientAttributeModifiers(newMultimap);
    }

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
                    float respirationStrength = 1.0f + AspectUtil.getArmorAndBuffsAspectValue(livingEntity, Aspects.RESPIRATION);
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

                        float invulnerabilityMultiplier = Float.min(1.0f, 2.0f / (float)drowningAmount);
                        DamageSource damageSource = OdysseyDamageSource.withInvulnerabilityMultiplier(DamageSource.DROWN, invulnerabilityMultiplier);
                        livingEntity.hurt(damageSource, 2.0f);
                    }
                }

                if (!this.level.isClientSide && this.isPassenger() && this.getVehicle() != null && !this.getVehicle().canBeRiddenUnderFluidType(this.getEyeInFluidType(), this)) {
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
        compoundTag.putString(FIRE_TYPE_TAG, this.getFireType().name());
        compoundTag.putFloat(TEMPERATURE_TAG, this.getTemperature());
        compoundTag.putFloat(SHIELD_METER_TAG, this.getShieldMeter());
        CompoundTag permabuffHolderTag = this.getPermaBuffHolder().toCompoundTag();
        compoundTag.put(PERMABUFF_HOLDER_TAG, permabuffHolderTag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "RETURN"))
    public void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci){
        this.flightValue = compoundTag.getInt(FLIGHT_VALUE_TAG);
        this.hasSlowFall = compoundTag.getBoolean(SLOW_FALL_TAG);
        this.glidingLevel = compoundTag.getInt(GLIDING_LEVEL_TAG);
        if(compoundTag.contains(FIRE_TYPE_TAG)){
            this.setFireType(FireType.valueOf(compoundTag.getString(FIRE_TYPE_TAG)));
        }
        this.setTemperature(compoundTag.getFloat(TEMPERATURE_TAG));
        if(compoundTag.contains(SHIELD_METER_TAG)){
            this.setShieldMeter(compoundTag.getFloat(SHIELD_METER_TAG));
        }
        if(compoundTag.contains(PERMABUFF_HOLDER_TAG)){
            this.setPermaBuffHolder(AspectHolder.fromCompoundTag(compoundTag.getCompound(PERMABUFF_HOLDER_TAG)));
        }
    }

    public void setFlightLevels(boolean hasSlowFall, int glidingLevel){
        this.hasSlowFall = hasSlowFall;
        this.glidingLevel = glidingLevel;
    }

    public void incrementFlight(){
        int amount = this.hasSlowFall ? 1 : this.getMaxFlight() / 20;
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
        return this.hasSlowFall ? 100 : this.glidingLevel;
    }

    public SmackPush getSmackPush(){
        return this.smackPush;
    }

    public void setSmackPush(SmackPush smackPush){
        this.smackPush = smackPush;
    }

    public float popKnockbackAspectQueue(){
        if(this.knockbackQueue.isEmpty()){
            return 0.0f;
        }
        float knockbackAspect = this.knockbackQueue.get(0);
        this.knockbackQueue.remove(0);
        return knockbackAspect;
    }

    public void pushKnockbackAspectQueue(float knockbackAspect){
        this.knockbackQueue.add(knockbackAspect);
    }

    public void setTrueHurtTime(Optional<Integer> trueHurtTime){
        this.trueHurtTime = trueHurtTime;
    }

    public Optional<Integer> getTrueHurtTime(){
        return this.trueHurtTime;
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
                float protectionArmor = 5f * AspectUtil.getProtectionAspectValue(this.getLivingEntity(), damageSource);
                if (protectionArmor != 0f) {
                    amount = CombatRules.getDamageAfterAbsorb(amount, protectionArmor, 0.0f);
                }

                return amount;
            }
        }
    }

    public boolean canFreeze() {
        return false;
    }

    public boolean isFreezing() {
        return this.isCold();
    }

    public float getPercentFrozen() {
        return Mth.clamp(-this.getTemperature(), 0.0f, 1.0f);
    }

    public boolean isFullyFrozen() {
        return this.getTemperature() <= -1.0f;
    }

    // Ripped out the 5 tick delay on blocking
    public boolean isBlocking() {
        if (this.isUsingItem() && !this.useItem.isEmpty()) {
            return this.useItem.is(OdysseyItemTags.PARRYABLES);
        } else {
            return false;
        }
    }

    // Remove piercing arrows going through shields, made the block angle dependent on the shield
    public boolean isDamageSourceBlocked(DamageSource damageSource) {
        if (!damageSource.isBypassArmor() && this.isBlocking()) {
            Vec3 damageSourcePosition = damageSource.getSourcePosition();
            if (damageSourcePosition != null) {
                Vec3 viewVector = this.getViewVector(1.0F);
                Vec3 vectorToPosition = damageSourcePosition.vectorTo(this.position());
                vectorToPosition = new Vec3(vectorToPosition.x, 0.0D, vectorToPosition.z).normalize();;
                ItemStack parryable = this.getUseItem();
                float blockingWidthAngle = ((ParryableWeaponItem) parryable.getItem()).getBlockingAngleWidth(parryable);

                float angleHalvedRadians = blockingWidthAngle * Mth.PI / 360f;
                if (vectorToPosition.dot(viewVector) < -Mth.cos(angleHalvedRadians)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void tryAddFrost() {
        if (!this.getBlockStateOn().isAir() && this.isFreezing()) {
            AttributeInstance attributeinstance = this.getLivingEntity().getAttribute(Attributes.MOVEMENT_SPEED);
            if (attributeinstance == null) {
                return;
            }
            float slowAmount = -0.05F * this.getPercentFrozen();
            attributeinstance.addTransientModifier(new AttributeModifier(LivingEntity.SPEED_MODIFIER_POWDER_SNOW_UUID, "Powder snow slow", (double)slowAmount, AttributeModifier.Operation.ADDITION));
        }

    }

    public float getTemperature(){
        return this.entityData.get(DATA_TEMPERATURE);
    }

    public void setTemperature(float temperature){
        this.entityData.set(DATA_TEMPERATURE, temperature);
    }

    public boolean displayFireAnimation() {
        return this.isOnFire() && !this.isSpectator() && RenderUtil.getStrongestFireType(this.getLivingEntity()).isNone();
    }

    public FireType getFireType(){
        return this.entityData.get(DATA_FIRE_TYPE);
    }

    public void setFireType(FireType fireType){
        this.entityData.set(DATA_FIRE_TYPE, fireType);
    }

    public float getShieldMeter(){
        return this.entityData.get(DATA_SHIELD_METER);
    }

    public float getShieldMeterO(){
        return this.shieldMeterO;
    }

    public void setShieldMeter(float shieldMeter){
        shieldMeter = Mth.clamp(shieldMeter, 0.0f, WeaponUtil.SHIELD_METER_MAX);
        this.entityData.set(DATA_SHIELD_METER, shieldMeter);
    }

    public void updateShieldMeterO(){
        this.shieldMeterO = this.getShieldMeter();
    }

    public void adjustShieldMeter(float amount){
        this.setShieldMeter(this.getShieldMeter() + amount);
    }

    private LivingEntity getLivingEntity(){
        return (LivingEntity)(Object)this;
    }
}