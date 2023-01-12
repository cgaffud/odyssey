package com.bedmen.odyssey.modifier;

import net.minecraft.world.damagesource.DamageSource;

import java.util.function.Predicate;

public class ProtectionModifier extends FloatModifier {
    public final Predicate<DamageSource> damageSourcePredicate;
    protected ProtectionModifier(String id, Predicate<DamageSource> damageSourcePredicate) {
        super(id);
        this.damageSourcePredicate = damageSourcePredicate;
    }
}
