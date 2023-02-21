package com.bedmen.odyssey.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;

public abstract class BossSubEntity<T extends BossMaster> extends Monster implements SubEntity<T> {

    private static final EntityDataAccessor<Integer> DATA_MASTER_ID = SynchedEntityData.defineId(BossSubEntity.class, EntityDataSerializers.INT);

    protected BossSubEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_MASTER_ID, -1);
    }

    public void setMasterId(int masterId) {
        this.entityData.set(DATA_MASTER_ID, masterId);
    }

    public Optional<T> getMaster() {
        int masterId = this.entityData.get(DATA_MASTER_ID);
        Entity entity = this.level.getEntity(masterId);
        // instanceof also checks if it is null
        if(entity != null) {
            return Optional.of((T)entity);
        }
        return Optional.empty();
    }

    public boolean hurtDirectly(DamageSource damageSource, float amount) {
        return super.hurt(damageSource, amount);
    }

    public void remove(RemovalReason removalReason) {
        if(removalReason == RemovalReason.DISCARDED || removalReason == RemovalReason.KILLED || this.getMaster().isEmpty()) {
            super.remove(removalReason);
        } else {
            this.getMaster().ifPresent(master -> master.handleSubEntity(this));
        }
    }

    public boolean save(CompoundTag compoundTag) {
        return false;
    }

    public void kill() {
        this.hurtDirectly(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
    }

    public void checkDespawn() {
        if (this.getMaster().isEmpty())
            this.discard();
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        SubEntity.super.writeSpawnData(friendlyByteBuf);
    }

    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        SubEntity.super.readSpawnData(friendlyByteBuf);
    }
}
