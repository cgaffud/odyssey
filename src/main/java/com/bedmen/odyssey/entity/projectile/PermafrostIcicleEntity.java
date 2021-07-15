package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.util.EntityTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PermafrostIcicleEntity extends DamagingProjectileEntity {
    private float damage;
    private int timer;
    private LivingEntity target;
    private double rx;
    private double ry;
    private double rz;


    public PermafrostIcicleEntity(EntityType<? extends PermafrostIcicleEntity> p_i50147_1_, World p_i50147_2_) {
        super(p_i50147_1_, p_i50147_2_);
        this.damage = 8.0f;
        this.timer = 20;
        this.target = null;
        this.rx = 0.0d;
        this.ry = 0.0d;
        this.rz = 0.0d;
    }

    public PermafrostIcicleEntity(World p_i1794_1_, LivingEntity p_i1794_2_, double x, double y, double z, float damage, int timer, LivingEntity target) {
        super(EntityTypeRegistry.PERMAFROST_ICICLE.get(), p_i1794_2_, x, y, z, p_i1794_1_);
        this.damage = damage;
        this.timer = 20;
        this.target = target;
        Vector3d vector3d = this.getOwner().getPosition(1.0f);
        this.rx = x - vector3d.x;
        this.ry = y - vector3d.y;
        this.rz = z - vector3d.z;
        this.setDeltaMovement(Vector3d.ZERO);
    }

    @OnlyIn(Dist.CLIENT)
    public PermafrostIcicleEntity(World p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
        super(EntityTypeRegistry.PERMAFROST_ICICLE.get(), p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_, p_i1795_1_);
        this.damage = 8.0f;
        this.timer = 20;
        this.target = null;
        this.rx = 0.0d;
        this.ry = 0.0d;
        this.rz = 0.0d;
    }

    public void tick() {
        Entity entity = this.getOwner();
        if (this.level.isClientSide || (entity == null || !entity.removed) && this.level.hasChunkAt(this.blockPosition())) {
            if (this.shouldBurn()) {
                this.setSecondsOnFire(1);
            }

            RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            this.checkInsideBlocks();
            if(this.tickCount <= this.timer){
                this.setDeltaMovement(Vector3d.ZERO);
            }

            Vector3d vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            float f = this.getInertia();
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.BUBBLE, d0 - vector3d.x * 0.25D, d1 - vector3d.y * 0.25D, d2 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
                }

                f = 0.8F;
            }
            this.setDeltaMovement(vector3d.add(this.xPower, this.yPower, this.zPower).scale((double)f));
            this.setPos(d0, d1, d2);
        } else {
            this.remove();
        }
        if(this.tickCount > 80){
            this.remove();
        }
    }

    public void setRotation(Vector3d vector3d) {
        //System.out.println("fffff");
        vector3d = vector3d.subtract(this.getPosition(1.0f));
        //Vector3d vector3d = this.getDeltaMovement();
        float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        if (vector3d.lengthSqr() != 0.0D) {
            this.xRot = (float)(MathHelper.atan2(vector3d.y, f) * (double)(180F / (float)Math.PI)) * -1.0F + 180.0f;
            this.xRotO = this.xRot;
            this.yRot = (float)(MathHelper.atan2(vector3d.z, vector3d.x) * (double)(180F / (float)Math.PI)) * -1.0f - 90.0F;
            this.yRotO = this.yRot;
        }
    }

    public void setRotation(LivingEntity livingEntity) {
        Vector3d vector3d = livingEntity.getPosition(1.0f);
        vector3d = vector3d.subtract(this.getPosition(1.0f));
        float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        if (vector3d.lengthSqr() != 0.0D) {
            this.xRot = (float)(MathHelper.atan2(vector3d.y, f) * (double)(180F / (float)Math.PI)) * -1.0F + 180.0f;
            this.xRotO = this.xRot;
            this.yRot = (float)(MathHelper.atan2(vector3d.z, vector3d.x) * (double)(180F / (float)Math.PI)) * -1.0f - 90.0F;
            this.yRotO = this.yRot;
        }
    }

    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        if (!this.level.isClientSide) {
            Entity entity = p_213868_1_.getEntity();
            Entity entity1 = this.getOwner();
            if (entity1 instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity1;
                entity.hurt(DamageSource.indirectMobAttack(this, livingentity), this.damage * this.damageMult());
            } else {
                entity.hurt(DamageSource.MAGIC, 0.0F);
            }
        }
    }

    private float damageMult(){
        switch(this.level.getDifficulty()){
            case EASY: return 0.67f;
            case NORMAL: return 1.0f;
            case HARD: return 1.5f;
            default: return 0.0f;
        }
    }

    protected void onHit(RayTraceResult p_70227_1_) {
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, SoundCategory.HOSTILE, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        super.onHit(p_70227_1_);
        if (!this.level.isClientSide) {
            this.remove();
        }
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    protected boolean shouldBurn() {
        return false;
    }

    protected float getInertia() {
        return 0.8F;
    }

    protected IParticleData getTrailParticle() {
        return ParticleTypes.CRIT;
    }
}
