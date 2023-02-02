package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.AspectTooltipFunctions;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class TargetConditionalMeleeAspect extends FloatAspect {
    public final Predicate<LivingEntity> livingEntityPredicate;

    protected TargetConditionalMeleeAspect(String id, Predicate<LivingEntity> livingEntityPredicate) {
        super(id, AspectTooltipFunctions.NUMBER_ADDITION, AspectItemPredicates.MELEE);
        this.livingEntityPredicate = livingEntityPredicate;
    }
}