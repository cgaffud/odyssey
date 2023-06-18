package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import com.google.common.base.MoreObjects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class WraithAmalgamProjectile extends Projectile  {
    private static final double SPEED = 0.25D;
    @Nullable
    private Entity finalTarget;
    private int flightSteps;
    private double targetX;
    private double targetY;
    private double targetZ;
    @Nullable
    private UUID targetId;

    public WraithAmalgamProjectile(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.noPhysics = true;
    }

    public WraithAmalgamProjectile(Level level, LivingEntity parent, Entity target) {
        this(EntityTypeRegistry.WRAITH_AMALGAM_PROJECTILE.get(), level);
        this.setOwner(parent);
        Vec3 eyePosition = parent.getEyePosition();
        float phi = parent.getYHeadRot();
        // Todo: This will have to be adjusted for the second head
        double adjustedX = eyePosition.x() - 0.5*Mth.cos(phi);
        double adjustedZ = eyePosition.z() - 0.5*Mth.sin(phi);
        this.moveTo(adjustedX, eyePosition.y(), adjustedZ, this.getYRot(), this.getXRot());
        this.finalTarget = target;
        this.targetX = target.getX();
        this.targetY = target.getEyeY()-0.5;
        this.targetZ = target.getZ();
        this.targetId = target.getUUID();
    }

    protected void defineSynchedData() {
    }


    protected void addAdditionalSaveData(CompoundTag p_37357_) {
        super.addAdditionalSaveData(p_37357_);
        if (this.finalTarget != null) {
            p_37357_.putUUID("Target", this.targetId);
        }

        p_37357_.putInt("Steps", this.flightSteps);
        p_37357_.putDouble("TX", this.targetX);
        p_37357_.putDouble("TY", this.targetY);
        p_37357_.putDouble("TZ", this.targetZ);
    }

    protected void readAdditionalSaveData(CompoundTag p_37353_) {
        super.readAdditionalSaveData(p_37353_);
        this.flightSteps = p_37353_.getInt("Steps");
        this.targetX = p_37353_.getDouble("TX");
        this.targetY = p_37353_.getDouble("TY");
        this.targetZ = p_37353_.getDouble("TZ");
        if (p_37353_.hasUUID("Target")) {
            this.targetId = p_37353_.getUUID("Target");
        }

    }

    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }



    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL ) {
            this.discard();
        }
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (this.finalTarget == null && this.targetId != null) {
                this.finalTarget = ((ServerLevel) this.level).getEntity(this.targetId);
                if (this.finalTarget == null) {
                    this.targetId = null;
                }
            }


            if (this.finalTarget == null || !this.finalTarget.isAlive() || this.finalTarget instanceof Player && this.finalTarget.isSpectator()) {
                if (!this.isNoGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.0D, 0.0D));
                }
            } else {
                this.targetX = this.finalTarget.getX();
                this.targetY = this.finalTarget.getEyeY()-0.5;
                this.targetZ = this.finalTarget.getZ();

                Vec3 targetDir = new Vec3(this.targetX-this.getX(), this.targetY-this.getY(), this.targetZ-this.getZ()).normalize();

                this.setDeltaMovement(targetDir.scale(this.SPEED));
            }

            HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (hitresult.getType() == HitResult.Type.ENTITY && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
        }


        this.hasImpulse = true;
        this.checkInsideBlocks();
        ProjectileUtil.rotateTowardsMovement(this, 0.2F);
        Vec3 vec31 = this.getDeltaMovement();
        this.setPos(this.getX() + vec31.x, this.getY() + vec31.y, this.getZ() + vec31.z);

        this.checkInsideBlocks();
    }

    protected boolean canHitEntity(Entity p_37341_) {
        return super.canHitEntity(p_37341_) && !p_37341_.noPhysics;
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean shouldRenderAtSqrDistance(double p_37336_) {
        return p_37336_ < 16384.0D;
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected void onHitEntity(EntityHitResult p_37345_) {
        super.onHitEntity(p_37345_);
        Entity target = p_37345_.getEntity();
        LivingEntity parent = (LivingEntity) this.getOwner();
        boolean flag = target.hurt(DamageSource.indirectMobAttack(this, parent).setProjectile(), 2.0F);
        if (flag) {
            this.doEnchantDamageEffects(parent, target);
            if (target instanceof LivingEntity livingTarget) {
                livingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100), MoreObjects.firstNonNull(parent, this));
                livingTarget.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100), MoreObjects.firstNonNull(parent, this));
            }
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult p_37343_) { }

    protected void onHit(HitResult p_37347_) {
        super.onHit(p_37347_);
        this.discard();
    }

    public boolean isPickable() {
        return true;
    }

    public boolean hurt(DamageSource damageSource, float p_37339_) {
        if (damageSource.isProjectile())
            return false;

        if (!this.level.isClientSide) {
            this.playSound(SoundEventRegistry.WRAITH_HURT.get(), 1.0F, 1.0F);
            ((ServerLevel)this.level).sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
            this.discard();
        }
        return true;
    }

    public void recreateFromPacket(ClientboundAddEntityPacket p_150185_) {
        super.recreateFromPacket(p_150185_);
        double d0 = p_150185_.getXa();
        double d1 = p_150185_.getYa();
        double d2 = p_150185_.getZa();
        this.setDeltaMovement(d0, d1, d2);
    }

}
