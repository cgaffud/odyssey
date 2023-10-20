package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class DamageSourcePredicateAspect extends FloatAspect {
    public final Predicate<DamageSource> damageSourcePredicate;
    // Protections
    protected DamageSourcePredicateAspect(String id, float weight, Predicate<Item> itemPredicate, boolean isBuff, Predicate<DamageSource> damageSourcePredicate) {
        this(id, weight, AspectTooltipFunctions.FLOAT_ADDITION, itemPredicate, isBuff, damageSourcePredicate);
    }

    protected DamageSourcePredicateAspect(String id, float weight, AspectTooltipFunction<Float> aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff, Predicate<DamageSource> damageSourcePredicate) {
        super(id, weight, aspectTooltipFunction, itemPredicate, isBuff);
        this.damageSourcePredicate = damageSourcePredicate;
    }
}
