package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class BooleanAspect extends Aspect {
    protected BooleanAspect(String id, Predicate<Item> itemPredicate){
        this(id, AspectTooltipFunctions.NAME, itemPredicate);
    }

    protected BooleanAspect(String id, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate){
        super(id, aspectTooltipFunction, itemPredicate);
    }
}
