package com.bedmen.odyssey.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

import javax.annotation.Nullable;

public class OdysseyDamageSource {
    public static DamageSource boomerang(Entity p_203096_0_, @Nullable Entity p_203096_1_) {
        return (new IndirectEntityDamageSource("boomerang", p_203096_0_, p_203096_1_)).setProjectile();
    }
}
