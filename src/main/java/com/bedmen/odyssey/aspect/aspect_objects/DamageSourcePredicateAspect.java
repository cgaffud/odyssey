package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.AspectTooltipFunctions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class DamageSourcePredicateAspect extends FloatAspect {
    public final Predicate<DamageSource> damageSourcePredicate;
    protected DamageSourcePredicateAspect(String id, Predicate<DamageSource> damageSourcePredicate, Predicate<Item> itemPredicate) {
        this(id, AspectTooltipFunctions.NUMBER_ADDITION, damageSourcePredicate, itemPredicate);
    }

    protected DamageSourcePredicateAspect(String id, AspectTooltipFunction aspectTooltipFunction, Predicate<DamageSource> damageSourcePredicate, Predicate<Item> itemPredicate) {
        super(id, aspectTooltipFunction, itemPredicate);
        this.damageSourcePredicate = damageSourcePredicate;
    }
}
