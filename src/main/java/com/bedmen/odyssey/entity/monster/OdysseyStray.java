package com.bedmen.odyssey.entity.monster;

import java.util.Random;
import java.util.function.Predicate;

import com.bedmen.odyssey.effect.TemperatureEffect;
import com.bedmen.odyssey.items.aspect_items.AspectArrowItem;
import com.bedmen.odyssey.registry.EffectRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class OdysseyStray extends OdysseyAbstractSkeleton {
    public OdysseyStray(EntityType<? extends OdysseyStray> entityType, Level level) {
        super(entityType, level);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33850_) {
        return SoundEvents.STRAY_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.STRAY_STEP;
    }

    public ItemStack getProjectile(ItemStack bow) {
        return new ItemStack(ItemRegistry.FROST_ARROW.get());
    }
}