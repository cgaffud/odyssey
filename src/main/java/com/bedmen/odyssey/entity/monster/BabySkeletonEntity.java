package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.entity.projectile.BoomerangEntity;
import com.bedmen.odyssey.items.equipment.BoomerangItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Optional;

public class BabySkeletonEntity extends AbstractSkeletonEntity {
    private int noBoomerangTick;
    private Optional<Hand> boomerangHand = Optional.empty();
    public BabySkeletonEntity(EntityType<? extends BabySkeletonEntity> p_i50194_1_, World p_i50194_2_) {
        super(p_i50194_1_, p_i50194_2_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new RangedBoomerangAttackGoal(this, 1.0D, 40, 15.0F));
    }

    public void tick(){
        super.tick();
        this.boomerangHand = this.hasBoomerang();
        if(this.boomerangHand.isPresent()){
            this.noBoomerangTick = 0;
        } else {
            if(this.noBoomerangTick < 200){
                this.noBoomerangTick++;
            } else {
                this.setItemInHand(Hand.MAIN_HAND, new ItemStack(ItemRegistry.BONE_BOOMERANG.get()));
            }
        }
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

    protected int getExperienceReward(PlayerEntity pPlayer) {
        this.xpReward = (int)((float)this.xpReward * 2.5F);
        return super.getExperienceReward(pPlayer);
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
    }

    public Optional<Hand> hasBoomerang(){
        if(this.getMainHandItem().getItem() instanceof BoomerangItem){
            return Optional.of(Hand.MAIN_HAND);
        }
        if(this.getOffhandItem().getItem() instanceof BoomerangItem){
            return Optional.of(Hand.OFF_HAND);
        }
        return Optional.empty();
    }

    public void performRangedAttack(LivingEntity target, float p_82196_2_) {
        Optional<Hand> hand = this.hasBoomerang();
        ItemStack itemstack = hand.map(value -> new ItemStack(this.getItemInHand(value).getItem())).orElseGet(() -> new ItemStack(ItemRegistry.BONE_BOOMERANG.get()));
        BoomerangEntity boomerangEntity = new BoomerangEntity(this.level, this, itemstack);
        double d0 = target.getX() - this.getX();
        double d1 = target.getEyeHeight() - this.getEyeHeight() + target.getY() - this.getY();
        double d2 = target.getZ() - this.getZ();
        boomerangEntity.shoot(d0, d1, d2, ((BoomerangItem)itemstack.getItem()).shootSpeed(), (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.TRIDENT_THROW, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(boomerangEntity);
        hand.ifPresent(value -> this.setItemInHand(value, ItemStack.EMPTY));
        this.reassessWeaponGoal();
    }

    public class RangedBoomerangAttackGoal extends Goal {
        private final BabySkeletonEntity mob;
        private final double speedModifier;
        private int attackIntervalMin;
        private final float attackRadiusSqr;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;

        public RangedBoomerangAttackGoal(BabySkeletonEntity p_i47515_1_, double p_i47515_2_, int p_i47515_4_, float p_i47515_5_) {
            this.mob = p_i47515_1_;
            this.speedModifier = p_i47515_2_;
            this.attackIntervalMin = p_i47515_4_;
            this.attackRadiusSqr = p_i47515_5_ * p_i47515_5_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone());
        }

        public void start() {
            super.start();
            this.mob.setAggressive(true);
        }

        public void stop() {
            super.stop();
            this.mob.setAggressive(false);
            this.seeTime = 0;
            this.attackTime = -1;
            this.mob.stopUsingItem();
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean flag = this.mob.getSensing().canSee(livingentity);
                boolean flag1 = this.seeTime > 0;
                if (flag != flag1) {
                    this.seeTime = 0;
                }

                if (flag) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.mob.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 20) {
                    if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if (d0 > (double)(this.attackRadiusSqr * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if (d0 < (double)(this.attackRadiusSqr * 0.25F)) {
                        this.strafingBackwards = true;
                    }

                    this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.mob.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                Optional<Hand> hand = mob.hasBoomerang();

                if(hand.isPresent()){
                    if (this.mob.isUsingItem()) {
                        if (!flag && this.seeTime < -60) {
                            this.mob.stopUsingItem();
                        } else if (flag) {
                            int i = this.mob.getTicksUsingItem();
                            if (i >= 20) {
                                this.mob.stopUsingItem();
                                this.mob.performRangedAttack(livingentity, 0.0f);
                                this.attackTime = this.attackIntervalMin;
                            }
                        }
                    } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                        this.mob.startUsingItem(hand.get());
                    }
                }
            }
        }
    }
}
