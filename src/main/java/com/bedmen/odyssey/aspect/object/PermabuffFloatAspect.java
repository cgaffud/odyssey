package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;

public class PermabuffFloatAspect extends FloatAspect implements PermabuffAspect{
    protected PermabuffFloatAspect(String id, AspectTooltipFunction aspectTooltipFunction) {
        super(id, 0.0f, aspectTooltipFunction, AspectItemPredicates.NONE);
    }
}
