package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class DamageSourcePredicateAspect extends FloatAspect {
    public final Predicate<DamageSource> damageSourcePredicate;
    // Protections
    protected DamageSourcePredicateAspect(String id, Predicate<DamageSource> damageSourcePredicate, Predicate<Item> itemPredicate) {
        this(id, 1.0f, AspectTooltipFunctions.NUMBER_ADDITION, damageSourcePredicate, itemPredicate);
    }

    protected DamageSourcePredicateAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<DamageSource> damageSourcePredicate, Predicate<Item> itemPredicate) {
        super(id, weight, aspectTooltipFunction, itemPredicate);
        this.damageSourcePredicate = damageSourcePredicate;
    }
}
