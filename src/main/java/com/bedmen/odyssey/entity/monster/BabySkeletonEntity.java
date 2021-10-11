package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.entity.projectile.BoomerangEntity;
import com.bedmen.odyssey.items.equipment.BoomerangItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class BabySkeletonEntity extends AbstractSkeletonEntity {
    private final BoomerangAttackGoal boomerangGoal = new BoomerangAttackGoal(this, 1.0D, 40, 15.0F);
    public BabySkeletonEntity(EntityType<? extends BabySkeletonEntity> p_i50194_1_, World p_i50194_2_) {
        super(p_i50194_1_, p_i50194_2_);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    public boolean isBaby() {
        return true;
    }

    public double getMyRidingOffset() {
        return this.isBaby() ? 0.0D : -0.45D;
    }

    protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
        return 0.93F;
    }

    protected void dropCustomDeathLoot(DamageSource p_213333_1_, int p_213333_2_, boolean p_213333_3_) {
        super.dropCustomDeathLoot(p_213333_1_, p_213333_2_, p_213333_3_);
        Entity entity = p_213333_1_.getEntity();
        if (entity instanceof CreeperEntity) {
            CreeperEntity creeperentity = (CreeperEntity)entity;
            if (creeperentity.canDropMobsSkull()) {
                creeperentity.increaseDroppedSkulls();
                this.spawnAtLocation(Items.SKELETON_SKULL);
            }
        }

    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
        super.populateDefaultEquipmentSlots(p_180481_1_);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ItemRegistry.BONE_BOOMERANG.get()));
    }

    public void reassessWeaponGoal() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.boomerangGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, ItemRegistry.BONE_BOOMERANG.get()));
            if (itemstack.getItem() instanceof BoomerangItem) {
                this.goalSelector.addGoal(4, this.boomerangGoal);
            }
        }
    }

    public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {
        BoomerangEntity boomerangEntity = new BoomerangEntity(this.level, this, new ItemStack(ItemRegistry.BONE_BOOMERANG.get()));
        double d0 = p_82196_1_.getX() - this.getX();
        double d1 = p_82196_1_.getY(0.3333333333333333D) - boomerangEntity.getY();
        double d2 = p_82196_1_.getZ() - this.getZ();
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        boomerangEntity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.TRIDENT_THROW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(boomerangEntity);
        this.setItemInHand(ProjectileHelper.getWeaponHoldingHand(this, ItemRegistry.BONE_BOOMERANG.get()), ItemStack.EMPTY);
        this.reassessWeaponGoal();
    }

    static class BoomerangAttackGoal extends RangedAttackGoal {
        private final BabySkeletonEntity babySkeletonEntity;

        public BoomerangAttackGoal(IRangedAttackMob p_i48907_1_, double p_i48907_2_, int p_i48907_4_, float p_i48907_5_) {
            super(p_i48907_1_, p_i48907_2_, p_i48907_4_, p_i48907_5_);
            this.babySkeletonEntity = (BabySkeletonEntity)p_i48907_1_;
        }

        public boolean canUse() {
            return super.canUse() && this.babySkeletonEntity.getMainHandItem().getItem() instanceof BoomerangItem;
        }

        public void start() {
            super.start();
            this.babySkeletonEntity.setAggressive(true);
            this.babySkeletonEntity.startUsingItem(Hand.MAIN_HAND);
        }

        public void stop() {
            super.stop();
            this.babySkeletonEntity.stopUsingItem();
            this.babySkeletonEntity.setAggressive(false);
        }
    }
}
