package com.bedmen.odyssey.aspect.aspect_objects;

import net.minecraft.world.damagesource.DamageSource;

import java.util.function.Predicate;

public class DamageSourcePredicateAspect extends FloatAspect {
    public final Predicate<DamageSource> damageSourcePredicate;
    protected DamageSourcePredicateAspect(String id, Predicate<DamageSource> damageSourcePredicate) {
        super(id, AspectTooltipFunctions.NUMBER_ADDITION);
        this.damageSourcePredicate = damageSourcePredicate;
    }

    protected DamageSourcePredicateAspect(String id, AspectTooltipFunction aspectTooltipFunction, Predicate<DamageSource> damageSourcePredicate) {
        super(id, aspectTooltipFunction);
        this.damageSourcePredicate = damageSourcePredicate;
    }
}
