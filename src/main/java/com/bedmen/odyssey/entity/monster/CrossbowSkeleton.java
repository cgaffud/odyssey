package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.items.OdysseyCrossbowItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;

public class CrossbowSkeleton extends AbstractSkeleton implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(CrossbowSkeleton.class, EntityDataSerializers.BOOLEAN);
    public final RangedCrossbowAttackGoal<CrossbowSkeleton> crossBowGoal = new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F);

    public CrossbowSkeleton(EntityType<? extends CrossbowSkeleton> p_33570_, Level p_33571_) {
        super(p_33570_, p_33571_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    public void tick() {
        super.tick();
    }

    public void addAdditionalSaveData(CompoundTag p_149836_) {
        super.addAdditionalSaveData(p_149836_);
    }

    public void readAdditionalSaveData(CompoundTag p_149833_) {
        super.readAdditionalSaveData(p_149833_);
    }

    public boolean canFreeze() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33579_) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    protected void dropCustomDeathLoot(DamageSource p_33574_, int p_33575_, boolean p_33576_) {
        super.dropCustomDeathLoot(p_33574_, p_33575_, p_33576_);
        Entity entity = p_33574_.getEntity();
        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper)entity;
            if (creeper.canDropMobsSkull()) {
                creeper.increaseDroppedSkulls();
                this.spawnAtLocation(Items.SKELETON_SKULL);
            }
        }

    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(difficultyInstance);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.BONE_SLUG_BOW.get()));
    }

    public void reassessWeaponGoal() {
        if (!this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            this.goalSelector.removeGoal(this.crossBowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.CrossbowItem));
            if (itemstack.getItem() instanceof CrossbowItem) {
                this.goalSelector.addGoal(4, this.crossBowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }
        }
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeaponItem) {
        return projectileWeaponItem instanceof CrossbowItem;
    }

    public void performRangedAttack(LivingEntity livingEntity, float power) {
        InteractionHand interactionhand = ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem);
        ItemStack itemstack = this.getItemInHand(interactionhand);
        if (this.isHolding(is -> is.getItem() instanceof OdysseyCrossbowItem)) {
            OdysseyCrossbowItem.performShooting(this.level, this, interactionhand, itemstack, 1.6f, (float)(14 - this.level.getDifficulty().getId() * 4));
        }
        this.onCrossbowAttackPerformed();
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public HumanoidModel.ArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem)) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        } else {
            return HumanoidModel.ArmPose.EMPTY;
        }
    }

    @Override
    public void setChargingCrossbow(boolean b) {
        this.entityData.set(IS_CHARGING_CROSSBOW, b);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity livingEntity, ItemStack itemStack, Projectile projectile, float power) {
        this.shootCrossbowProjectile(this, livingEntity, projectile, power, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }
}