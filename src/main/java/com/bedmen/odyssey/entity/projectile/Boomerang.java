package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.entity.OdysseyDamageSource;
import com.bedmen.odyssey.entity.monster.BabySkeleton;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

public class Boomerang extends OdysseyAbstractArrow implements IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(Boomerang.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(Boomerang.class, EntityDataSerializers.BOOLEAN);
    private BoomerangType boomerangType = BoomerangType.WOODEN;
    private ItemStack thrownStack = new ItemStack(ItemRegistry.WOODEN_BOOMERANG.get());
    private boolean dealtDamage;
    public int returningTicks;

    public Boomerang(EntityType<? extends Boomerang> type, Level level) {
        super(type, level);
    }

    public Boomerang(Level level, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityTypeRegistry.BOOMERANG.get(), thrower, level);
        this.thrownStack = thrownStackIn.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentUtil.getLoyalty(thrownStackIn));
        this.entityData.set(ID_FOIL, thrownStackIn.hasFoil());
        this.boomerangType = ((BoomerangItem)this.thrownStack.getItem()).getBoomerangType();
    }

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
        if (this.inGroundTime > 0 || this.tickCount > this.getBoomerangType().getReturnTime()) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
            int loyalty = this.getLoyalty();
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level.isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vector3d = new Vec3(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                double d0 = 0.03D * (double)(loyalty+1);
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(d0)));

                ++this.returningTicks;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
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

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    public int getLoyalty() {
        return this.entityData.get(ID_LOYALTY);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        if(this.dealtDamage){
            if(!this.level.isClientSide){
                AABB box = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D);
                for(Entity entity : this.level.getEntities(this, box, this::isBabySkeletonOwner)) {
                    BabySkeleton babySkeleton = (BabySkeleton)entity;
                    if(babySkeleton.getMainHandItem().isEmpty()){
                        babySkeleton.setItemInHand(InteractionHand.MAIN_HAND, this.thrownStack);
                    } else if (babySkeleton.getOffhandItem().isEmpty()){
                        babySkeleton.setItemInHand(InteractionHand.OFF_HAND, this.thrownStack);
                    }
                    this.discard();
                    break;
                }
            }
            return null;
        } else {
            return super.findHitEntity(startVec, endVec);
        }
    }

    private boolean isBabySkeletonOwner(Entity entity){
        return entity instanceof BabySkeleton && entity.getUUID() == this.getOwner().getUUID();
    }

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

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    protected boolean tryPickup(Player p_150196_) {
        return super.tryPickup(p_150196_) || this.isNoPhysics() && this.ownedBy(p_150196_) && p_150196_.getInventory().add(this.getPickupItem());
    }

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

    public ItemStack getThrownStack(){
        return this.thrownStack;
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

    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.getOwner() == null ? -1 : this.getOwner().getId());
        buffer.writeItem(this.thrownStack);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        int id = additionalData.readInt();
        this.setOwner(id == -1 ? null : this.level.getEntity(id));
        this.thrownStack = additionalData.readItem();
        Item item = this.thrownStack.getItem();
        if(item instanceof BoomerangItem boomerangItem){
            this.boomerangType = boomerangItem.getBoomerangType();
        }
    }

    public enum BoomerangType{
        WOODEN(4.0d, 20, 500),
        BONE(5.0d, 20, 0),
        BONERANG(7.0d, 10, 0),
        CLOVER_STONE(6.0d, 20, 0);
//        COPPER(6.0d, new ResourceLocation(Odyssey.MOD_ID, "textures/entity/projectiles/copper_boomerang.png"));

        private final double damage;
        private final int returnTime;
        private final int burnTime;

        BoomerangType(double damage, int returnTime, int burnTime){
            this.damage = damage;
            this.returnTime = returnTime;
            this.burnTime = burnTime;
        }

        public double getDamage(){
            return this.damage;
        }

        public int getReturnTime(){
            return this.returnTime;
        }

        public float getAttackTime(){
            return Math.round(10f * 20f / (float)this.getReturnTime()) / 10f - 4f;
        }

        public int getBurnTime(){
            return this.burnTime;
        }
    }
}
