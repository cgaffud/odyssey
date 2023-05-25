package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.items.aspect_items.AspectArrowItem;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Random;

public abstract class AbstractWraith extends Monster {

    public final double MIN_CHASE_VELOCITY;
    public final double MAX_VELOCITY;
    public final double INITIAL_VELOCITY;
    public final double REACH;


    protected AbstractWraith(EntityType<? extends Monster> p_33002_, Level p_33003_, double minV, double maxV, double initV, double reach) {
        super(p_33002_, p_33003_);
        this.setNoGravity(true);
        this.moveControl = new WraithMoveControl(this);
        this.MIN_CHASE_VELOCITY = minV;
        this.MAX_VELOCITY = maxV;
        this.INITIAL_VELOCITY = initV;
        this.REACH = reach;
    }

    protected AbstractArrow getOdysseyArrow(ItemStack ammo, float bowDamageMultiplier) {
        AspectArrowItem aspectArrowItem = (AspectArrowItem) ItemRegistry.ETHEREAL_ARROW.get();
        AbstractArrow abstractarrow = aspectArrowItem.createArrow(this.level, ammo, this);
        abstractarrow.setEnchantmentEffectsFromEntity(this, bowDamageMultiplier);
        return abstractarrow;
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
    public static boolean spawnPredicate(EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pReason, BlockPos pPos, Random pRandom) {
        return Monster.checkMonsterSpawnRules(pType, pLevel, pReason, pPos, pRandom) && pPos.getY() <= -16;
    }

    public void moveControlStop() {
        this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), this.INITIAL_VELOCITY);
    }

    public class WraithMeleeAttackGoal extends Goal {

        protected final AbstractWraith wraith;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;


        public WraithMeleeAttackGoal(AbstractWraith wraith) {
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
            return false;
        }

        public void start() {
            wraith.setAggressive(true);
            LivingEntity livingentity = wraith.getTarget();
            if (livingentity != null) {
                Vec3 vec3 = livingentity.position();
                wraith.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, wraith.INITIAL_VELOCITY);
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
            wraith.moveControlStop();
        }

        public void tick() {
            LivingEntity livingentity = wraith.getTarget();
            if (livingentity != null) {
                wraith.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                double d0 = wraith.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                Vec3 rVector = new Vec3(wraith.getMoveControl().getWantedX() - wraith.getX(), wraith.getMoveControl().getWantedY()  - AbstractWraith.this.getY(), wraith.getMoveControl().getWantedZ() - AbstractWraith.this.getZ());
                double r = rVector.length();
                if ((this.ticksUntilNextPathRecalculation <= 0 && (wraith.getRandom().nextFloat() < 0.05F)) || (r < wraith.getBoundingBox().getSize())) {
                    this.ticksUntilNextPathRecalculation = 4 + wraith.getRandom().nextInt(7);

                    if (d0 > 1024.0D) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d0 > 256.0D) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }

                    Vec3 vec3 = livingentity.position();

                    wraith.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, wraith.INITIAL_VELOCITY);

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
            return (wraith.getBbWidth() * wraith.REACH * wraith.getBbWidth() * wraith.REACH + p_25556_.getBbWidth());
        }
    }

    public class WraithBowAttackGoal<T extends net.minecraft.world.entity.Mob & RangedAttackMob> extends Goal {
        protected final T mob;
        private int attackIntervalMin;
        protected final float attackRadiusSqr;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;

        public <M extends Monster & RangedAttackMob> WraithBowAttackGoal(M p_25792_, int p_25794_, float p_25795_) {
            this((T) p_25792_, p_25794_, p_25795_);
        }

        public WraithBowAttackGoal(T Mob, int attackIntervalMin, float attackRadius) {
            this.mob = Mob;
            this.attackIntervalMin = attackIntervalMin;
            this.attackRadiusSqr = attackRadius * attackRadius;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return this.mob.getTarget() != null && this.isHoldingBow();
        }

        protected boolean isHoldingBow() {
            return this.mob.isHolding(is -> is.getItem() instanceof BowItem);
        }

        public boolean canContinueToUse() {
            return this.canUse() && this.isHoldingBow();
        }

        public void setMinAttackInterval(int p_25798_) {
            this.attackIntervalMin = p_25798_;
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
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
                boolean flag1 = this.seeTime > 0;
                if (flag != flag1)
                    this.seeTime = 0;


                if (flag) ++this.seeTime;
                else --this.seeTime;

                if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY(), mob.getZ(), AbstractWraith.this.INITIAL_VELOCITY);
                    ++this.strafingTime;
                } else {
                    Vec3 vec3 = livingentity.position();
                    this.mob.getMoveControl().setWantedPosition(vec3.x(), vec3.y(), vec3.z(), AbstractWraith.this.INITIAL_VELOCITY);
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

        public WraithMoveControl(AbstractWraith p_34062_) {
            super(p_34062_);
        }

        public void tick() {
            if (this.operation == Operation.WAIT){
                AbstractWraith.this.setNoGravity(true);
            } else if (this.operation == Operation.STRAFE) {
                float f = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float f1 = (float) this.speedModifier * f;

                this.mob.setSpeed(f1);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = MoveControl.Operation.WAIT;
            } else if (this.operation == Operation.MOVE_TO) {
                Vec3 rVector = new Vec3(this.wantedX - AbstractWraith.this.getX(), this.wantedY - AbstractWraith.this.getY(), this.wantedZ - AbstractWraith.this.getZ());
                double r = rVector.length();
                if (r < AbstractWraith.this.getBoundingBox().getSize()) {
                    AbstractWraith.this.setDeltaMovement(AbstractWraith.this.getDeltaMovement().scale(0.7D));
                } else {
                    // 0.05(Speed Modifier) is the constant acceleration
                    Vec3 vVector;
                    if ((r >= AbstractWraith.this.getBoundingBox().getSize() * 2) && (AbstractWraith.this.getDeltaMovement().length() < AbstractWraith.this.MIN_CHASE_VELOCITY)) {
                        // Divide by 80 so that Wraith can get to target in 4 seconds
                        double vmin = r / 80;
                        vmin = (vmin > AbstractWraith.this.MIN_CHASE_VELOCITY) ? vmin : AbstractWraith.this.MIN_CHASE_VELOCITY;
                        vVector = rVector.scale(vmin / r);
                    } else {
                        vVector = AbstractWraith.this.getDeltaMovement().add(rVector.scale(this.speedModifier * 0.05D / r));
                    }

                    // System.out.printf("Dist: %f, Bounding Box Size * 2: %f, Velocity: %f", r, AbstractWraith.this.getBoundingBox().getSize()*2, vVector.length());

                    AbstractWraith.this.setDeltaMovement((vVector.length() > AbstractWraith.this.MAX_VELOCITY) ? rVector.scale(AbstractWraith.this.MAX_VELOCITY / r) : vVector);

                    if (AbstractWraith.this.getTarget() == null) {
                        Vec3 vec31 = AbstractWraith.this.getDeltaMovement();
                        AbstractWraith.this.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * (180F / (float) Math.PI));
                        AbstractWraith.this.yBodyRot = AbstractWraith.this.getYRot();
                    } else {
                        double d2 = AbstractWraith.this.getTarget().getX() - AbstractWraith.this.getX();
                        double d1 = AbstractWraith.this.getTarget().getZ() - AbstractWraith.this.getZ();
                        AbstractWraith.this.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
                        AbstractWraith.this.yBodyRot = AbstractWraith.this.getYRot();
                    }
                }
            }
        }
    }

}
