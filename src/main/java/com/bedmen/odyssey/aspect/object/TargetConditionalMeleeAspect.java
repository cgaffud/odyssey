package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class TargetConditionalMeleeAspect extends BonusDamageAspect {
    public final Predicate<LivingEntity> livingEntityPredicate;

    protected TargetConditionalMeleeAspect(String id, Predicate<LivingEntity> livingEntityPredicate) {
        super(id, AspectItemPredicates.MELEE);
        this.livingEntityPredicate = livingEntityPredicate;
    }
}