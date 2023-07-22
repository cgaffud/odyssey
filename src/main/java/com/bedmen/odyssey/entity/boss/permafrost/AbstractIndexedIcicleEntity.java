package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.entity.boss.BossSubEntity;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

abstract public class AbstractIndexedIcicleEntity extends BossSubEntity<PermafrostMaster> {

    private static final EntityDataAccessor<Integer> DATA_ICICLE_INDEX = SynchedEntityData.defineId(PermafrostBigIcicleEntity.class, EntityDataSerializers.INT);

    protected AbstractIndexedIcicleEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public AbstractIndexedIcicleEntity(EntityType<? extends Monster> entityType, Level level, int id) {
        this(entityType, level);
        this.setIcicleIndex(id);
    }

    public void setIcicleIndex(int icicleIndex) {
        this.entityData.set(DATA_ICICLE_INDEX, icicleIndex);
    }

    public int getIcicleIndex() {
        return this.entityData.get(DATA_ICICLE_INDEX);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ICICLE_INDEX, -1);
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("icicleIndex", this.getIcicleIndex());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains("icicleIndex")){
            this.setIcicleIndex(compoundNBT.getInt("icicleIndex"));
        }
    }
}
