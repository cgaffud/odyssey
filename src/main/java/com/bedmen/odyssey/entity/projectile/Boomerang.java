package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.combat.BoomerangType;
import com.bedmen.odyssey.entity.monster.BoomerangAttackMob;
import com.bedmen.odyssey.items.aspect_items.BoomerangItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

public class Boomerang extends OdysseyAbstractArrow implements IEntityAdditionalSpawnData {
    private static final int BOOMERANG_TURNAROUND_TIME = 10;
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

    protected double getDamage() {
        return this.boomerangType.damage * this.getBaseDamage();
    }

    protected void onFinalPierce() {
        this.dealtDamage = true;
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
    }

    protected void onSuccessfulHurt(Entity target) {

    }

    protected void onFailedHurt(Entity target) {
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        this.setNoGravity(true);
        if (this.inGroundTime > 0 || this.tickCount > BOOMERANG_TURNAROUND_TIME) {
            this.dealtDamage = true;
        }

        Entity owner = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && owner != null) {
            if (!this.isAcceptibleReturnOwner()) {
                despawn();
            } else {
                this.setNoPhysics(true);
                Vec3 vector3d = new Vec3(owner.getX() - this.getX(), owner.getEyeY() - this.getY(), owner.getZ() - this.getZ());
                float loyaltyStrength = Float.max(1.0f, this.getAspectStrength(Aspects.LOYALTY));
                System.out.println(loyaltyStrength);
                double returnSpeed = 0.04D * (double)this.boomerangType.velocity * loyaltyStrength;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(returnSpeed)));

                ++this.returningTicks;
            }
        } else if (owner == null && this.getDeltaMovement().length() < 0.01){
            this.despawnTicks++;
        }

        if(this.despawnTicks > 20){
            despawn();
        }

        float velocity = ((BoomerangItem)this.thrownStack.getItem()).getBoomerangType().velocity;
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

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("Boomerang", 10)) {
            this.thrownStack = ItemStack.of(compoundTag.getCompound("Boomerang"));
        }
        this.dealtDamage = compoundTag.getBoolean("DealtDamage");
        if (compoundTag.contains("BoomerangType")) {
            this.boomerangType = BoomerangType.valueOf(compoundTag.getString("BoomerangType"));
        }
        this.isMultishotClone = compoundTag.getBoolean("IsMultishot");
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.put("Boomerang", this.thrownStack.save(new CompoundTag()));
        compoundTag.putBoolean("DealtDamage", this.dealtDamage);
        compoundTag.putString("BoomerangType", this.boomerangType.name());
        compoundTag.putBoolean("IsMultishot", this.isMultishotClone);
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
}
