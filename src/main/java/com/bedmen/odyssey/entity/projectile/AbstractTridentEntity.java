package com.bedmen.odyssey.entity.projectile;

import javax.annotation.Nullable;

import com.bedmen.odyssey.util.EntityTypeRegistry;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractTridentEntity extends AbstractArrowEntity {
    private static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.defineId(AbstractTridentEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> ID_FOIL = EntityDataManager.defineId(AbstractTridentEntity.class, DataSerializers.BOOLEAN);
    private ItemStack thrownStack;
    private boolean dealtDamage;
    public int returningTicks;
    private double damage = 1.0D;

    public AbstractTridentEntity(EntityType<? extends AbstractTridentEntity> type, World worldIn, ItemStack itemStack) {
        super(type, worldIn);
        this.thrownStack = itemStack;
    }

    public AbstractTridentEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn, double damage, EntityType<? extends AbstractTridentEntity> type) {
        super(type, thrower, worldIn);
        this.damage = damage;
        this.thrownStack = thrownStackIn.copy();
        this.entityData.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getLoyalty(thrownStackIn));
        this.entityData.set(ID_FOIL, thrownStackIn.hasFoil());
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractTridentEntity(World worldIn, double x, double y, double z, EntityType<? extends AbstractTridentEntity> type, ItemStack itemStack) {
        super(type, x, y, z, worldIn);
        this.thrownStack = itemStack;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOYALTY_LEVEL, (byte)0);
        this.entityData.define(ID_FOIL, false);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
            int i = this.entityData.get(LOYALTY_LEVEL);
            if (i > 0 && !this.shouldReturnToThrower()) {
                if (!this.level.isClientSide && this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.remove();
            } else if (i > 0) {
                this.setNoPhysics(true);
                Vector3d vector3d = new Vector3d(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                this.setPosRaw(this.getX(), this.getY() + vector3d.y * 0.015D * (double)i, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double)i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(d0)));
                if (this.returningTicks == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.returningTicks;
            }
        }

        super.tick();
    }

    private boolean shouldReturnToThrower() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    protected ItemStack getPickupItem() {
        return this.thrownStack.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    /**
     * Gets the EntityRayTraceResult representing the entity hit
     */
    @Nullable
    protected EntityRayTraceResult findHitEntity(Vector3d startVec, Vector3d endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        Entity entity = p_213868_1_.getEntity();
        float f = (float)this.damage - 1.0f;
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity;
            f += EnchantmentHelper.getDamageBonus(this.thrownStack, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = DamageSource.trident(this, (Entity)(entity1 == null ? this : entity1));
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity)entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;
        if (this.level instanceof ServerWorld && this.level.isThundering() && EnchantmentHelper.hasChanneling(this.thrownStack)) {
            BlockPos blockpos = entity.blockPosition();
            if (this.level.canSeeSky(blockpos)) {
                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.level);
                lightningboltentity.moveTo(Vector3d.atBottomCenterOf(blockpos));
                lightningboltentity.setCause(entity1 instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity1 : null);
                this.level.addFreshEntity(lightningboltentity);
                soundevent = SoundEvents.TRIDENT_THUNDER;
                f1 = 5.0F;
            }
        }

        this.playSound(soundevent, f1, 1.0F);
    }

    /**
     * The sound made when an entity is hit by this projectile
     */
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void playerTouch(PlayerEntity entityIn) {
        Entity entity = this.getOwner();
        if (entity == null || entity.getUUID() == entityIn.getUUID()) {
            super.playerTouch(entityIn);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Trident", 10)) {
            this.thrownStack = ItemStack.of(compound.getCompound("Trident"));
        }

        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.entityData.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getLoyalty(this.thrownStack));
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Trident", this.thrownStack.save(new CompoundNBT()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn() {
        int i = this.entityData.get(LOYALTY_LEVEL);
        if (this.pickup != AbstractArrowEntity.PickupStatus.ALLOWED || i <= 0) {
            super.tickDespawn();
        }

    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    public Item getItem(){
        return this.thrownStack.getItem();
    }
}
