package com.bedmen.odyssey.aspect.aspect_objects;

import net.minecraft.world.damagesource.DamageSource;

import java.util.function.Predicate;

public class ShieldDamageBlockAspect extends DamageSourcePredicateAspect {
    protected ShieldDamageBlockAspect(String id, Predicate<DamageSource> damageSourcePredicate) {
        super(id, AspectTooltipFunctions.ADDITIONAL_SHIELD_DAMAGE_BLOCK, damageSourcePredicate);
    }
}
