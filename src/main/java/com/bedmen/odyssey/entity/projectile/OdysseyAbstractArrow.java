package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.aspect.AspectStrengthMap;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import com.bedmen.odyssey.aspect.aspect_objects.Aspects;
import com.bedmen.odyssey.combat.WeaponUtil;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.Arrays;

public abstract class OdysseyAbstractArrow extends AbstractArrow {
    private AspectStrengthMap aspectStrengthMap = new AspectStrengthMap();
    public static final String ASPECT_STRENGTH_MAP_TAG = "AspectStrengthMap";
    public static final String PIERCING_DAMAGE_PENALTY_TAG = "PiercingDamagePenalty";
    // Decreases damage of arrow on last piercing
    public float piercingDamagePenalty = 1.0f;

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

    public void setPiercingValues(float strength){
        int ceil = Mth.ceil(strength);
        this.piercingDamagePenalty = 1.0f - ((float)ceil) + strength;
        this.setPierceLevel((byte)ceil);
    }

    public void setAspectStrength(Aspect aspect, float strength){
        this.aspectStrengthMap.put(aspect, strength);
        if(aspect == Aspects.PIERCING){
            this.setPiercingValues(strength);
        }
    }

    public float getAspectStrength(Aspect aspect){
        return this.aspectStrengthMap.get(aspect);
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        double velocity = this.getDeltaMovement().length();
        double velocityFactor = velocity / WeaponUtil.BASE_ARROW_VELOCITY ;
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

            if (entity instanceof LivingEntity livingEntity) {
                if (!this.level.isClientSide && this.getPierceLevel() <= 0) {
                    livingEntity.setArrowCount(livingEntity.getArrowCount() + 1);
                }

                if (!this.level.isClientSide && owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingEntity);
                }

                this.doPostHurtEffects(livingEntity);
                if (owner != null && livingEntity != owner && livingEntity instanceof Player && owner instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)owner).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingEntity);
                }

                if (!this.level.isClientSide && owner instanceof ServerPlayer) {
                    ServerPlayer serverplayerentity = (ServerPlayer)owner;
                    if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, this.piercedAndKilledEntities);
                    } else if (!entity.isAlive() && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, Arrays.asList(entity));
                    }
                }

                // Poison Damage
                int poisonStrength = (int)this.getAspectStrength(Aspects.PROJECTILE_POISON_DAMAGE);
                if(!entity.level.isClientSide && poisonStrength > 0) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 10 + 24, 0));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 10 + (12 * poisonStrength), 1));
                }
                // Cobweb Chance
                float cobwebChance = this.getAspectStrength(Aspects.PROJECTILE_COBWEB_CHANCE);
                if(cobwebChance > livingEntity.getRandom().nextFloat()){
                    BlockPos blockPos = new BlockPos(livingEntity.getPosition(1f));
                    if (livingEntity.level.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                        livingEntity.level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
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
        CompoundTag aspectStrengthMapTag = this.aspectStrengthMap.toCompoundTag();
        compoundTag.put(ASPECT_STRENGTH_MAP_TAG, aspectStrengthMapTag);
        compoundTag.putFloat(PIERCING_DAMAGE_PENALTY_TAG, this.piercingDamagePenalty);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.contains(ASPECT_STRENGTH_MAP_TAG)){
            this.aspectStrengthMap = AspectStrengthMap.fromCompoundTag(compoundTag.getCompound(ASPECT_STRENGTH_MAP_TAG));
        }
        this.piercingDamagePenalty = compoundTag.contains(PIERCING_DAMAGE_PENALTY_TAG) ? compoundTag.getFloat(PIERCING_DAMAGE_PENALTY_TAG) : 1.0f;
    }
}
