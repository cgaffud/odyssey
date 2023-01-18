package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.aspect_objects.FloatAspect;
import net.minecraft.world.damagesource.DamageSource;

import java.util.function.Predicate;

public class DamageSourcePredicateAspect extends FloatAspect {
    public final Predicate<DamageSource> damageSourcePredicate;
    protected DamageSourcePredicateAspect(String id, Predicate<DamageSource> damageSourcePredicate) {
        super(id);
        this.damageSourcePredicate = damageSourcePredicate;
    }

    protected DamageSourcePredicateAspect(String id, AspectTooltipFunction aspectTooltipFunction, Predicate<DamageSource> damageSourcePredicate) {
        super(id, aspectTooltipFunction);
        this.damageSourcePredicate = damageSourcePredicate;
    }
}
