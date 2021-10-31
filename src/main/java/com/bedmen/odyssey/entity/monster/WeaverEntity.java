package com.bedmen.odyssey.entity.monster;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WeaverEntity extends MonsterEntity {
    private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(WeaverEntity.class, DataSerializers.BYTE);

    public WeaverEntity(EntityType<? extends WeaverEntity> p_i48550_1_, World p_i48550_2_) {
        super(p_i48550_1_, p_i48550_2_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new WeaverEntity.AttackGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new WeaverEntity.TargetGoal<>(this, PlayerEntity.class));
        this.targetSelector.addGoal(3, new WeaverEntity.TargetGoal<>(this, IronGolemEntity.class));
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getPassengersRidingOffset() {
        return (double)(this.getBbHeight() * 0.5F);
    }

    /**
     * Returns new PathNavigateGround instance
     */
    protected PathNavigator createNavigation(World pLevel) {
        return new ClimberPathNavigator(this, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }

    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.MOVEMENT_SPEED, (double)0.3F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SPIDER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    /**
     * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or
     * for AI reasons)
     */
    public boolean onClimbable() {
        return this.isClimbing();
    }

    public void makeStuckInBlock(BlockState pState, Vector3d pMotionMultiplier) {
        if (!pState.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }

    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.ARTHROPOD;
    }

    public boolean canBeAffected(EffectInstance pPotioneffect) {
        if (pPotioneffect.getEffect() == Effects.POISON) {
            net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, pPotioneffect);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW;
        }
        return super.canBeAffected(pPotioneffect);
    }

    /**
     * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns false. The WatchableObject is updated using
     * setBesideClimableBlock.
     */
    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
     * false.
     */
    public void setClimbing(boolean pClimbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pClimbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (pLevel.getRandom().nextInt(100) == 0) {
            CreeperEntity creeper = EntityType.CREEPER.create(this.level);
            creeper.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
            creeper.finalizeSpawn(pLevel, pDifficulty, pReason, (ILivingEntityData)null, (CompoundNBT)null);
            creeper.startRiding(this);
        }

        return pSpawnData;
    }

    protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
        return 0.65F;
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(WeaverEntity p_i46676_1_) {
            super(p_i46676_1_, 1.0D, true);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            if (this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget((LivingEntity)null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            double d0 = this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(Hand.MAIN_HAND);
                this.mob.doHurtTarget(pEnemy);
                if(this.mob.getRandom().nextFloat() < 0.1f){
                    BlockPos blockPos = new BlockPos(pEnemy.getPosition(1f));
                    if(this.mob.level.getBlockState(blockPos).getBlock() == Blocks.AIR){
                        this.mob.level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
                    }
                }
            }

        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double)(4.0F + pAttackTarget.getBbWidth());
        }
    }

    static class TargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public TargetGoal(WeaverEntity p_i45818_1_, Class<T> p_i45818_2_) {
            super(p_i45818_1_, p_i45818_2_, true);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            float f = this.mob.getBrightness();
            return !(f >= 0.5F) && super.canUse();
        }
    }
}
