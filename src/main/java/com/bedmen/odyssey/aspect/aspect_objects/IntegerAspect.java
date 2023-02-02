package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectTooltipFunction;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class IntegerAspect extends Aspect {
    protected IntegerAspect(String id, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate){
        super(id, aspectTooltipFunction, itemPredicate);
    }
}
