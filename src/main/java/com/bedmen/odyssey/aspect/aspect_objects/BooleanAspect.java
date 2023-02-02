package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.AspectTooltipFunctions;
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
