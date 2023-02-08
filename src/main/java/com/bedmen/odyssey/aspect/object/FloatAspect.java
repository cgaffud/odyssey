package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class FloatAspect extends Aspect {
    protected FloatAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate){
        super(id, weight, aspectTooltipFunction, itemPredicate);
    }
}
