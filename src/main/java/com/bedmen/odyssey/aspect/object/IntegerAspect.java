package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class IntegerAspect extends Aspect<Integer> {
    public final boolean hasInfusionPenalty;

    // Buff constructor
    protected IntegerAspect(String id, AspectTooltipFunction aspectTooltipFunction) {
        this(id, 0.0f, aspectTooltipFunction, AspectItemPredicates.NONE, true, false);
    }
    protected IntegerAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff, boolean hasInfusionPenalty){
        super(id, weight, aspectTooltipFunction, itemPredicate, isBuff);
        this.hasInfusionPenalty = hasInfusionPenalty;
    }

    public AspectInstance generateInstanceWithModifiability(float modifiability){
        if(this.weight <= 0.0f){
            return new AspectInstance(this, 1);
        }
        return new AspectInstance(this, Integer.max(1, (int)(modifiability / this.weight)));
    }

    public Integer castStrength(float strength){
        return (int)strength;
    }
}
