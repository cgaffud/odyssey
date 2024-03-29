package com.bedmen.odyssey.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EncasedSkeleton extends OdysseySkeleton implements Encased {
    protected static final EntityDataAccessor<Float> DATA_ARMOR_HEALTH = SynchedEntityData.defineId(EncasedSkeleton.class, EntityDataSerializers.FLOAT);
    private static final float BASE_ARMOR_HEALTH = 5.0f;

    public EncasedSkeleton(EntityType<? extends OdysseySkeleton> p_33570_, Level p_33571_) {
        super(p_33570_, p_33571_);
        this.setStoneArmorHealth(BASE_ARMOR_HEALTH + 3*(level.getRandom().nextFloat()-0.5f));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ARMOR_HEALTH, 0.0f);
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putFloat(Encased.STONE_ARMOR_HEALTH_TAG, this.getStoneArmorHealth());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains(Encased.STONE_ARMOR_HEALTH_TAG)){
            this.setStoneArmorHealth(compoundNBT.getFloat(Encased.STONE_ARMOR_HEALTH_TAG));
        }
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        return this.hurt(this, damageSource, amount);
    }

    @Override
    public boolean hurtWithoutStoneArmor(DamageSource damageSource, float amount) {
        return super.hurt(damageSource, amount);
    }

    @Override
    public float getStoneArmorHealth() {
        return this.entityData.get(DATA_ARMOR_HEALTH);
    }

    @Override
    public void setStoneArmorHealth(float newStoneArmorHealth) {
        this.entityData.set(DATA_ARMOR_HEALTH, newStoneArmorHealth);
    }
}
