package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class IntegerAspect extends Aspect {
    public final boolean hasInfusionPenalty;
    protected IntegerAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate, boolean hasInfusionPenalty){
        super(id, weight, aspectTooltipFunction, itemPredicate);
        this.hasInfusionPenalty = hasInfusionPenalty;
    }

    public AspectInstance generateInstanceWithModifiability(Item item, float modifiability){
        float weight = this.getWeight(item);
        if(weight <= 0.0f){
            return new AspectInstance(this, 1);
        }
        return new AspectInstance(this, Integer.max(1, (int)(modifiability / weight)));
    }
}
