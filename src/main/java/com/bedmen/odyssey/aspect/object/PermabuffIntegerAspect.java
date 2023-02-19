package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;

public class PermabuffIntegerAspect extends IntegerAspect implements PermabuffAspect {
    protected PermabuffIntegerAspect(String id, AspectTooltipFunction aspectTooltipFunction) {
        super(id, 0.0f, aspectTooltipFunction, AspectItemPredicates.NONE, false);
    }
}
