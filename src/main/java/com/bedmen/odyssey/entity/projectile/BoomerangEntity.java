package com.bedmen.odyssey.entity.projectile;

import javax.annotation.Nullable;

import com.bedmen.odyssey.entity.monster.BabySkeletonEntity;
import com.bedmen.odyssey.items.equipment.BoomerangItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.OdysseyDamageSource;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

public class BoomerangEntity extends AbstractArrowEntity {
    private static final DataParameter<Byte> LOYALTY_LEVEL = EntityDataManager.defineId(BoomerangEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> ID_FOIL = EntityDataManager.defineId(BoomerangEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> BOOMERANG_TYPE = EntityDataManager.defineId(BoomerangEntity.class, DataSerializers.STRING);
    private ItemStack thrownStack = new ItemStack(ItemRegistry.COPPER_BOOMERANG.get());
    private boolean dealtDamage;
    public int returningTicks;

    public BoomerangEntity(EntityType<? extends BoomerangEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public BoomerangEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypeRegistry.BOOMERANG.get(), thrower, worldIn);
        this.thrownStack = thrownStackIn.copy();
        this.entityData.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getLoyalty(thrownStackIn));
        this.entityData.set(ID_FOIL, thrownStackIn.hasFoil());
        this.setBoomerangType(((BoomerangItem)this.thrownStack.getItem()).getBoomerangType());
    }

    @OnlyIn(Dist.CLIENT)
    public BoomerangEntity(World p_i48791_1_, double p_i48791_2_, double p_i48791_4_, double p_i48791_6_) {
        super(EntityTypeRegistry.BOOMERANG.get(), p_i48791_2_, p_i48791_4_, p_i48791_6_, p_i48791_1_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOYALTY_LEVEL, (byte)0);
        this.entityData.define(ID_FOIL, false);
        this.entityData.define(BOOMERANG_TYPE, BoomerangItem.BoomerangType.COPPER.name());
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        this.setNoGravity(true);
        if (this.inGroundTime > 0 || this.tickCount > 20) {
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
                double d0 = 0.05D * (double)i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(d0)));

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
        if(this.dealtDamage){
            if(!this.level.isClientSide){
                AxisAlignedBB box = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D);
                for(Entity entity : this.level.getEntities(this, box, this::isBabySkeletonOwner)) {
                    BabySkeletonEntity babySkeletonEntity = (BabySkeletonEntity)entity;
                    if(babySkeletonEntity.getMainHandItem().isEmpty()){
                        babySkeletonEntity.setItemInHand(Hand.MAIN_HAND, this.thrownStack);
                    } else if (babySkeletonEntity.getOffhandItem().isEmpty()){
                        babySkeletonEntity.setItemInHand(Hand.OFF_HAND, this.thrownStack);
                    }
                    this.remove();
                    break;
                }
            }
            return null;
        } else {
            return super.findHitEntity(startVec, endVec);
        }
    }

    private boolean isBabySkeletonOwner(Entity entity){
        return entity instanceof BabySkeletonEntity && entity.getUUID() == this.getOwner().getUUID();
    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        Entity entity = p_213868_1_.getEntity();
        float f = (float)this.getBoomerangType().getDamage();
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity;
            f += EnchantmentHelper.getDamageBonus(this.thrownStack, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = OdysseyDamageSource.boomerang(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
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
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0f, 1.0F);
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
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (compoundNBT.contains("Boomerang", 10)) {
            this.thrownStack = ItemStack.of(compoundNBT.getCompound("Boomerang"));
        }
        this.dealtDamage = compoundNBT.getBoolean("DealtDamage");
        this.entityData.set(LOYALTY_LEVEL, (byte)EnchantmentHelper.getLoyalty(this.thrownStack));
        if (compoundNBT.contains("BoomerangType")) {
            this.setBoomerangType(compoundNBT.getString("BoomerangType"));
        }
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.put("Boomerang", this.thrownStack.save(new CompoundNBT()));
        compoundNBT.putBoolean("DealtDamage", this.dealtDamage);
        compoundNBT.putString("BoomerangType", this.entityData.get(BOOMERANG_TYPE));
    }


    public BoomerangItem.BoomerangType getBoomerangType(){
        return BoomerangItem.BoomerangType.valueOf(this.entityData.get(BOOMERANG_TYPE));
    }

    public void setBoomerangType(BoomerangItem.BoomerangType BoomerangType){
        this.entityData.set(BOOMERANG_TYPE, BoomerangType.name());
    }

    public void setBoomerangType(String s){
        this.entityData.set(BOOMERANG_TYPE, s);
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
}
