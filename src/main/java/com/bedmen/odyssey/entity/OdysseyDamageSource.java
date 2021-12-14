package com.bedmen.odyssey.entity;

import com.bedmen.odyssey.entity.projectile.Boomerang;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class OdysseyDamageSource {
    public static DamageSource boomerang(Boomerang boomerang, @Nullable Entity entity) {
        return (new IndirectEntityDamageSource("boomerang", boomerang, entity)).setProjectile();
    }
}
