package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class IntegerAspect extends Aspect {
    protected IntegerAspect(String id, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate){
        super(id, aspectTooltipFunction, itemPredicate);
    }
}
