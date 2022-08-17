package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.registry.EffectRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class BossEntity extends LivingEntity {
    private static final Iterable<ItemStack> ARMOR_SLOTS = ImmutableList.of();
    private static final int DESPAWN_TIME = 1200;
    private int despawnTimer;
    protected float damageReduction = 1.0f;
    private int cachedNearbyPlayers = 0;

    public BossEntity(EntityType<? extends BossEntity> entityType, Level level) {
        super(entityType, level);
    }

    public void tick(){
        super.tick();
        if(this.cachedNearbyPlayers <= 0){
            this.despawnTimer++;
        }
        this.damageReduction = this.difficultyDamageReductionMultiplier() * this.nearbyPlayerDamageReductionMultiplier();
        ServerBossEvent serverBossInfo = this.getBossEvent();
        this.cachedNearbyPlayers = (int) serverBossInfo.getPlayers().stream().filter(this::validTargetPredicate).count();
        if(this.level.isClientSide){
            this.clientTick();
        } else {
            this.serverTick();
        }
    }
    
    public void clientTick() {}
    
    public void serverTick() {
        ServerBossEvent serverBossEvent = this.getBossEvent();
        serverBossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void setCustomName(@Nullable Component component) {
        super.setCustomName(component);
        ServerBossEvent serverBossEvent = this.getBossEvent();
        serverBossEvent.setName(this.getDisplayName());
    }

    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.getBossEvent().addPlayer(serverPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.getBossEvent().removePlayer(serverPlayer);
    }

    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL || this.despawnTimer > DESPAWN_TIME) {
            this.discard();
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

    protected boolean canRide(Entity entity) {
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

    public Iterable<ItemStack> getArmorSlots() {
        return ARMOR_SLOTS;
    }

    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {}

    public HumanoidArm getMainArm() {
        return null;
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @NotNull
    public abstract ServerBossEvent getBossEvent();
}
