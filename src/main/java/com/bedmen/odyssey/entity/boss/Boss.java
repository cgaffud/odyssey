package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.combat.OdysseyDamageSource;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class Boss extends Monster {
    @NotNull
    public final ServerBossEvent bossEvent = new ServerBossEvent(this.getDisplayName(), this.getBossBarColor(), BossEvent.BossBarOverlay.PROGRESS);
    private static final int DESPAWN_TIME = 1200;
    private int despawnTimer;
    protected float damageReduction = 1.0f;
    private int cachedNearbyPlayers = 0;
    protected Boss(EntityType<? extends Boss> entityType, Level level) {
        super(entityType, level);
    }

    public void tick(){
        this.cachedNearbyPlayers = (int) this.bossEvent.getPlayers().stream().filter(this::validTargetPredicate).count();
        this.damageReduction = this.difficultyDamageReductionMultiplier() * this.nearbyPlayerDamageReductionMultiplier();
        super.tick();
        if(this.cachedNearbyPlayers <= 0){
            this.despawnTimer++;
        } else {
            this.despawnTimer = 0;
        }
        if(this.level.isClientSide){
            this.clientTick();
        } else {
            this.serverTick();
        }
    }

    public void clientTick() {}

    public void serverTick() {}

    public void setCustomName(@Nullable Component component) {
        super.setCustomName(component);
        this.bossEvent.setName(this.getDisplayName());
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    protected abstract BossEvent.BossBarColor getBossBarColor();

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL || this.despawnTimer > DESPAWN_TIME) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public int decreaseAirSupply(int pAir) {
        return pAir;
    }

    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    public boolean canChangeDimensions() {
        return false;
    }

    protected boolean canRide(Entity p_184228_1_) {
        return false;
    }

    protected boolean isAlwaysExperienceDropper() {
        return true;
    }

    public boolean canBeAffected(MobEffectInstance mobEffectInstance) {
        return mobEffectInstance.getEffect() == EffectRegistry.SHATTERED.get();
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        amount *= this.damageReduction;
        damageSource = OdysseyDamageSource.withInvulnerabilityMultiplier(damageSource, 1.0f / Integer.max(1, this.getNearbyPlayerNumber()));
        return super.hurt(damageSource, amount);
    }

    public void setTarget(@Nullable LivingEntity livingEntity) {
        if(livingEntity instanceof ServerPlayer serverPlayer && validTargetPredicate(serverPlayer)){
            super.setTarget(livingEntity);
        }
    }

    public boolean validTargetPredicate(ServerPlayer serverPlayer){
        double followRange = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        return serverPlayer.isAlive() && !serverPlayer.isInvulnerable() && !serverPlayer.isCreative() && !serverPlayer.isSpectator() && this.distanceToSqr(serverPlayer) <= followRange * followRange;
    }

    public int getNearbyPlayerNumber(){
        return this.cachedNearbyPlayers;
    }

    public float nearbyPlayerDamageReductionMultiplier(){
        return 1.0f / Float.max(1f, this.getNearbyPlayerNumber() * 0.75f + 0.25f);
    }

    public float difficultyDamageReductionMultiplier(){
        switch(this.level.getDifficulty()){
            default: return 1.5f;
            case NORMAL: return 1.0f;
            case HARD: return 2.0f/3.0f;
        }
    }

    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    public float getDamageReduction() {
        return this.damageReduction;
    }

    private static final String DESPAWN_TIMER_TAG = "DespawnTimer";

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt(DESPAWN_TIMER_TAG, this.despawnTimer);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.despawnTimer = compoundTag.getInt(DESPAWN_TIMER_TAG);
    }
}
