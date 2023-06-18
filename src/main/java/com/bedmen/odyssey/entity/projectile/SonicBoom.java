package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class SonicBoom extends AbstractHurtingProjectile implements SwungProjectile {
    private static final int MAX_TICKS = 40;
    public static final float INITIAL_SPEED = 1.2f;

    public SonicBoom(EntityType<? extends SonicBoom> entityType, Level world) {
        super(entityType, world);
    }

    public SonicBoom(Level level, LivingEntity livingEntity) {
        this(level, livingEntity, livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ(), 1d, 1d, 1d);
    }

    public SonicBoom(Level level, LivingEntity livingEntity, double x, double y, double z, double dx, double dy, double dz) {
        super(EntityTypeRegistry.SONIC_BOOM.get(), livingEntity, dx, dy, dz, level);
        this.setPos(x, y, z);
        Vec3 vector3d = new Vec3(dx,dy,dz);
        this.setRotation(vector3d);
        this.setDeltaMovement(vector3d.normalize().scale(INITIAL_SPEED));
    }

    public SonicBoom(Level level, double x, double y, double z, double dx, double dy, double dz) {
        super(EntityTypeRegistry.SONIC_BOOM.get(), x, y, z, dx, dy, dz, level);
    }

    protected void defineSynchedData() {
    }

    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return p_70112_1_ < d0 * d0;
    }

    public void tick() {
        Entity entity = this.getOwner();
        if ((this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.hasChunkAt(this.blockPosition())) && this.tickCount <= MAX_TICKS) {
            if (!this.leftOwner) {
                this.leftOwner = this.checkLeftOwner();
            }

            if (!this.level.isClientSide) {
                this.setSharedFlag(6, this.isCurrentlyGlowing());
            }

            this.baseTick();

            HitResult raytraceresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            this.checkInsideBlocks();
            
            Vec3 vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;

            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.BUBBLE, d0 - vector3d.x * 0.25D, d1 - vector3d.y * 0.25D, d2 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
                }
            }
            this.setPos(d0, d1, d2);
            this.setRotation(vector3d);
        } else {
            this.discard();
        }
    }

    protected boolean canHitEntity(Entity entity) {
        if (!entity.isSpectator() && entity.isAlive() && entity.isPickable()) {
            Entity owner = this.getOwner();
            return owner == null || this.leftOwner || !entity.isPassengerOfSameVehicle(entity);
        } else {
            return false;
        }
    }

    public boolean isOnFire() {
        return false;
    }

    protected boolean shouldBurn() {
        return false;
    }

    protected ParticleOptions getTrailParticle() {
        return null;
    }

    protected float getInertia() {
        return 0.85F;
    }

    public void addAdditionalSaveData(CompoundTag p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        p_213281_1_.put("power", this.newDoubleList(this.xPower, this.yPower, this.zPower));
    }

    public void readAdditionalSaveData(CompoundTag p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        if (p_70037_1_.contains("power", 9)) {
            ListTag listnbt = p_70037_1_.getList("power", 6);
            if (listnbt.size() == 3) {
                this.xPower = listnbt.getDouble(0);
                this.yPower = listnbt.getDouble(1);
                this.zPower = listnbt.getDouble(2);
            }
        }

    }

    public boolean isPickable() {
        return false;
    }

    public float getPickRadius() {
        return 1.0F;
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        return false;
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level.isClientSide) {
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.25f, Explosion.BlockInteraction.NONE);
            this.discard();
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setRotation(Vec3 vector3d) {
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        if (vector3d.lengthSqr() != 0.0D) {
            this.setXRot((float)(Mth.atan2(f, vector3d.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f));
            this.xRotO = this.getXRot();
            this.setYRot((float)(Mth.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
        }
    }

    public void launch(Entity entity, float xRot, float yRot, float zRot, float inaccuracy) {
        shootFromRotation(entity, xRot, yRot, zRot, INITIAL_SPEED, inaccuracy);
    }

    protected float getEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return 0.25F;
    }
}
