package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.util.GeneralUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class WraithStalker extends AbstractWraith implements NeutralMob {

    private int remainingPersistentAngerTime;
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;
    public boolean isFrozen;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(40, 59);
    private static final float FREEZE_RANGE = 32f;


    public WraithStalker(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_, 0.2D, 0.65D, 0.3D, 2.5D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
//        this.goalSelector.addGoal(1, new WraithStalker.WraithStalkerFreezeWhenLookedAt(this));
        this.goalSelector.addGoal(1, new WraithStalkerMeleeGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 120.0D).add(Attributes.MOVEMENT_SPEED, 1.2D).add(Attributes.ATTACK_DAMAGE, 15.0D).add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    public void readAdditionalSaveData(CompoundTag p_32152_) {
        super.readAdditionalSaveData(p_32152_);
        this.setInvulnerable(false);
    }

//    public void reassessAttackGoal() {
//        if (this.level != null && !this.level.isClientSide) {
//            this.goalSelector.removeGoal(meleeGoal);
//
//            if (this.isAggressive()) {
//                System.out.println("We are agressing!\n");
//                this.isFrozen = false;
//                for (Player player : this.level.getNearbyPlayers(TargetingConditions.forCombat().range(FREEZE_RANGE), this, this.getBoundingBox().inflate(FREEZE_RANGE, FREEZE_RANGE, FREEZE_RANGE))) {
//                    if (player.hasLineOfSight(this) && !player.isInvisible()) {
//                        this.moveControlStop();
//                        this.setInvulnerable(true);
//                        this.isFrozen = true;
//                        break;
//                    }
//                }
//                if (!this.isFrozen) {
//                    this.setInvulnerable(false);
//                    this.goalSelector.addGoal(2, meleeGoal);
//                }
//            }
//        }
//    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (GeneralUtil.isHashTick(this, this.level, 20))
            System.out.printf("Aggr?: %b, Frozen?: %b\n", this.isAggressive(), this.isFrozen);
    }

    public void aiStep() {
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
        super.aiStep();
    }

    boolean isLookingAtMe(Player player) {
        Vec3 viewVec = player.getViewVector(1.0F).normalize();
        Vec3 dist = new Vec3(this.getX() - player.getX(), this.getY() + this.getBbHeight()/2 - player.getEyeY(), this.getZ() - player.getZ());
        double d0 = dist.length();
        dist = dist.normalize();
        double d1 = viewVec.dot(dist);
        // This overlap is huge
        return d1 > 1.0D - 0.60D / d0 ? player.hasLineOfSight(this) : false;
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

    @org.jetbrains.annotations.Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@org.jetbrains.annotations.Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    public class WraithStalkerMeleeGoal extends WraithMeleeAttackGoal {

        public WraithStalkerMeleeGoal(WraithStalker stalker) {
            super(stalker);
        }

        @Override
        public void stop() {
            this.wraith.setInvulnerable(false);
            ((WraithStalker) this.wraith).stopBeingAngry();
            super.stop();
        }

        @Override
        public void tick() {
            if (this.wraith.level != null && !this.wraith.level.isClientSide) {
                WraithStalker stalker = (WraithStalker) this.wraith;
                stalker.isFrozen = false;
                for (Player player : stalker.level.getNearbyPlayers(TargetingConditions.forCombat().range(FREEZE_RANGE), stalker, stalker.getBoundingBox().inflate(FREEZE_RANGE, FREEZE_RANGE, FREEZE_RANGE))) {
                    if (stalker.isLookingAtMe(player) && !player.isInvisible()) {
                        stalker.moveControlStop();
                        stalker.lookControl.setLookAt(player);
                        stalker.setInvulnerable(true);
                        stalker.isFrozen = true;
                        break;
                    }
                }

                if (!stalker.isFrozen) {
                    stalker.setInvulnerable(false);
                    super.tick();
                }
            } else {
                wraith.setInvulnerable(false);
                super.tick();
            }

        }
    }
}
