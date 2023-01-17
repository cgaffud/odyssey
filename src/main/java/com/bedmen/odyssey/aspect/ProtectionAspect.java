package com.bedmen.odyssey.aspect;

import net.minecraft.world.damagesource.DamageSource;

import java.util.function.Predicate;

public class ProtectionAspect extends FloatAspect {
    public final Predicate<DamageSource> damageSourcePredicate;
    protected ProtectionAspect(String id, Predicate<DamageSource> damageSourcePredicate) {
        super(id);
        this.damageSourcePredicate = damageSourcePredicate;
    }
}
