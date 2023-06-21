package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.world.WorldGenUtil;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
    private static final AttributeModifier HEALTH_MODIFIER_QUEEN = new AttributeModifier(HEALTH_MODIFIER_QUEEN_UUID, "Queen health boost", 9.0D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final UUID DAMAGE_MODIFIER_QUEEN_UUID = UUID.fromString("aae4f1af-b9c8-4221-ba0e-f4794f87a651");
    private static final AttributeModifier DAMAGE_MODIFIER_QUEEN = new AttributeModifier(DAMAGE_MODIFIER_QUEEN_UUID, "Queen damage boost", 1.0D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final Multimap<Attribute, AttributeModifier> QUEEN_MODIFIER_MAP = Util.make(() -> {
        Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create();
        map.put(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_QUEEN);
        map.put(Attributes.MAX_HEALTH, HEALTH_MODIFIER_QUEEN);
        map.put(Attributes.ATTACK_DAMAGE, DAMAGE_MODIFIER_QUEEN);
        return map;
    });
    public static final float WEB_ATTACK_CHANCE = 0.2f;

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

    public double getPassengersRidingOffset() {
        return (double)(this.getBbHeight() * 0.5F);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16D).add(Attributes.MOVEMENT_SPEED, (double)0.3F).add(Attributes.ATTACK_DAMAGE, 2D);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
        if(this.isQueen()){
            return false;
        }
        return super.causeFallDamage(distance, damageMultiplier, damageSource);
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

    public void makeStuckInBlock(BlockState pState, Vec3 pMotionMultiplier) {
        if (!(pState.getBlock() instanceof WebBlock)) {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }
    }

    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public boolean isQueen() {
        return (this.entityData.get(DATA_FLAGS_ID) & 2) != 0;
    }

    public void makeQueen() {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (!this.level.isClientSide) {
            this.getAttributes().addTransientAttributeModifiers(QUEEN_MODIFIER_MAP);
            this.setHealth((float) this.getAttribute(Attributes.MAX_HEALTH).getValue());
            b0 = (byte)(b0 | 2);
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

    public static boolean spawnPredicate(EntityType<? extends Monster> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return Monster.checkMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) && blockPos.getY() <= 56;
    }

    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(damageSource, looting, recentlyHit);
        if(this.isQueen()){
            this.spawnAtLocation(ItemRegistry.WEAVER_EGG.get());
            for(int i = 0; i < 2 + looting; i++) {
                if(this.random.nextFloat() < 0.3f) {
                    this.spawnAtLocation(ItemRegistry.WEAVER_FANG.get());
                }
            }
        }
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("IsQueen", this.isQueen());
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.getBoolean("IsQueen")){
            this.makeQueen();
        }
    }

    public static void tryPlaceCobwebOnTarget(float chance, LivingEntity target){
        if(chance > target.getRandom().nextFloat()){
            BlockPos blockPos = new BlockPos(target.getPosition(1f));
            if(WorldGenUtil.isEmpty(target.level, blockPos)){
                target.level.setBlock(blockPos, BlockRegistry.TEMPORARY_COBWEB.get().defaultBlockState(), 3);
            }
        }
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
                tryPlaceCobwebOnTarget(WEB_ATTACK_CHANCE, pEnemy);
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
