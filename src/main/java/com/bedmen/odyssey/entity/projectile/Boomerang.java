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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
    private BoomerangType boomerangType = BoomerangType.WOODEN;
    private ItemStack thrownStack = new ItemStack(ItemRegistry.WOODEN_BOOMERANG.get());
    private boolean isMultishotClone = false;
    private boolean dealtDamage;
    public int returningTicks;
    private int despawnTicks;

    public Boomerang(EntityType<? extends Boomerang> type, Level level) {
        super(type, level);
    }

    public Boomerang(Level level, LivingEntity thrower, ItemStack thrownStackIn, boolean isMultishotClone) {
        super(EntityTypeRegistry.BOOMERANG.get(), thrower, level);
        this.thrownStack = thrownStackIn.copy();
        this.boomerangType = ((BoomerangItem)this.thrownStack.getItem()).getBoomerangType();
        this.isMultishotClone = isMultishotClone;
    }

    public Boomerang(Level p_i48791_1_, double p_i48791_2_, double p_i48791_4_, double p_i48791_6_) {
        super(EntityTypeRegistry.BOOMERANG.get(), p_i48791_2_, p_i48791_4_, p_i48791_6_, p_i48791_1_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        this.setNoGravity(true);
        if (this.inGroundTime > 0 || this.tickCount > ((BoomerangItem)this.thrownStack.getItem()).getTurnaroundTime(this.thrownStack)) {
            this.dealtDamage = true;
        }

        Entity owner = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && owner != null) {
            if (!this.isAcceptibleReturnOwner()) {
                despawn();
            } else {
                this.setNoPhysics(true);
                Vec3 vector3d = new Vec3(owner.getX() - this.getX(), owner.getEyeY() - this.getY(), owner.getZ() - this.getZ());
                double d0 = 0.04D * (double)this.boomerangType.getVelocity(this.thrownStack);
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(d0)));

                ++this.returningTicks;
            }
        } else if (owner == null && this.getDeltaMovement().length() < 0.01){
            this.despawnTicks++;
        }

        if(this.despawnTicks > 20){
            despawn();
        }

        float velocity = ((BoomerangItem)this.thrownStack.getItem()).getBoomerangType().getVelocity(this.thrownStack);
        int tickFrequency = Integer.max((int)(5f/velocity), 2);
        if (!this.level.isClientSide && this.tickCount % tickFrequency == 1) {
            this.level.playSound(null, this, SoundEvents.PLAYER_ATTACK_SWEEP, owner instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE, 1.0f, velocity);
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
        return this.isMultishotClone ? ItemStack.EMPTY : this.thrownStack.copy();
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        if(this.dealtDamage){
            if(!this.level.isClientSide){
                AABB box = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D);
                this.level.getEntities(this, box, this::isNonPlayerBoomerangOwner)
                        .stream().findFirst()
                        .ifPresent(owner -> {
                            if (!this.isMultishotClone) {
                                LivingEntity livingEntity = (LivingEntity)owner;
                                if(livingEntity.getMainHandItem().isEmpty()){
                                    livingEntity.setItemInHand(InteractionHand.MAIN_HAND, this.thrownStack);
                                } else if (livingEntity.getOffhandItem().isEmpty()){
                                    livingEntity.setItemInHand(InteractionHand.OFF_HAND, this.thrownStack);
                                }
                            }
                            this.discard();
                        });
            }
        } else if(!this.isNoPhysics()) {
            return super.findHitEntity(startVec, endVec);
        }
        return null;
    }

    private boolean isNonPlayerBoomerangOwner(Entity entity){
        if(this.getOwner() == null){
            return false;
        }
        return entity instanceof BoomerangAttackMob && entity instanceof LivingEntity && this.ownedBy(entity);
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (this.piercingIgnoreEntityIds == null) {
            this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
        }
        if (this.piercingIgnoreEntityIds.contains(entity.getId())) {
            return;
        }
        this.piercingIgnoreEntityIds.add(entity.getId());
        float f = (float)this.getBoomerangType().damage;
        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity;
            f += EnchantmentHelper.getDamageBonus(this.thrownStack, livingentity.getMobType());
        }
        Entity owner = this.getOwner();
        DamageSource damagesource = OdysseyDamageSource.boomerang(this, owner == null ? this : owner);
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

                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingEntity);
                }

                this.doPostHurtEffects(livingEntity);
            }
        }
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
        if(flag && this.isMultishotClone){
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
        if (compoundNBT.contains("BoomerangType")) {
            this.boomerangType = BoomerangType.valueOf(compoundNBT.getString("BoomerangType"));
        }
        this.isMultishotClone = compoundNBT.getBoolean("IsMultishot");
    }

    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.put("Boomerang", this.thrownStack.save(new CompoundTag()));
        compoundNBT.putBoolean("DealtDamage", this.dealtDamage);
        compoundNBT.putString("BoomerangType", this.boomerangType.name());
        compoundNBT.putBoolean("IsMultishot", this.isMultishotClone);
    }


    public BoomerangType getBoomerangType(){
        return this.boomerangType;
    }

    public ItemStack getThrownStack(){
        return this.thrownStack;
    }

    public void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED) {
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
        WOODEN(4.0d, 0.8f, 300),
        BONE(5.0d, 1.0f, 0),
        SPEEDY_BONE(5.0d, 1.2f, 0),
        BONERANG(5.0d, 1.2f, 0),
        CLOVER_STONE(6.0d, 1.0f, 0),
        GREATROOT(8.0d, 1.2f, 0),
        SPEEDY_GREATROOT(8.0d, 1.5f, 0),
        SUPER_GREATROOT(12.0d, 1.25f, 0);


        public final double damage;
        private final float velocity;
        public final int burnTime;

        BoomerangType(double damage, float velocity, int burnTime){
            this.damage = damage;
            this.velocity = velocity;
            this.burnTime = burnTime;
        }

        public float getVelocity(ItemStack boomerang) {
            return this.velocity * EnchantmentUtil.getSuperChargeMultiplier(boomerang);
        }
    }
}
