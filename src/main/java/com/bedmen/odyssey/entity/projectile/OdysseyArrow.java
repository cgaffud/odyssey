package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.items.aspect_items.AspectBowItem;
import com.bedmen.odyssey.items.aspect_items.AspectCrossbowItem;
import com.bedmen.odyssey.items.aspect_items.QuiverItem;
import com.bedmen.odyssey.registry.EntityTypeRegistry;
import com.bedmen.odyssey.combat.ArrowType;
import com.bedmen.odyssey.combat.WeaponUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.util.Optional;

public class OdysseyArrow extends OdysseyAbstractArrow implements IEntityAdditionalSpawnData {
    public static final String ARROW_TYPE_TAG = "ArrowType";
    private ArrowType arrowType = ArrowType.AMETHYST;

    public OdysseyArrow(EntityType<? extends OdysseyArrow> entityType, Level level) {
        super(entityType, level);
    }

    public OdysseyArrow(Level level, LivingEntity livingEntity, ArrowType arrowType) {
        super(EntityTypeRegistry.ARROW.get(), livingEntity, level);
        this.arrowType = arrowType;
        this.setBaseDamage(arrowType.damage);
    }

    public OdysseyArrow(Level level, double x, double y, double z) {
        super(EntityTypeRegistry.ARROW.get(), x, y, z, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(this.getArrowType().getItem());
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains(ARROW_TYPE_TAG)) {
            this.arrowType = ArrowType.valueOf(compoundTag.getString(ARROW_TYPE_TAG));
        }
        this.setBaseDamage(this.getArrowType().damage);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putString(ARROW_TYPE_TAG, this.arrowType.name());
    }

    public ArrowType getArrowType(){
        return this.arrowType;
    }

    protected SoundEvent getEntityHitSoundEvent(){
        return SoundEvents.ARROW_HIT;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.arrowType.ordinal());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.arrowType = ArrowType.values()[additionalData.readInt()];
    }

    public void setEnchantmentEffectsFromEntity(LivingEntity shooter, float bowDamageMultiplier) {
        this.setBaseDamage((bowDamageMultiplier + this.random.nextGaussian() * 0.1D + (double)((float)this.level.getDifficulty().getId() * 0.055F)) * this.arrowType.damage * (WeaponUtil.BASE_ARROW_VELOCITY * WeaponUtil.BASE_ARROW_VELOCITY) / (WeaponUtil.BASE_ARROW_VELOCITY_ENEMIES * WeaponUtil.BASE_ARROW_VELOCITY_ENEMIES));
    }

    protected double getDamage(){
        double velocity = this.getDeltaMovement().length();
        double velocityFactor = velocity / WeaponUtil.BASE_ARROW_VELOCITY;
        return Mth.clamp(velocityFactor * velocityFactor * this.getBaseDamage(), 0.0D, 2.147483647E9D);
    }

    protected void onFinalPierce(){
        this.discard();
    }

    protected void onHurt(Entity target, boolean hurtSuccessful) {
        if(hurtSuccessful){
            if (target instanceof LivingEntity livingEntity) {
                Entity owner = this.getOwner();

                if (!this.level.isClientSide && this.getPierceLevel() <= 0) {
                    livingEntity.setArrowCount(livingEntity.getArrowCount() + 1);
                }

                if (livingEntity != owner && livingEntity instanceof Player && owner instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)owner).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
            }
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            }
        }
    }
}