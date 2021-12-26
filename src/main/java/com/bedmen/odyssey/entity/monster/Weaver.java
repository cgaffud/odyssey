package com.bedmen.odyssey.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class Weaver extends Monster {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Weaver.class, EntityDataSerializers.BYTE);
    private static final UUID SPEED_MODIFIER_QUEEN_UUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier SPEED_MODIFIER_QUEEN = new AttributeModifier(SPEED_MODIFIER_QUEEN_UUID, "Queen speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final UUID HEALTH_MODIFIER_QUEEN_UUID = UUID.fromString("c03c6f3c-95ff-43b0-88e1-01d6a780c61b");
    private static final AttributeModifier HEALTH_MODIFIER_QUEEN = new AttributeModifier(HEALTH_MODIFIER_QUEEN_UUID, "Queen health boost", 4.0D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final UUID DAMAGE_MODIFIER_QUEEN_UUID = UUID.fromString("aae4f1af-b9c8-4221-ba0e-f4794f87a651");
    private static final AttributeModifier DAMAGE_MODIFIER_QUEEN = new AttributeModifier(DAMAGE_MODIFIER_QUEEN_UUID, "Queen damage boost", 1.0D, AttributeModifier.Operation.MULTIPLY_BASE);

    public Weaver(EntityType<? extends Weaver> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new AttackGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new TargetGoal<>(this, Player.class));
        this.targetSelector.addGoal(3, new TargetGoal<>(this, IronGolem.class));
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
    protected PathNavigation createNavigation(Level pLevel) {
        return new WallClimberNavigation(this, pLevel);
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

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.MOVEMENT_SPEED, (double)0.3F);
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

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public void makeStuckInBlock(BlockState pState, Vec3 pMotionMultiplier) {
        if (!(pState.getBlock() instanceof WebBlock)) {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }
    }

    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean pClimbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pClimbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public boolean isQueen() {
        return (this.entityData.get(DATA_FLAGS_ID) & 2) != 0;
    }

    public void setQueen(boolean isQueen) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (!this.level.isClientSide) {
            AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            attributeinstance.removeModifier(SPEED_MODIFIER_QUEEN);
            attributeinstance.removeModifier(HEALTH_MODIFIER_QUEEN);
            attributeinstance.removeModifier(DAMAGE_MODIFIER_QUEEN);
            if (isQueen) {
                b0 = (byte)(b0 | 2);
                attributeinstance.addTransientModifier(SPEED_MODIFIER_QUEEN);
                attributeinstance.addTransientModifier(HEALTH_MODIFIER_QUEEN);
                attributeinstance.addTransientModifier(DAMAGE_MODIFIER_QUEEN);
            } else {
                b0 = (byte)(b0 & -3);
            }
            this.entityData.set(DATA_FLAGS_ID, b0);
        }
    }

    public float getScale(){
        return this.isQueen() ? 2.0f : 1.0f;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        if (DATA_FLAGS_ID.equals(entityDataAccessor)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(entityDataAccessor);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (pLevel.getRandom().nextInt(100) == 0) {
            Creeper creeper = EntityType.CREEPER.create(this.level);
            creeper.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            creeper.finalizeSpawn(pLevel, pDifficulty, pReason, (SpawnGroupData)null, (CompoundTag)null);
            creeper.startRiding(this);
        }

        return pSpawnData;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 0.65F;
    }

    public static boolean spawnPredicate(EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pReason, BlockPos pPos, Random pRandom) {
        return Monster.checkMonsterSpawnRules(pType, pLevel, pReason, pPos, pRandom) && pPos.getY() <= 56;
    }

    static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(Weaver p_i46676_1_) {
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
            if (pDistToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
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
        public TargetGoal(Weaver p_i45818_1_, Class<T> p_i45818_2_) {
            super(p_i45818_1_, p_i45818_2_, true);
        }
    }
}
