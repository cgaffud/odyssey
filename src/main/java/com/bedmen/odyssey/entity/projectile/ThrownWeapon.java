package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.aspect.AspectStrengthMap;
import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.combat.ThrowableType;
import com.bedmen.odyssey.items.aspect_items.ThrowableWeaponItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

public abstract class ThrownWeapon extends OdysseyAbstractArrow implements IEntityAdditionalSpawnData {

    private static final EntityDataAccessor<Float> DATA_LOYALTY_ASPECT = SynchedEntityData.defineId(ThrownWeapon.class, EntityDataSerializers.FLOAT);

    private static final String THROWN_WEAPON_ITEMSTACK_TAG = "ThrownWeaponItemStack";
    private static final String DONE_DEALING_DAMAGE_TAG = "DoneDealingDamage";
    private static final String THROWABLE_TYPE_TAG = "ThrowableType";
    private static final String IS_MULTISHOT_CLONE_TAG = "IsMultishotClone";

    protected ItemStack thrownStack = ItemStack.EMPTY;
    protected boolean isMultishotClone = false;
    protected ThrowableType throwableType = null;
    protected boolean doneDealingDamage;

    public ThrownWeapon(EntityType<? extends ThrownWeapon> type, Level level) {
        super(type, level);
    }

    public ThrownWeapon(EntityType<? extends ThrownWeapon> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    public ThrownWeapon(EntityType<? extends ThrownWeapon> type, Level level, LivingEntity thrower, ItemStack thrownStackIn, boolean isMultishotClone) {
        super(type, thrower, level);
        this.thrownStack = thrownStackIn.copy();
        this.throwableType = ((ThrowableWeaponItem)this.thrownStack.getItem()).throwableType;
        this.isMultishotClone = isMultishotClone;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_LOYALTY_ASPECT, 0.0f);
    }

    public void addAspectStrengthMap(AspectStrengthMap aspectStrengthMap){
        super.addAspectStrengthMap(aspectStrengthMap);
        if(this.aspectStrengthMap.containsKey(Aspects.LOYALTY)){
            this.entityData.set(DATA_LOYALTY_ASPECT, this.aspectStrengthMap.getNonNull(Aspects.LOYALTY));
        }
    }

    public float getAspectStrength(Aspect aspect){
        if(aspect == Aspects.LOYALTY){
            return this.entityData.get(DATA_LOYALTY_ASPECT);
        }
        return super.getAspectStrength(aspect);
    }

    protected void despawn(){
        if (!this.level.isClientSide && this.pickup == Pickup.ALLOWED && !this.isMultishotClone) {
            this.spawnAtLocation(this.getPickupItem(), 0.1F);
        }
        this.discard();
    }

    protected void markAsDoneDealingDamage(){
        this.doneDealingDamage = true;
    }

    protected void onFinalPierce() {
        this.markAsDoneDealingDamage();
        this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
    }

    public ItemStack getThrownStack(){
        return this.thrownStack;
    }

    public void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    protected double getDamage() {
        return this.throwableType.thrownDamage * this.getBaseDamage();
    }

    protected void onHurt(Entity target, boolean hurtSuccessful) {}

    protected SoundEvent getEntityHitSoundEvent(){
        return this.throwableType.soundProfile.entityHitSound;
    }

    protected ItemStack getPickupItem() {
        return this.isMultishotClone ? ItemStack.EMPTY : this.thrownStack.copy();
    }

    protected boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    public void tick(){
        if (this.isDoneDealingDamage()) {
            this.markAsDoneDealingDamage();
        }

        if (this.hasAspect(Aspects.LOYALTY) && (this.doneDealingDamage || this.isNoPhysics())) {
            if (!this.isAcceptibleReturnOwner()) {
                this.despawn();
            } else {
                this.doLoyaltyMovement();
            }
        }

        super.tick();
    }

    protected abstract boolean isDoneDealingDamage();

    protected void doLoyaltyMovement(){
        Entity owner = this.getOwner();
        this.setNoPhysics(true);
        Vec3 vector3d = new Vec3(owner.getX() - this.getX(), owner.getEyeY() - this.getY(), owner.getZ() - this.getZ());
        double returnSpeed = 0.06D * this.getAspectStrength(Aspects.LOYALTY);
        this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(returnSpeed)));
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        if(this.doneDealingDamage){
            if(!this.level.isClientSide){
                AABB box = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D);
                this.level.getEntities(this, box, this::isNonPlayerOwner)
                        .stream().findFirst()
                        .ifPresent(this::tryToGiveToNonPlayerOwner);
            }
        } else if(!this.isNoPhysics()) {
            return super.findHitEntity(startVec, endVec);
        }
        return null;
    }

    private boolean isNonPlayerOwner(Entity entity){
        if(this.getOwner() == null){
            return false;
        }
        return !(entity instanceof Player) && entity instanceof LivingEntity && this.ownedBy(entity);
    }

    protected void tryToGiveToNonPlayerOwner(Entity nonPlayerOwner){
        if (!this.isMultishotClone && nonPlayerOwner instanceof LivingEntity livingEntity) {
            if(livingEntity.getMainHandItem().isEmpty()){
                livingEntity.setItemInHand(InteractionHand.MAIN_HAND, this.thrownStack);
            } else if (livingEntity.getOffhandItem().isEmpty()){
                livingEntity.setItemInHand(InteractionHand.OFF_HAND, this.thrownStack);
            }
        }
        this.discard();
    }

    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    protected boolean tryPickup(Player player) {
        boolean isInLoyaltyReturnMode = this.isNoPhysics() && this.ownedBy(player);
        if(isInLoyaltyReturnMode && this.isMultishotClone){
            this.discard();
            return false;
        }
        return super.tryPickup(player) || isInLoyaltyReturnMode && player.getInventory().add(this.getPickupItem());
    }

    protected void onHitBlock(BlockHitResult blockHitResult) {
        this.setSoundEvent(this.throwableType.soundProfile.groundHitSound);
        super.onHitBlock(blockHitResult);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains(THROWN_WEAPON_ITEMSTACK_TAG, 10)) {
            this.thrownStack = ItemStack.of(compoundTag.getCompound(THROWN_WEAPON_ITEMSTACK_TAG));
        }
        this.doneDealingDamage = compoundTag.getBoolean(DONE_DEALING_DAMAGE_TAG);
        if (compoundTag.contains(THROWABLE_TYPE_TAG)) {
            this.throwableType = ThrowableType.fromName(compoundTag.getString(THROWABLE_TYPE_TAG));
        }
        this.isMultishotClone = compoundTag.getBoolean(IS_MULTISHOT_CLONE_TAG);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.put(THROWN_WEAPON_ITEMSTACK_TAG, this.thrownStack.save(new CompoundTag()));
        compoundTag.putBoolean(DONE_DEALING_DAMAGE_TAG, this.doneDealingDamage);
        compoundTag.putString(THROWABLE_TYPE_TAG, this.throwableType.getName());
        compoundTag.putBoolean(IS_MULTISHOT_CLONE_TAG, this.isMultishotClone);
    }

    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.getOwner() == null ? -1 : this.getOwner().getId());
        buffer.writeItem(this.thrownStack);
    }

    public void readSpawnData(FriendlyByteBuf additionalData) {
        int id = additionalData.readInt();
        this.setOwner(id == -1 ? null : this.level.getEntity(id));
        this.thrownStack = additionalData.readItem();
        Item item = this.thrownStack.getItem();
        if(item instanceof ThrowableWeaponItem throwableWeaponItem){
            this.throwableType = throwableWeaponItem.throwableType;
        }
    }

}
