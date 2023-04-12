package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.entity.boss.coven.EnderWitch;
import com.bedmen.odyssey.items.aspect_items.AspectArrowItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import com.bedmen.odyssey.combat.WeaponUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

public class Wraith extends Monster implements NeutralMob, RangedAttackMob {
    public final WraithBowAttackGoal<Wraith> bowGoal = new WraithBowAttackGoal<>(this, 1.0D, 20, 10.0F);
    public final WraithMeleeAttackGoal meleeGoal = new WraithMeleeAttackGoal(this);

    private int remainingPersistentAngerTime;
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(30, 49);

    public Wraith(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new WraithMoveControl(this);
        this.reassessWeaponGoal();
        this.setNoGravity(true);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60.0D).add(Attributes.MOVEMENT_SPEED, 1.2D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if ((random.nextDouble() < 0.001)) {
            Player player = this.level.getNearestPlayer(this.getX(), this.getY(), this.getZ(), 20, true);
            if (player != null) {
                setPersistentAngerTarget(player.getUUID());
                startPersistentAngerTimer();
            }
        }
    }

    public void reassessWeaponGoal() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem));
            if (itemstack.getItem() instanceof BowItem) {
                int i = 20;
                if (this.level.getDifficulty() != Difficulty.HARD)
                    i = 40;

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(2, this.bowGoal);
            } else {
                this.goalSelector.addGoal(2, this.meleeGoal);
            }

        }
    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_32146_, DifficultyInstance p_32147_, MobSpawnType p_32148_, @javax.annotation.Nullable SpawnGroupData p_32149_, @javax.annotation.Nullable CompoundTag p_32150_) {
        p_32149_ = super.finalizeSpawn(p_32146_, p_32147_, p_32148_, p_32149_, p_32150_);
        this.populateDefaultEquipmentSlots(p_32147_);
        this.populateDefaultEquipmentEnchantments(p_32147_);
        this.reassessWeaponGoal();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * p_32147_.getSpecialMultiplier());
        return p_32149_;
    }

    public void readAdditionalSaveData(CompoundTag p_32152_) {
        super.readAdditionalSaveData(p_32152_);
        this.reassessWeaponGoal();
    }

    public void setItemSlot(EquipmentSlot p_32138_, ItemStack p_32139_) {
        super.setItemSlot(p_32138_, p_32139_);
        if (!this.level.isClientSide) {
            this.reassessWeaponGoal();
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.AMBIENT_CAVE;
    }

    protected SoundEvent getHurtSound(DamageSource DamageSource) {
        return SoundEventRegistry.WRAITH_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return SoundEventRegistry.WRAITH_DEATH.get();
    }

    @Override
    public void playAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null && (this.getRandom().nextBoolean() && this.getRandom().nextBoolean())) {
            this.playSound(soundevent, this.getSoundVolume(), this.getVoicePitch());
        }

    }

    protected AbstractArrow getOdysseyArrow(ItemStack ammo, float bowDamageMultiplier) {
        AspectArrowItem aspectArrowItem = (AspectArrowItem) ItemRegistry.ETHEREAL_ARROW.get();
        AbstractArrow abstractarrow = aspectArrowItem.createArrow(this.level, ammo, this);
        abstractarrow.setEnchantmentEffectsFromEntity(this, bowDamageMultiplier);
        return abstractarrow;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float bowDamageMultiplier) {
        ItemStack bow = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem));
        ItemStack ammoStack = this.getProjectile(bow);
        AbstractArrow abstractarrow = this.getOdysseyArrow(ammoStack, bowDamageMultiplier);
        Item item = bow.getItem();
        if (item instanceof BowItem  bowItem)
            abstractarrow = bowItem.customArrow(abstractarrow);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrow.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float velocity = WeaponUtil.getMaxArrowVelocity(bow, false);
        float accuracyMultiplier = 1.0f + AspectUtil.getFloatAspectStrength(bow, Aspects.ACCURACY);
        abstractarrow.shoot(d0, d1 + d3 * (double)(0.32f / velocity), d2, velocity, (float)(14 - this.level.getDifficulty().getId() * 4) * accuracyMultiplier);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrow);
    }

    protected AbstractArrow getArrow(ItemStack p_32156_, float p_32157_) {
        return ProjectileUtil.getMobArrow(this, p_32156_, p_32157_);
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_32144_) {
        return p_32144_ == ItemRegistry.BOW.get();
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(difficultyInstance);
        Item item = random.nextBoolean() ? ItemRegistry.VOID_BOW.get() : ItemRegistry.VOID_SWORD.get();
        this.setItemSlot(EquipmentSlot.MAINHAND, item.getDefaultInstance());
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = remainingPersistentAngerTime;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
    }

    public static boolean spawnPredicate(EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pReason, BlockPos pPos, Random pRandom) {
        return Monster.checkMonsterSpawnRules(pType, pLevel, pReason, pPos, pRandom) && pPos.getY() <= -16;
    }

    // Todo: Small possible bug? You can semi-tightly wind around the Wraith and something here delays its turning rate so it can't gety ou
    public class WraithMeleeAttackGoal extends Goal {

        private final Wraith wraith;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;

        private final double SPEED = 0.3D;
        private final double MELEE_SPEED_MULT = 2.5F;


        public WraithMeleeAttackGoal(Wraith wraith) {
            this.wraith = wraith;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = wraith.getTarget();
            return (livingentity != null) && (livingentity.isAlive());
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = wraith.getTarget();
            if ((livingentity != null) && (livingentity.isAlive())) {
                return !(livingentity instanceof Player) || (!livingentity.isSpectator() && !((Player)livingentity).isCreative());
            }
            return true;
        }

        public void start() {
            wraith.setAggressive(true);
            LivingEntity livingentity = wraith.getTarget();
            if (livingentity != null) {
                Vec3 vec3 = livingentity.position();
                wraith.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, this.SPEED);
            }
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        public void stop(){
            LivingEntity livingentity = wraith.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                wraith.setTarget(null);
            }
            wraith.setAggressive(false);
            // Kill movement when stopping
            wraith.getMoveControl().setWantedPosition(wraith.getX(), wraith.getY(), wraith.getZ(), this.SPEED);
        }

        public void tick() {
            LivingEntity livingentity = wraith.getTarget();
            if (livingentity != null) {
                wraith.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                double d0 = wraith.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if (this.ticksUntilNextPathRecalculation <= 0 && (wraith.getRandom().nextFloat() < 0.05F)) {
                    this.ticksUntilNextPathRecalculation = 4 + wraith.getRandom().nextInt(7);

                    if (d0 > 1024.0D) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d0 > 256.0D) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }

                    Vec3 vec3 = livingentity.position();

                    wraith.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, this.SPEED);

                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);

                if ((this.ticksUntilNextAttack <= 0) && (d0 < this.getAttackReachSqr(livingentity))) {
                    this.ticksUntilNextAttack = this.adjustedTickDelay(20);
                    wraith.swing(InteractionHand.MAIN_HAND);
                    wraith.doHurtTarget(livingentity);
                }
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        protected double getAttackReachSqr(LivingEntity p_25556_) {
            return (wraith.getBbWidth() * this.MELEE_SPEED_MULT * wraith.getBbWidth() * this.MELEE_SPEED_MULT + p_25556_.getBbWidth());
        }
    }

    public class WraithBowAttackGoal<T extends net.minecraft.world.entity.Mob & RangedAttackMob> extends Goal {
        private final T mob;
        private final double speedModifier;
        private int attackIntervalMin;
        private final float attackRadiusSqr;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;
        private final double SPEED = 0.3D;

        public <M extends Monster & RangedAttackMob> WraithBowAttackGoal(M p_25792_, double p_25793_, int p_25794_, float p_25795_) {
            this((T) p_25792_, p_25793_, p_25794_, p_25795_);
        }

        public WraithBowAttackGoal(T Mob, double speedModifier, int attackIntervalMin, float attackRadius) {
            this.mob = Mob;
            this.speedModifier = speedModifier;
            this.attackIntervalMin = attackIntervalMin;
            this.attackRadiusSqr = attackRadius * attackRadius;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public void setMinAttackInterval(int p_25798_) {
            this.attackIntervalMin = p_25798_;
        }

        public boolean canUse() {
            return this.mob.getTarget() == null ? false : this.isHoldingBow();
        }

        protected boolean isHoldingBow() {
            return this.mob.isHolding(is -> is.getItem() instanceof BowItem);
        }

        public boolean canContinueToUse() {
            return this.canUse() && this.isHoldingBow();
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

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                System.out.println("[Tick] Nonnull");
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
                boolean flag1 = this.seeTime > 0;
                if (flag != flag1)
                    this.seeTime = 0;


                if (flag) ++this.seeTime;
                else --this.seeTime;

                if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY(), mob.getZ(), this.SPEED);
                    ++this.strafingTime;
                } else {
                    Vec3 vec3 = livingentity.position();
                    this.mob.getMoveControl().setWantedPosition(vec3.x(), vec3.y(), vec3.z(), this.SPEED);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 20) {
                    if ((double) this.mob.getRandom().nextFloat() < 0.3D)
                        this.strafingClockwise = !this.strafingClockwise;
                    if ((double) this.mob.getRandom().nextFloat() < 0.3D)
                        this.strafingBackwards = !this.strafingBackwards;
                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if (d0 > (double) (this.attackRadiusSqr * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if (d0 < (double) (this.attackRadiusSqr * 0.25F)) {
                        this.strafingBackwards = true;
                    }
                    this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.mob.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if (this.mob.isUsingItem()) {
                    if (!flag && this.seeTime < -60) {
                        this.mob.stopUsingItem();
                    } else if (flag) {
                        int i = this.mob.getTicksUsingItem();
                        if (i >= 20) {
                            this.mob.stopUsingItem();
                            this.mob.performRangedAttack(livingentity, BowItem.getPowerForTime(i));
                            this.attackTime = this.attackIntervalMin;
                        }
                    }
                } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                    this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof BowItem));
                }
            }
        }
    }



    public class WraithMoveControl extends MoveControl {

        private final double MIN_CHASE_VELOCITY = 0.2D;
        private final double MAX_VELOCITY = 0.65D;

        public WraithMoveControl(Wraith p_34062_) {
            super(p_34062_);
        }

        public void tick() {
            if (this.operation == Operation.WAIT){
                Wraith.this.setNoGravity(true);
            } else if (this.operation == Operation.STRAFE) {
                float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float f1 = (float) this.speedModifier * f;

                this.mob.setSpeed(f1);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = MoveControl.Operation.WAIT;
            } else if (this.operation == Operation.MOVE_TO) {
                Vec3 rVector = new Vec3(this.wantedX - Wraith.this.getX(), this.wantedY - Wraith.this.getY(), this.wantedZ - Wraith.this.getZ());
                double r = rVector.length();
                if (r < Wraith.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    Wraith.this.setDeltaMovement(Wraith.this.getDeltaMovement().scale(0.25D));
                } else {
                    // 0.05(Speed Modifier) is the constant acceleration
                    Vec3 vVector;
                    if ((r >= Wraith.this.getBoundingBox().getSize() * 2) && (Wraith.this.getDeltaMovement().length() < this.MIN_CHASE_VELOCITY)) {
                        // Divide by 80 so that Wraith can get to target in 4 seconds
                        double vmin = r / 80;
                        vmin = (vmin > this.MIN_CHASE_VELOCITY) ? vmin : this.MIN_CHASE_VELOCITY;
                        vVector = rVector.scale(vmin / r);
                    } else {
                        vVector = Wraith.this.getDeltaMovement().add(rVector.scale(this.speedModifier * 0.05D / r));
                    }

                    // System.out.printf("Dist: %f, Bounding Box Size * 2: %f, Velocity: %f", r, Wraith.this.getBoundingBox().getSize()*2, vVector.length());

                    Wraith.this.setDeltaMovement((vVector.length() > this.MAX_VELOCITY) ? rVector.scale(this.MAX_VELOCITY / r) : vVector);

                    if (Wraith.this.getTarget() == null) {
                        Vec3 vec31 = Wraith.this.getDeltaMovement();
                        Wraith.this.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * (180F / (float) Math.PI));
                        Wraith.this.yBodyRot = Wraith.this.getYRot();
                    } else {
                        double d2 = Wraith.this.getTarget().getX() - Wraith.this.getX();
                        double d1 = Wraith.this.getTarget().getZ() - Wraith.this.getZ();
                        Wraith.this.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
                        Wraith.this.yBodyRot = Wraith.this.getYRot();
                    }
                }
            }
        }
    }

}
