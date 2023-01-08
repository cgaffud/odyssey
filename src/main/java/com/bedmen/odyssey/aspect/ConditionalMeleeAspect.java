package com.bedmen.odyssey.aspect;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;
import java.util.function.Predicate;

public class ConditionalMeleeAspect extends Aspect {
    public final Predicate<LivingEntity> livingEntityPredicate;
    protected ConditionalMeleeAspect(String id, Predicate<LivingEntity> livingEntityPredicate){
        super(id);
        this.livingEntityPredicate = livingEntityPredicate;
    }
}
