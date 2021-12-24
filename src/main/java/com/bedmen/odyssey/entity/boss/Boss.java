package com.bedmen.odyssey.entity.boss;

import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public abstract class Boss extends Monster implements IBossEventEntity {
    private int despawnTimer;
    protected static final Predicate<LivingEntity> ENTITY_SELECTOR = (entity) -> {
        return entity.attackable() && !(entity instanceof Monster);
    };
    protected Boss(EntityType<? extends Monster> p_i48576_1_, Level p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
    }

    public void tick(){
        super.tick();
        if(this.getTarget() == null){
            this.despawnTimer++;
        }
    }

    public void checkDespawn() {
        if ((this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) || this.despawnTimer > 1200) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    public boolean canChangeDimensions() {
        return false;
    }

    protected boolean canRide(Entity p_184228_1_) {
        return false;
    }

    public boolean canBeAffected(MobEffectInstance mobEffectInstance) {
        return false;
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        amount *= this.getDamageReduction();
        return super.hurt(damageSource, amount);
    }
}
