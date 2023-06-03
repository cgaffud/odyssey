package com.bedmen.odyssey.entity.monster;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class EncasedSkeleton extends OdysseySkeleton {
    protected static final EntityDataAccessor<Float> DATA_ARMOR_HEALTH = SynchedEntityData.defineId(EncasedSkeleton.class, EntityDataSerializers.FLOAT);
    private static final float BASE_ARMOR_HEALTH = 5.0f;

    public EncasedSkeleton(EntityType<? extends OdysseySkeleton> p_33570_, Level p_33571_) {
        super(p_33570_, p_33571_);
        this.setArmorHealth(this.BASE_ARMOR_HEALTH + 3*(level.getRandom().nextFloat()-0.5f));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ARMOR_HEALTH, 0.0f);
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putFloat("ArmorHealth", this.getArmorHealth());
    }

    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains("ArmorHealth")){
            this.setArmorHealth(compoundNBT.getFloat("ArmorHealth"));
        }
    }

    public void setArmorHealth(float f) {
        this.entityData.set(DATA_ARMOR_HEALTH, f);
    }

    public float getArmorHealth() {
        return this.entityData.get(DATA_ARMOR_HEALTH);
    }


    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    protected boolean convertsInWater() {
        return false;
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        Entity entity = damageSource.getEntity();
        if(damageSource.isExplosion()){
            return this.hurtWithArmorConsidered(damageSource, amount);
        }
        else if (damageSource == DamageSource.OUT_OF_WORLD){
            this.hurtWithoutArmor(damageSource, amount);
        }
        else if (entity instanceof LivingEntity livingEntity){
            ItemStack itemStack = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
            Item item = itemStack.getItem();
            if(this.getArmorHealth() > 0) {
                if (item instanceof PickaxeItem && itemStack.isCorrectToolForDrops(Blocks.DRIPSTONE_BLOCK.defaultBlockState())) {
                    float scaledAmount = amount * 0.5f + 1f;
                    itemStack.hurtAndBreak(1, livingEntity, (livingEntity1) -> {
                        livingEntity1.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                    });
                    return this.hurtWithArmorConsidered(damageSource, scaledAmount);
                } else {
                    return false;
                }
            } else {
                return this.hurtWithoutArmor(damageSource, amount);
            }
        }
        return false;
    }

    protected boolean hurtWithArmorConsidered(DamageSource damageSource, float amount){
        float armorHealth = this.getArmorHealth();
        if(armorHealth > 0.0f){
            float newArmorHealth = armorHealth - amount;
            if(!this.level.isClientSide){
                this.setArmorHealth(newArmorHealth);
                if (newArmorHealth <= 0f) {
                    for(int i = 0; i < 8; ++i) {
                        ((ServerLevel)this.level).sendParticles(ParticleTypes.CRIT, this.getX() + this.random.nextFloat()-0.5f, this.getY() + this.random.nextFloat()*2, this.getZ() +this.random.nextFloat()-0.5f, 2, 0.2D, 0.2D, 0.2D, 0.0D);
                    }
                }
            }
            if (newArmorHealth != armorHealth && !this.isSilent()) {
                if(newArmorHealth > 0f){
                    this.playSound(SoundEvents.STONE_BREAK, 1.0F, 1.0F);
                } else {
                    this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
                }
            }
            return false;
        } else {
            return this.hurtWithoutArmor(damageSource, amount);
        }
    }

    protected boolean hurtWithoutArmor(DamageSource damageSource, float amount){
        return super.hurt(damageSource, amount);
    }

}
