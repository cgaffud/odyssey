package com.bedmen.odyssey.aspect;

import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;

public class TargetConditionalMeleeAspect extends Aspect {
    public final Predicate<LivingEntity> livingEntityPredicate;

    protected TargetConditionalMeleeAspect(String id, Predicate<LivingEntity> livingEntityPredicate) {
        super(id);
        this.livingEntityPredicate = livingEntityPredicate;
    }
}