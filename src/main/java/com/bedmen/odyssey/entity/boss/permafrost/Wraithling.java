package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.entity.boss.BossSubEntity;
import com.bedmen.odyssey.entity.monster.AbstractWraith;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class Wraithling extends AbstractWraith {

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;

    public Wraithling(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_, 0.3D, 0.70D, 0.5D, 2.5D, 35);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WraithlingMeleeAttackGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (livingEntity -> true)));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 7.5D).add(Attributes.MOVEMENT_SPEED, 1.5D).add(Attributes.ATTACK_DAMAGE, 3D).add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public void startPersistentAngerTimer() {

    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(EffectRegistry.PERMAFROST_SMALL_FREEZING.get(), 50));
        }
        return super.doHurtTarget(entity);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float p_21017_) {
        if (this.getOwner() != null && this.getOwner() instanceof PermafrostWraith &&
                (damageSource.getEntity() instanceof BossSubEntity || damageSource.getEntity() instanceof PermafrostMaster
                        || damageSource.getEntity() instanceof PermafrostIcicleEntity))
            return false;
        return super.hurt(damageSource, p_21017_);
    }

    @Override
    public void die(DamageSource damageSource) {
        if (this.getOwner() != null && this.getOwner() instanceof PermafrostSpawnerIcicle spawnerIcicle) {
            spawnerIcicle.setWraithlingNum(spawnerIcicle.getWraithlingNum()-1);
        }
        super.die(damageSource);
    }

    public void setOwner(@Nullable Entity p_37263_) {
        if (p_37263_ != null) {
            this.ownerUUID = p_37263_.getUUID();
            this.cachedOwner = p_37263_;
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.level instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel)this.level).getEntity(this.ownerUUID);
            return this.cachedOwner;
        } else {
            return null;
        }
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.ownerUUID != null) {
            compoundTag.putUUID("Owner", this.ownerUUID);
        }
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.hasUUID("Owner")) {
            this.ownerUUID = compoundTag.getUUID("Owner");
        }
    }

    private class WraithlingMeleeAttackGoal extends WraithMeleeAttackGoal {

        private boolean strafing;
        private boolean strafingClockwise;
        private int strafeTicker;

        public WraithlingMeleeAttackGoal(AbstractWraith wraith) {
            super(wraith);
        }

        @Override
        public void start() {
            super.start();
            strafing = false;
            strafingClockwise = false;
            strafeTicker = 0;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = wraith.getTarget();
            if (livingentity != null) {
                wraith.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                double d0 = wraith.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                if (d0 > 30) {
                    strafing = false;
                    strafeTicker = 0;
                    runAtTargetAndMelee(livingentity, d0);
                } else {
                    if (wraith.getRandom().nextFloat() < 0.35f || strafeTicker > 20) {
                        strafing = !strafing;
                        if (strafing) strafeTicker = 0;
                    }

                    if (strafing) {
                        strafeTicker ++;
                        if (wraith.getRandom().nextFloat() < 0.25f)
                            strafingClockwise = !strafingClockwise;
                        wraith.getMoveControl().strafe(0 , strafingClockwise ? 1.0f : -1.0f );
                        wraith.lookAt(livingentity, 30.0f, 30.0f);
                    } else {
                        runAtTargetAndMelee(livingentity, d0);
                    }

                }
            }

            super.tick();
        }
    }
}
