package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.entity.OdysseyDamageSource;
import com.bedmen.odyssey.entity.monster.BoomerangAttackMob;
import com.bedmen.odyssey.items.equipment.BoomerangItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

public class Boomerang extends OdysseyAbstractArrow implements IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(Boomerang.class, EntityDataSerializers.BYTE);
    private BoomerangType boomerangType = BoomerangType.WOODEN;
    private ItemStack thrownStack = new ItemStack(ItemRegistry.WOODEN_BOOMERANG.get());
    private boolean multishot = false;
    private boolean dealtDamage;
    public int returningTicks;
    private int despawnTicks;

    public Boomerang(EntityType<? extends Boomerang> type, Level level) {
        super(type, level);
    }

    public Boomerang(Level level, LivingEntity thrower, ItemStack thrownStackIn, boolean multishot) {
        super(EntityTypeRegistry.BOOMERANG.get(), thrower, level);
        this.thrownStack = thrownStackIn.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentUtil.getLoyalty(thrownStackIn));
        this.boomerangType = ((BoomerangItem)this.thrownStack.getItem()).getBoomerangType();
        this.multishot = multishot;
    }

    public Boomerang(Level p_i48791_1_, double p_i48791_2_, double p_i48791_4_, double p_i48791_6_) {
        super(EntityTypeRegistry.BOOMERANG.get(), p_i48791_2_, p_i48791_4_, p_i48791_6_, p_i48791_1_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte)0);
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
                despawn();
            } else {
                this.setNoPhysics(true);
                Vec3 vector3d = new Vec3(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                double d0 = 0.03D * (double)(loyalty+1);
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(d0)));

                ++this.returningTicks;
            }
        } else if (entity == null && this.getDeltaMovement().length() < 0.01){
            this.despawnTicks++;
        }

        if(this.despawnTicks > 20){
            despawn();
        }

        super.tick();
    }

    private void despawn(){
        if (!this.level.isClientSide && this.pickup == Pickup.ALLOWED) {
            this.spawnAtLocation(this.getPickupItem(), 0.1F);
        }

        this.discard();
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
        return this.multishot ? ItemStack.EMPTY : this.thrownStack.copy();
    }

    public int getLoyalty() {
        return this.entityData.get(ID_LOYALTY);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        if(this.dealtDamage){
            if(!this.level.isClientSide){
                AABB box = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D);
                for(Entity entity : this.level.getEntities(this, box, this::isBoomerangOwner)) {
                    LivingEntity livingEntity = (LivingEntity)entity;
                    if(livingEntity.getMainHandItem().isEmpty()){
                        livingEntity.setItemInHand(InteractionHand.MAIN_HAND, this.thrownStack);
                    } else if (livingEntity.getOffhandItem().isEmpty()){
                        livingEntity.setItemInHand(InteractionHand.OFF_HAND, this.thrownStack);
                    }
                    this.discard();
                    break;
                }
            }
        } else if(!this.isNoPhysics()) {
            return super.findHitEntity(startVec, endVec);
        }
        return null;
    }

    private boolean isBoomerangOwner(Entity entity){
        if(this.getOwner() == null){
            return false;
        }
        return entity instanceof BoomerangAttackMob && entity instanceof LivingEntity && entity.getUUID() == this.getOwner().getUUID();
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = (float)this.getBoomerangType().getDamage();
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity;
            f += EnchantmentHelper.getDamageBonus(this.thrownStack, livingentity.getMobType());
        }
        Entity entity1 = this.getOwner();
        DamageSource damagesource = OdysseyDamageSource.boomerang(this, entity1 == null ? this : entity1);
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingEntity) {
                if (this.knockback > 0) {
                    Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockback * 0.6D);
                    if (vec3.lengthSqr() > 0.0D) {
                        livingEntity.push(vec3.x, 0.1D, vec3.z);
                    }
                }

                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingEntity);
                }

                this.doPostHurtEffects(livingEntity);
            }
        }
        if (this.piercingIgnoreEntityIds == null) {
            this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
        }
        this.piercingIgnoreEntityIds.add(entity.getId());
        if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
            this.dealtDamage = true;
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        }
        this.playSound(SoundEvents.ARROW_HIT, 1.0f, 1.0F);
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ARROW_HIT;
    }

    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    protected boolean tryPickup(Player player) {
        boolean flag = this.isNoPhysics() && this.ownedBy(player);
        if(flag && this.multishot){
            this.discard();
            return false;
        }
        return super.tryPickup(player) || flag && player.getInventory().add(this.getPickupItem());
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
        this.multishot = compoundNBT.getBoolean("IsMultishot");
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.put("Boomerang", this.thrownStack.save(new CompoundTag()));
        compoundNBT.putBoolean("DealtDamage", this.dealtDamage);
        compoundNBT.putString("BoomerangType", this.boomerangType.name());
        compoundNBT.putBoolean("IsMultishot", this.multishot);
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
        UPGRADED_BONE(6.0d, 20, 0),
        SPEEDY_BONE(5.0d, 14, 0),
        BONERANG(6.0d, 14, 0),
        CLOVER_STONE(6.5d, 20, 0),
        GREATROOT(7.0d, 20, 0),
        UPGRADED_GREATROOT(8.0d, 20, 0),
        SPEEDY_GREATROOT(7.0d, 12, 0),
        SUPER_GREATROOT(12.0d, 30, 0);


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

        public int getChargeTime(){
            return this.returnTime / 2;
        }

        public float getAttackTime(){
            return Math.round(10f * 20f / (float)this.getReturnTime()) / 10f - 4f;
        }

        public int getBurnTime(){
            return this.burnTime;
        }
    }
}
