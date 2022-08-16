package com.bedmen.odyssey.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public abstract class PuppetEntity extends Entity {

    public PuppetEntity(EntityType<? extends PuppetEntity> entityType, Level level) {
        super(entityType, level);
    }

    public boolean needsDiscardingOrRegeneration(){
        return !this.isAlive() || this.isRemoved() || this.touchingUnloadedChunk();
    }

    public abstract boolean shouldBeDiscarded();

    protected void defineSynchedData() {

    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
