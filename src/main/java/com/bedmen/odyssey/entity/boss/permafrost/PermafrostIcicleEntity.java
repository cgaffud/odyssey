package com.bedmen.odyssey.entity.boss.permafrost;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.mojang.math.Vector3d;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public class PermafrostIcicleEntity extends AbstractHurtingProjectile {
    private float damage = 6.0f;
    private static final int maxTicks = 40;

    public PermafrostIcicleEntity(EntityType<? extends PermafrostIcicleEntity> entityType, Level level) {
        super(entityType, level);
    }

    public PermafrostIcicleEntity(Level level, LivingEntity livingEntity, double x, double y, double z, float damage) {
        super(EntityTypeRegistry.PERMAFROST_ICICLE_ENTITY.get(), livingEntity, x, y, z, level);
        this.damage = damage;
        Vec3 vec3 = new Vec3(x,y,z);
        this.setRotation(vec3);
        this.setDeltaMovement(vec3.normalize().scale(0.8));
    }

    @OnlyIn(Dist.CLIENT)
    public PermafrostIcicleEntity(Level p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
        super(EntityTypeRegistry.PERMAFROST_ICICLE_ENTITY.get(), p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_, p_i1795_1_);
    }

    protected void defineSynchedData() {
    }

    @OnlyIn(Dist.CLIENT)
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
        if ((this.level.isClientSide || (entity == null || !entity.isRemoved()) && this.level.hasChunkAt(this.blockPosition())) && this.tickCount <= maxTicks) {
            if (!this.leftOwner) {
                this.leftOwner = this.checkLeftOwner();
            }

            if (!this.level.isClientSide) {
                this.setSharedFlag(6, this.isCurrentlyGlowing());
            }

            this.baseTick();

            HitResult hitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
            if (hitResult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitResult)) {
                this.onHit(hitResult);
            }

            this.checkInsideBlocks();
            
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;

            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }
            }
            this.setPos(d0, d1, d2);
            this.setRotation(vec3);
        } else {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    protected boolean canHitEntity(Entity p_230298_1_) {
        return super.canHitEntity(p_230298_1_) && !p_230298_1_.noPhysics;
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
        return 0.8F;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("power", this.newDoubleList(new double[]{this.xPower, this.yPower, this.zPower}));
    }

    public void readAdditionalSaveData(CompoundTag p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        if (p_70037_1_.contains("power", 9)) {
            ListTag listTag = p_70037_1_.getList("power", 6);
            if (listTag.size() == 3) {
                this.xPower = listTag.getDouble(0);
                this.yPower = listTag.getDouble(1);
                this.zPower = listTag.getDouble(2);
            }
        }

    }

    public boolean isPickable() {
        return false;
    }

    public float getPickRadius() {
        return 1.0F;
    }

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected void onHit(HitResult p_70227_1_) {
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        super.onHit(p_70227_1_);
        if (!this.level.isClientSide) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected void onHitEntity(EntityHitResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        if (!this.level.isClientSide) {
            Entity entity = p_213868_1_.getEntity();
            Entity entity1 = this.getOwner();
            if (entity1 instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity1;
                entity.hurt(DamageSource.indirectMobAttack(this, livingentity).setScalesWithDifficulty(), this.damage);
            } else {
                entity.hurt(DamageSource.MAGIC, 0.0F);
            }
        }
    }

    public void setRotation(Vec3 vec3) {
        float f = Mth.sqrt( (float)(vec3.x() * vec3.x() + vec3.z() * vec3.z()));
        if (vec3.lengthSqr() != 0.0D) {
            this.setXRot((float)(Mth.atan2(f, vec3.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f));
            this.xRotO = this.getXRot();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
        }
    }
}
