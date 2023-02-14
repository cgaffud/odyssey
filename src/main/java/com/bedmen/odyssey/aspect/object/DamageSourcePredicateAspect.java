package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class DamageSourcePredicateAspect extends FloatAspect {
    public final Predicate<DamageSource> damageSourcePredicate;
    // Protections
    protected DamageSourcePredicateAspect(String id, float weight, Predicate<Item> itemPredicate, Predicate<DamageSource> damageSourcePredicate) {
        this(id, weight, AspectTooltipFunctions.NUMBER_ADDITION, itemPredicate, damageSourcePredicate);
    }

    protected DamageSourcePredicateAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate, Predicate<DamageSource> damageSourcePredicate) {
        super(id, weight, aspectTooltipFunction, itemPredicate);
        this.damageSourcePredicate = damageSourcePredicate;
    }
}
