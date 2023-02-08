package com.bedmen.odyssey.entity.projectile;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.encapsulator.AspectStrengthMap;
import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.object.Aspects;
import com.bedmen.odyssey.entity.monster.Weaver;
import com.bedmen.odyssey.network.datasync.OdysseyDataSerializers;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

import java.util.Arrays;

public abstract class OdysseyAbstractArrow extends AbstractArrow {
    private static final EntityDataAccessor<AspectStrengthMap> DATA_ASPECT_STRENGTH_MAP = SynchedEntityData.defineId(OdysseyAbstractArrow.class, OdysseyDataSerializers.ASPECT_STRENGTH_MAP);
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
        this.entityData.define(DATA_ASPECT_STRENGTH_MAP, new AspectStrengthMap());
    }

    public void updatePiercingValues(){
        float strength = this.getAspectStrength(Aspects.PIERCING);
        int ceil = Mth.ceil(strength);
        this.piercingDamagePenalty = 1.0f - ((float)ceil) + strength;
        this.setPierceLevel((byte)ceil);
    }

    public AspectStrengthMap getAspectStrengthMap(){
        return this.entityData.get(DATA_ASPECT_STRENGTH_MAP).copy();
    }

    public void setAspectStrengthMap(AspectStrengthMap aspectStrengthMap){
        this.entityData.set(DATA_ASPECT_STRENGTH_MAP, aspectStrengthMap.copy());
        if(aspectStrengthMap.containsKey(Aspects.PIERCING)){
            this.updatePiercingValues();
        }
    }

    public void addAspectStrengthMap(AspectStrengthMap aspectStrengthMap){
        AspectStrengthMap newAspectStrengthMap = this.getAspectStrengthMap().combine(aspectStrengthMap);
        this.setAspectStrengthMap(newAspectStrengthMap);
    }

    public float getAspectStrength(Aspect aspect){
        return this.getAspectStrengthMap().get(aspect);
    }

    public boolean hasAspect(Aspect aspect){
        return getAspectStrength(aspect) > 0.0f;
    }

    protected abstract double getDamage();

    protected abstract void onFinalPierce();

    protected abstract void onHurt(Entity target, boolean hurtSuccessful);

    protected abstract SoundEvent getEntityHitSoundEvent();

    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();

        if (this.piercingIgnoreEntityIds == null) {
            this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
        }

        if (this.piercedAndKilledEntities == null) {
            this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
        }

        this.piercingIgnoreEntityIds.add(target.getId());

        boolean finalPierce = this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1;

        double damage = this.getDamage();
        if(finalPierce){
            damage *= this.piercingDamagePenalty;
        }

        Entity owner = this.getOwner();
        DamageSource damagesource;
        if (owner == null) {
            damagesource = DamageSource.arrow(this, this);
        } else {
            damagesource = DamageSource.arrow(this, owner);
            if (owner instanceof LivingEntity) {
                ((LivingEntity)owner).setLastHurtMob(target);
            }
        }

        boolean targetIsEnderman = target.getType() == EntityType.ENDERMAN;
        int k = target.getRemainingFireTicks();
        if (this.isOnFire() && !targetIsEnderman) {
            target.setSecondsOnFire(5);
        }
        if (target.hurt(damagesource, (float)damage)) {
            if (targetIsEnderman) {
                return;
            }
            if (target instanceof LivingEntity livingEntity) {

                if (!this.level.isClientSide && owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingEntity);
                }

                this.doPostHurtEffects(livingEntity);

                if (!target.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingEntity);
                }

                if (!this.level.isClientSide && owner instanceof ServerPlayer) {
                    ServerPlayer serverplayerentity = (ServerPlayer)owner;
                    if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, this.piercedAndKilledEntities);
                    } else if (!target.isAlive() && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayerentity, Arrays.asList(target));
                    }
                }

                // Poison Damage
                int poisonStrength = (int)this.getAspectStrength(Aspects.PROJECTILE_POISON_DAMAGE);
                if(!target.level.isClientSide && poisonStrength > 0) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 10 + 24, 0));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 10 + (12 * poisonStrength), 1));
                }
                // Cobweb Chance
                float cobwebChance = this.getAspectStrength(Aspects.PROJECTILE_COBWEB_CHANCE);
                if(!(this instanceof OdysseyArrow) || this.isCritArrow()){
                    Weaver.tryPlaceCobwebOnTarget(cobwebChance, livingEntity);
                }
            }

            this.onHurt(target, true);

            this.playSound(this.getEntityHitSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (finalPierce) {
                this.onFinalPierce();
            }
        } else {
            target.setRemainingFireTicks(k);
            this.onHurt(target, false);
        }

    }

    protected float getWaterInertia() {
        return this.hasAspect(Aspects.HYDRODYNAMIC) ? 0.99F : super.getWaterInertia();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        CompoundTag aspectStrengthMapTag = this.getAspectStrengthMap().toCompoundTag();
        compoundTag.put(ASPECT_STRENGTH_MAP_TAG, aspectStrengthMapTag);
        compoundTag.putFloat(PIERCING_DAMAGE_PENALTY_TAG, this.piercingDamagePenalty);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.contains(ASPECT_STRENGTH_MAP_TAG)){
            this.setAspectStrengthMap(AspectStrengthMap.fromCompoundTag(compoundTag.getCompound(ASPECT_STRENGTH_MAP_TAG)));
        }
        this.piercingDamagePenalty = compoundTag.contains(PIERCING_DAMAGE_PENALTY_TAG) ? compoundTag.getFloat(PIERCING_DAMAGE_PENALTY_TAG) : 1.0f;
    }
}
