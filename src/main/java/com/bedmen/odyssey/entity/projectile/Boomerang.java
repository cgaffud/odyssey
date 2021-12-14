package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.OdysseyDamageSource;
import com.bedmen.odyssey.items.equipment.BoomerangItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData;

public class Boomerang extends OdysseyAbstractArrow implements IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(Boomerang.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(Boomerang.class, EntityDataSerializers.BOOLEAN);
    private BoomerangType boomerangType = BoomerangType.WOOD;
    private ItemStack thrownStack = new ItemStack(ItemRegistry.WOODEN_BOOMERANG.get());
    private boolean dealtDamage;
    public int returningTicks;

    public Boomerang(EntityType<? extends Boomerang> type, Level worldIn) {
        super(type, worldIn);
    }

    public Boomerang(Level worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypeRegistry.BOOMERANG.get(), thrower, worldIn);
        this.thrownStack = thrownStackIn.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentUtil.getLoyalty(thrownStackIn));
        this.entityData.set(ID_FOIL, thrownStackIn.hasFoil());
        this.boomerangType = ((BoomerangItem)this.thrownStack.getItem()).getBoomerangType();
    }

    @OnlyIn(Dist.CLIENT)
    public Boomerang(Level p_i48791_1_, double p_i48791_2_, double p_i48791_4_, double p_i48791_6_) {
        super(EntityTypeRegistry.BOOMERANG.get(), p_i48791_2_, p_i48791_4_, p_i48791_6_, p_i48791_1_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte)0);
        this.entityData.define(ID_FOIL, false);
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
            int i = this.entityData.get(ID_LOYALTY);
            if (i > 0 && !this.shouldReturnToThrower()) {
                if (!this.level.isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else if (i > 0) {
                this.setNoPhysics(true);
                Vec3 vector3d = new Vec3(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
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
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
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

//    @Nullable
//    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
//        if(this.dealtDamage){
//            if(!this.level.isClientSide){
//                AABB box = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D);
//                for(Entity entity : this.level.getEntities(this, box, this::isBabySkeletonOwner)) {
//                    BabySkeleton babySkeleton = (BabySkeleton)entity;
//                    if(babySkeleton.getMainHandItem().isEmpty()){
//                        babySkeleton.setItemInHand(InteractionHand.MAIN_HAND, this.thrownStack);
//                    } else if (babySkeleton.getOffhandItem().isEmpty()){
//                        babySkeleton.setItemInHand(InteractionHand.OFF_HAND, this.thrownStack);
//                    }
//                    this.discard();
//                    break;
//                }
//            }
//            return null;
//        } else {
//            return super.findHitEntity(startVec, endVec);
//        }
//    }

//    private boolean isBabySkeletonOwner(Entity entity){
//        return entity instanceof BabySkeleton && entity.getUUID() == this.getOwner().getUUID();
//    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityHitResult p_213868_1_) {
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
    public void playerTouch(Player entityIn) {
        Entity entity = this.getOwner();
        if (entity == null || entity.getUUID() == entityIn.getUUID()) {
            super.playerTouch(entityIn);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (compoundNBT.contains("Boomerang", 10)) {
            this.thrownStack = ItemStack.of(compoundNBT.getCompound("Boomerang"));
        }
        this.dealtDamage = compoundNBT.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, (byte)EnchantmentUtil.getLoyalty(this.thrownStack));
        if (compoundNBT.contains("BoomerangType")) {
            this.boomerangType = BoomerangType.valueOf(compoundNBT.getString("BoomerangType"));
        }
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.put("Boomerang", this.thrownStack.save(new CompoundTag()));
        compoundNBT.putBoolean("DealtDamage", this.dealtDamage);
        compoundNBT.putString("BoomerangType", this.boomerangType.name());
    }


    public BoomerangType getBoomerangType(){
        return this.boomerangType;
    }

    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != Pickup.ALLOWED || i <= 0) {
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

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.getOwner().getId());
        buffer.writeInt(this.boomerangType.ordinal());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.setOwner(this.level.getEntity(additionalData.readInt()));
        this.boomerangType = BoomerangType.values()[additionalData.readInt()];
    }

    public enum BoomerangType{
        WOOD(4.0d, 500, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/wooden_boomerang.png")),
        BONE(5.0d, 0, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/bone_boomerang.png"));
//        CHARMED(5.0d, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/charmed_boomerang.png")),
//        COPPER(6.0d, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/copper_boomerang.png"));

        private final double damage;
        private final int burnTime;
        private final ResourceLocation resourceLocation;

        BoomerangType(double damage, int burnTime, ResourceLocation resourceLocation){
            this.damage = damage;
            this.burnTime = burnTime;
            this.resourceLocation = resourceLocation;
        }

        public double getDamage(){
            return this.damage;
        }

        public int getBurnTime(){
            return this.burnTime;
        }

        public ResourceLocation getResourceLocation(){
            return this.resourceLocation;
        }
    }
}
