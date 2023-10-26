package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.world.item.Item;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ComparableAspect<T> extends Aspect<T> {
    protected ComparableAspect(String id, Function<T, Float> weightFunction, AspectTooltipFunction<T> aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff) {
        super(id, weightFunction, aspectTooltipFunction, itemPredicate, isBuff);
    }

    public abstract Comparator<T> getComparator();
}
