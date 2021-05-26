package com.bedmen.odyssey.entity.monster;

import java.util.Random;

import com.bedmen.odyssey.entity.MonsterWaterEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArctihornEntity extends MonsterWaterEntity {
    public float squidPitch;
    public float prevSquidPitch;
    public float squidYaw;
    public float prevSquidYaw;
    public float squidRotation;
    public float prevSquidRotation;
    public float tentacleAngle;
    public float lastTentacleAngle;
    private float randomMotionSpeed;
    private float rotationVelocity;
    private float rotateSpeed;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;

    public ArctihornEntity(EntityType<? extends ArctihornEntity> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 10;
        this.rand.setSeed((long)this.getEntityId());
        this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ArctihornEntity.MoveToTarget(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new ArctihornEntity.MoveRandomGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PolarBearEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute attributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 40.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * 0.5F;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SQUID_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SQUID_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SQUID_DEATH;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume() {
        return 0.4F;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick() {
        super.livingTick();
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;
        if ((double)this.squidRotation > (Math.PI * 2D)) {
            if (this.world.isRemote) {
                this.squidRotation = ((float)Math.PI * 2F);
            } else {
                this.squidRotation = (float)((double)this.squidRotation - (Math.PI * 2D));
                if (this.rand.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
                }

                this.world.setEntityState(this, (byte)19);
            }
        }

        if (this.isInWaterOrBubbleColumn()) {
            if (this.squidRotation < (float)Math.PI) {
                float f = this.squidRotation / (float)Math.PI;
                this.tentacleAngle = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25F;
                if ((double)f > 0.75D) {
                    this.randomMotionSpeed = 1.0F;
                    this.rotateSpeed = 1.0F;
                } else {
                    this.rotateSpeed *= 0.8F;
                }
            } else {
                this.tentacleAngle = 0.0F;
                this.randomMotionSpeed *= 0.9F;
                this.rotateSpeed *= 0.99F;
            }

            if (!this.world.isRemote) {
                this.setMotion((double)(this.randomMotionVecX * this.randomMotionSpeed), (double)(this.randomMotionVecY * this.randomMotionSpeed), (double)(this.randomMotionVecZ * this.randomMotionSpeed));
            }

            Vector3d vector3d = this.getMotion();
            float f1 = MathHelper.sqrt(horizontalMag(vector3d));
            this.renderYawOffset += (-((float)MathHelper.atan2(vector3d.x, vector3d.z)) * (180F / (float)Math.PI) - this.renderYawOffset) * 0.1F;
            this.rotationYaw = this.renderYawOffset;
            this.squidYaw = (float)((double)this.squidYaw + Math.PI * (double)this.rotateSpeed * 1.5D);
            this.squidPitch += (-((float)MathHelper.atan2((double)f1, vector3d.y)) * (180F / (float)Math.PI) - this.squidPitch) * 0.1F;
        } else {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float)Math.PI * 0.25F;
            if (!this.world.isRemote) {
                double d0 = this.getMotion().y;
                if (this.isPotionActive(Effects.LEVITATION)) {
                    d0 = 0.05D * (double)(this.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1);
                } else if (!this.hasNoGravity()) {
                    d0 -= 0.08D;
                }

                this.setMotion(0.0D, d0 * (double)0.98F, 0.0D);
            }

            this.squidPitch = (float)((double)this.squidPitch + (double)(-90.0F - this.squidPitch) * 0.02D);
        }

    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount) && this.getRevengeTarget() != null;
    }

    public void travel(Vector3d travelVector) {
        this.move(MoverType.SELF, this.getMotion());
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 19) {
            this.squidRotation = 0.0F;
        } else {
            super.handleStatusUpdate(id);
        }

    }

    public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
        this.randomMotionVecX = randomMotionVecXIn;
        this.randomMotionVecY = randomMotionVecYIn;
        this.randomMotionVecZ = randomMotionVecZIn;
    }

    public boolean hasMovementVector() {
        return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
    }

    public static boolean predicate(EntityType<ArctihornEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return pos.getY() < world.getSeaLevel();
    }

    class MoveRandomGoal extends Goal {
        private final ArctihornEntity squid;

        public MoveRandomGoal(ArctihornEntity p_i48823_2_) {
            this.squid = p_i48823_2_;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return this.squid.getAttackTarget() == null;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            int i = this.squid.getIdleTime();
            if (i > 100) {
                this.squid.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.inWater || !this.squid.hasMovementVector()) {
                float f = this.squid.getRNG().nextFloat() * ((float)Math.PI * 2F);
                float f1 = MathHelper.cos(f) * 0.2F;
                float f2 = -0.1F + this.squid.getRNG().nextFloat() * 0.2F;
                float f3 = MathHelper.sin(f) * 0.2F;
                this.squid.setMovementVector(f1, f2, f3);
            }

        }
    }

    class MoveToTarget extends Goal {
        private final ArctihornEntity squid;
        private LivingEntity target;

        public MoveToTarget(ArctihornEntity p_i48823_2_) {
            this.squid = p_i48823_2_;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            this.target = this.squid.getAttackTarget();
            return this.target != null;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            int i = this.squid.getIdleTime();
            if (i > 100) {
                this.squid.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (this.squid.getRNG().nextInt(10) == 0 || !this.squid.inWater || !this.squid.hasMovementVector()) {
                Vector3d vector3d1 = new Vector3d(this.target.getPosX() - this.squid.getPosX(), this.target.getPosY() - this.squid.getPosY(), this.target.getPosZ() - this.squid.getPosZ());
                if (vector3d1.lengthSquared() > 1.0E-7D) {
                    vector3d1 = vector3d1.normalize().scale(0.4D);
                }
                this.squid.setMovementVector((float)vector3d1.x, (float)vector3d1.y, (float)vector3d1.z);
            }

        }
    }
}