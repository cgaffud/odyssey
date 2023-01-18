package com.bedmen.odyssey.aspect.aspect_objects;

import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class TargetConditionalAspect extends FloatAspect {
    public final Predicate<LivingEntity> livingEntityPredicate;

    protected TargetConditionalAspect(String id, Predicate<LivingEntity> livingEntityPredicate) {
        super(id);
        this.livingEntityPredicate = livingEntityPredicate;
    }
}