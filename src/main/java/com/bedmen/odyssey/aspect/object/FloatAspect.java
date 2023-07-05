package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class FloatAspect extends Aspect<Float> {

    // Buff constructor
    protected FloatAspect(String id, AspectTooltipFunction aspectTooltipFunction) {
        this(id, 0.0f, aspectTooltipFunction, AspectItemPredicates.NONE, true);
    }
    protected FloatAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff){
        super(id, weight, aspectTooltipFunction, itemPredicate, isBuff);
    }
    public Float castStrength(float strength){
        return strength;
    }
}
