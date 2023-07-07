package com.bedmen.odyssey.entity.monster;

import com.bedmen.odyssey.effect.TemperatureEffect;
import com.bedmen.odyssey.registry.EffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Random;

public class OdysseyHusk extends Zombie {
    public OdysseyHusk(EntityType<? extends OdysseyHusk> entityType,  Level level) {
        super(entityType, level);
    }

    public boolean doHurtTarget(Entity target) {
        boolean doHurtTargetResult = super.doHurtTarget(target);
        if (doHurtTargetResult && this.getMainHandItem().isEmpty() && target instanceof LivingEntity livingTarget) {
            float effectiveDifficulty = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            livingTarget.addEffect(new MobEffectInstance(EffectRegistry.HUSK_DRYING.get(), 140 * (int)effectiveDifficulty), this);
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

    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        return 0.5F;
    }

    public static boolean spawnPredicate(EntityType<OdysseyHusk> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return checkAnyLightMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) && (mobSpawnType == MobSpawnType.SPAWNER || serverLevelAccessor.canSeeSky(blockPos));
    }
}
