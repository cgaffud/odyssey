package com.bedmen.odyssey.entity.boss;

import com.bedmen.odyssey.registry.EffectRegistry;
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

import javax.annotation.Nullable;
import java.util.function.Predicate;

public abstract class Boss extends Monster implements IBossEventEntity {
    private static final int DESPAWN_TIME = 2400;
    private int despawnTimer;
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
        if ((this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) || this.despawnTimer > DESPAWN_TIME) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected int decreaseAirSupply(int pAir) {
        return pAir;
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
        return mobEffectInstance.getEffect() == EffectRegistry.SHATTERED.get();
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        amount *= this.getDamageReduction();
        return super.hurt(damageSource, amount);
    }

    public void setTarget(@Nullable LivingEntity livingEntity) {
        if(livingEntity instanceof Boss){
            return;
        }
        super.setTarget(livingEntity);
    }
}
