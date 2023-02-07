package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class BooleanAspect extends Aspect {
    protected BooleanAspect(String id, Predicate<Item> itemPredicate){
        this(id, 1.0f, AspectTooltipFunctions.NAME, itemPredicate);
    }

    protected BooleanAspect(String id, float weight, Predicate<Item> itemPredicate){
        this(id, weight, AspectTooltipFunctions.NAME, itemPredicate);
    }


    protected BooleanAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate){
        super(id, weight, aspectTooltipFunction, itemPredicate);
    }
}