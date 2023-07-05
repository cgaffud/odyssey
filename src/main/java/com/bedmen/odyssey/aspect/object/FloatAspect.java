package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class FloatAspect extends Aspect<Float> {

    protected FloatAspect(String id, AspectTooltipFunction aspectTooltipFunction) {
        this(id, 0.0f, aspectTooltipFunction, AspectItemPredicates.NONE);
    }
    protected FloatAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate){
        super(id, weight, aspectTooltipFunction, itemPredicate);
    }
    public Float castStrength(float strength){
        return strength;
    }
}
