package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class BooleanAspect extends Aspect<Boolean> {

    protected BooleanAspect(String id){
        this(id, 1.0f, AspectTooltipFunctions.NAME, AspectItemPredicates.NONE);
    }
    protected BooleanAspect(String id, Predicate<Item> itemPredicate){
        this(id, 1.0f, AspectTooltipFunctions.NAME, itemPredicate);
    }

    protected BooleanAspect(String id, float weight, Predicate<Item> itemPredicate){
        this(id, weight, AspectTooltipFunctions.NAME, itemPredicate);
    }

    protected BooleanAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate){
        super(id, weight, aspectTooltipFunction, itemPredicate);
    }

    public AspectInstance generateInstanceWithModifiability(Item item, float modifiability){
        return new AspectInstance(this);
    }

    public Boolean castStrength(float strength){
        return strength > 0f;
    }
}
