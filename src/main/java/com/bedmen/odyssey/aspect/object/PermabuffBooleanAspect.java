package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;

public class PermabuffBooleanAspect extends BooleanAspect implements PermabuffAspect {
    protected PermabuffBooleanAspect(String id) {
        super(id, 0.0f, AspectTooltipFunctions.NAME, AspectItemPredicates.NONE);
    }
}
