package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.combat.CombatUtil;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.Arrays;

public abstract class OdysseyAbstractArrow extends AbstractArrow {
    public static final String KNOCKBACK_MODIFIER_TAG = "KnockbackModifier";
    public static final String PIERCING_DAMAGE_PENALTY_TAG = "PiercingDamagePenalty";
    public static final String LOOTING_MODIFIER_TAG = "LootingModifier";
    public static final String LARCENY_MODIFIER_TAG = "LarcenyModifier";
    public float knockbackModifier = 1.0f;
    // Decreases damage of arrow on last piercing
    public float piercingDamagePenalty = 1.0f;
    public int lootingModifier = 0;
    public float larcenyModifier = 0.0f;

    protected OdysseyAbstractArrow(EntityType<? extends OdysseyAbstractArrow> type, Level level) {
        super(type, level);
    }

    protected OdysseyAbstractArrow(EntityType<? extends OdysseyAbstractArrow> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    protected OdysseyAbstractArrow(EntityType<? extends OdysseyAbstractArrow> type, LivingEntity livingEntity, Level level) {
        super(type, livingEntity, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void setPiercingModifier(float piercingModifier){
        int ceil = Mth.ceil(piercingModifier);
        this.piercingDamagePenalty = 1.0f - ((float)ceil) + piercingModifier;
        this.setPierceLevel((byte)ceil);
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        double velocity = this.getDeltaMovement().length();
        double velocityFactor = velocity / CombatUtil.BASE_ARROW_VELOCITY ;
        double damage = Mth.clamp(velocityFactor * velocityFactor * this.getBaseDamage(), 0.0D, 2.147483647E9D);
        if (this.getPierceLevel() > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if(this.piercingIgnoreEntityIds.size() >= this.getPierceLevel()){
                damage *= this.piercingDamagePenalty;
            }

            if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
                this.discard();
                return;
            }

            this.piercingIgnoreEntityIds.add(entity.getId());
        }

        Entity owner = this.getOwner();
        DamageSource damagesource;
        if (owner == null) {
            damagesource = DamageSource.arrow(this, this);
        } else {
            damagesource = DamageSource.arrow(this, owner);
            if (owner instanceof LivingEntity) {
                ((LivingEntity)owner).setLastHurtMob(entity);
            }
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int k = entity.getRemainingFireTicks();
        if (this.isOnFire() && !flag) {
            entity.setSecondsOnFire(5);
        }

        if (entity.hurt(damagesource, (float)damage)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                if (!this.level.isClientSide && this.getPierceLevel() <= 0) {
                    livingentity.setArrowCount(livingentity.getArrowCount() + 1);
                }

                if (!this.level.isClientSide && owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingentity);
                }

                this.doPostHurtEffects(livingentity);
                if (owner != null && livingentity != owner && livingentity instanceof Player && owner instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)owner).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }

                if (!this.level.isClientSide && owner instanceof ServerPlayer) {
                    ServerPlayer serverplayerentity = (ServerPlayer)owner;
                    if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, this.piercedAndKilledEntities);
                    } else if (!entity.isAlive() && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, Arrays.asList(entity));
                    }
                }
            }

            this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            entity.setRemainingFireTicks(k);
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

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putFloat(KNOCKBACK_MODIFIER_TAG, this.knockbackModifier);
        compoundTag.putFloat(PIERCING_DAMAGE_PENALTY_TAG, this.piercingDamagePenalty);
        compoundTag.putInt(LOOTING_MODIFIER_TAG, this.lootingModifier);
        compoundTag.putFloat(LARCENY_MODIFIER_TAG, this.larcenyModifier);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.knockbackModifier = compoundTag.contains(KNOCKBACK_MODIFIER_TAG) ? compoundTag.getFloat(KNOCKBACK_MODIFIER_TAG) : 1.0f;
        this.piercingDamagePenalty = compoundTag.contains(PIERCING_DAMAGE_PENALTY_TAG) ? compoundTag.getFloat(PIERCING_DAMAGE_PENALTY_TAG) : 1.0f;
        this.lootingModifier = compoundTag.getInt(LOOTING_MODIFIER_TAG);
        this.larcenyModifier = compoundTag.getFloat(LARCENY_MODIFIER_TAG);
    }
}
