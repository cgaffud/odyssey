package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.util.BossUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
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
import net.minecraftforge.fml.network.NetworkHooks;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PermafrostIcicleEntity extends DamagingProjectileEntity {
    private float damage = 6.0f;
    private static final int maxTicks = 40;
    private static Field leftOwnerField;
    private static Method checkLeftOwnerMethod;

    static {
        try {
            leftOwnerField = ProjectileEntity.class.getDeclaredField("leftOwner");
            checkLeftOwnerMethod = ProjectileEntity.class.getDeclaredMethod("checkLeftOwner");
            leftOwnerField.setAccessible(true);
            checkLeftOwnerMethod.setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public PermafrostIcicleEntity(EntityType<? extends PermafrostIcicleEntity> entityType, World world) {
        super(entityType, world);
    }

    public PermafrostIcicleEntity(World p_i1794_1_, LivingEntity p_i1794_2_, double x, double y, double z, float damage) {
        super(EntityTypeRegistry.PERMAFROST_ICICLE.get(), p_i1794_2_, x, y, z, p_i1794_1_);
        this.damage = damage;
        Vector3d vector3d = new Vector3d(x,y,z);
        this.setRotation(vector3d);
        this.setDeltaMovement(vector3d.normalize().scale(0.8));
    }

    @OnlyIn(Dist.CLIENT)
    public PermafrostIcicleEntity(World p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
        super(EntityTypeRegistry.PERMAFROST_ICICLE.get(), p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_, p_i1795_1_);
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
        if ((this.level.isClientSide || (entity == null || !entity.removed) && this.level.hasChunkAt(this.blockPosition())) && this.tickCount <= maxTicks) {
            try {
                if (!(Boolean)leftOwnerField.get(this)) {
                    leftOwnerField.set(this, checkLeftOwnerMethod.invoke(this));
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            if (!this.level.isClientSide) {
                this.setSharedFlag(6, this.isGlowing());
            }

            this.baseTick();

            RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            this.checkInsideBlocks();
            
            Vector3d vector3d = this.getDeltaMovement();
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
            this.remove();
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

    protected IParticleData getTrailParticle() {
        return null;
    }

    protected float getInertia() {
        return 0.8F;
    }

    public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        p_213281_1_.put("power", this.newDoubleList(new double[]{this.xPower, this.yPower, this.zPower}));
    }

    public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        if (p_70037_1_.contains("power", 9)) {
            ListNBT listnbt = p_70037_1_.getList("power", 6);
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

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected void onHit(RayTraceResult p_70227_1_) {
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GLASS_BREAK, SoundCategory.HOSTILE, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        super.onHit(p_70227_1_);
        if (!this.level.isClientSide) {
            this.remove();
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        if (!this.level.isClientSide) {
            Entity entity = p_213868_1_.getEntity();
            Entity entity1 = this.getOwner();
            if (entity1 instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity1;
                entity.hurt(DamageSource.indirectMobAttack(this, livingentity), this.damage * BossUtil.difficultyDamageMultiplier(this.level.getDifficulty()));
            } else {
                entity.hurt(DamageSource.MAGIC, 0.0F);
            }
        }
    }

    public void setRotation(Vector3d vector3d) {
        float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
        if (vector3d.lengthSqr() != 0.0D) {
            this.xRot = (float)(MathHelper.atan2(f, vector3d.y) * (double)(180F / (float)Math.PI) * -1.0f + 90.0f);
            this.xRotO = this.xRot;
            this.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
            this.yRotO = this.yRot;
        }
    }
}
