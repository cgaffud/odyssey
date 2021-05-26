package com.bedmen.odyssey.entity.monster;

import java.util.Random;
import java.util.UUID;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WerewolfEntity extends MonsterEntity {
    private float headRotationCourse;
    private float headRotationCourseOld;
    private boolean isWet;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    private static final RangedInteger PERSISTENT_ANGER_TIME = TickRangeConverter.rangeOfSeconds(20, 39);
    private UUID persistentAngerTarget;

    public WerewolfEntity(EntityType<? extends WerewolfEntity> type, World worldIn) {
        super(type, worldIn);
        this.xpReward = 5;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute attributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.35F).add(Attributes.MAX_HEALTH, 12.0D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_GROWL;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.WOLF_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume() {
        return 0.4F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void aiStep() {
        if (this.isAlive()) {
            boolean flag = this.shouldBurnInDay() && this.isSunBurnTick();
            if (flag) {
                this.setSecondsOnFire(8);
            }
        }
        super.aiStep();
        if (!this.level.isClientSide && this.isWet && !this.isShaking && !this.isPathFinding() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
            this.level.broadcastEntityEvent(this, (byte)8);
        }

    }

    protected boolean shouldBurnInDay() {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            this.headRotationCourseOld = this.headRotationCourse;
            this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;

            if (this.isInWaterRainOrBubble()) {
                this.isWet = true;
                if (this.isShaking && !this.level.isClientSide) {
                    this.level.broadcastEntityEvent(this, (byte)56);
                    this.cancelShake();
                }
            } else if ((this.isWet || this.isShaking) && this.isShaking) {
                if (this.timeWolfIsShaking == 0.0F) {
                    this.playSound(SoundEvents.WOLF_SHAKE, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }

                this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
                this.timeWolfIsShaking += 0.05F;
                if (this.prevTimeWolfIsShaking >= 2.0F) {
                    this.isWet = false;
                    this.isShaking = false;
                    this.prevTimeWolfIsShaking = 0.0F;
                    this.timeWolfIsShaking = 0.0F;
                }

                if (this.timeWolfIsShaking > 0.4F) {
                    float f = (float)this.getY();
                    int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7.0F);
                    Vector3d vector3d = this.getDeltaMovement();

                    for(int j = 0; j < i; ++j) {
                        float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                        float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                        this.level.addParticle(ParticleTypes.SPLASH, this.getX() + (double)f1, (double)(f + 0.8F), this.getZ() + (double)f2, vector3d.x, vector3d.y, vector3d.z);
                    }
                }
            }

        }
    }

    private void cancelShake() {
        this.isShaking = false;
        this.timeWolfIsShaking = 0.0F;
        this.prevTimeWolfIsShaking = 0.0F;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void die(DamageSource cause) {
        this.isWet = false;
        this.isShaking = false;
        this.prevTimeWolfIsShaking = 0.0F;
        this.timeWolfIsShaking = 0.0F;
        super.die(cause);
    }

    /**
     * True if the wolf is wet
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isWolfWet() {
        return this.isWet;
    }

    /**
     * Used when calculating the amount of shading to apply while the wolf is wet.
     */
    @OnlyIn(Dist.CLIENT)
    public float getShadingWhileWet(float partialTicks) {
        return Math.min(0.5F + MathHelper.lerp(partialTicks, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) / 2.0F * 0.5F, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public float getShakeAngle(float partialTicks, float offset) {
        float f = (MathHelper.lerp(partialTicks, this.prevTimeWolfIsShaking, this.timeWolfIsShaking) + offset) / 1.8F;
        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }

        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }

    @OnlyIn(Dist.CLIENT)
    public float getInterestedAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.headRotationCourseOld, this.headRotationCourse) * 0.15F * (float)Math.PI;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.8F;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.hurt(source, amount);
        }
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.doEnchantDamageEffects(this, entityIn);
        }

        return flag;
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
        } else if (id == 56) {
            this.cancelShake();
        } else {
            super.handleEntityEvent(id);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public float getTailRotation() {
        return 1.5393804F;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3d getLeashOffset() {
        return new Vector3d(0.0D, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    public static boolean predicate(EntityType<? extends CreatureEntity> type, IWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn){
        boolean fullmoon = worldIn.getMoonPhase() == 0;
        boolean y = pos.getY() > 63;
        return fullmoon && y;
    }
}
