package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.effect.TemperatureEffect;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OdysseyHusk extends Zombie {
    public OdysseyHusk(EntityType<? extends OdysseyHusk> entityType,  Level level) {
        super(entityType, level);
    }

    public boolean doHurtTarget(Entity target) {
        boolean doHurtTargetResult = super.doHurtTarget(target);
        if (doHurtTargetResult && this.getMainHandItem().isEmpty() && target instanceof LivingEntity livingTarget) {
            float effectiveDifficulty = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            livingTarget.addEffect(TemperatureEffect.getTemperatureEffectInstance(EffectRegistry.ROASTING.get(), 140 * (int)effectiveDifficulty, 0, false), this);
        }
        return doHurtTargetResult;
    }

    protected boolean isSunSensitive() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.HUSK_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.HUSK_STEP;
    }

    protected boolean convertsInWater() {
        return true;
    }

    protected void doUnderWaterConversion() {
        this.convertToZombieType(EntityType.ZOMBIE);
        if (!this.isSilent()) {
            this.level.levelEvent(null, 1041, this.blockPosition(), 0);
        }

    }

    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}
