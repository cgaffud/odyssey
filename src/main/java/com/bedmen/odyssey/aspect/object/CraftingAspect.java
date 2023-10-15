package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import com.bedmen.odyssey.registry.ItemRegistry;

public class CraftingAspect extends IntegerAspect {

    protected CraftingAspect(String id, float weight){
        super(id, weight, AspectTooltipFunctions.NUMBER_ADDITION, item -> item == ItemRegistry.ABSTRACT_CRAFTER.get(), false, false);
    }
}
