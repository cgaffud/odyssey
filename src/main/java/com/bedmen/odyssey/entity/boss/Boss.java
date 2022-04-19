package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.registry.EffectRegistry;
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

import javax.annotation.Nullable;

public abstract class Boss extends Monster {
    private static final int DESPAWN_TIME = 1200;
    private int despawnTimer;
    protected float damageReduction = 1.0f;
    private int cachedNearbyPlayers = 0;
    protected Boss(EntityType<? extends Monster> p_i48576_1_, Level p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
    }

    public void tick(){
        super.tick();
        if(this.getTarget() == null){
            this.despawnTimer++;
        }
        this.damageReduction = this.difficultyDamageReductionMultiplier() * this.nearbyPlayerDamageReductionMultiplier();
        ServerBossEvent serverBossInfo = this.getBossEvent();
        if(serverBossInfo == null){
            this.cachedNearbyPlayers = 0;
        } else {
            this.cachedNearbyPlayers = (int) serverBossInfo.getPlayers().stream().filter(this::validTargetPredicate).count();
        }
    }

    public void setCustomName(@Nullable Component component) {
        super.setCustomName(component);
        ServerBossEvent serverBossEvent = this.getBossEvent();
        if(canChangeBossEvent() && serverBossEvent != null){
            serverBossEvent.setName(this.getDisplayName());
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        ServerBossEvent serverBossEvent = this.getBossEvent();
        if(canChangeBossEvent() && serverBossEvent != null){
            serverBossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    public abstract ServerBossEvent getBossEvent();

    public boolean canChangeBossEvent(){
        return true;
    }

    public void checkDespawn() {
        if ((this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) || this.despawnTimer > DESPAWN_TIME) {
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
        return super.hurt(damageSource, amount);
    }

    public void setTarget(@Nullable LivingEntity livingEntity) {
        if(livingEntity instanceof Monster){
            return;
        }
        super.setTarget(livingEntity);
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
}
