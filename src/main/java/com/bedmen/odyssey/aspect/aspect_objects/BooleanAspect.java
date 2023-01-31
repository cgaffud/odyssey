package com.bedmen.odyssey.aspect.aspect_objects;

public class BooleanAspect extends Aspect {
    protected BooleanAspect(String id) {
        this(id, AspectTooltipFunctions.NAME);
    }

    protected BooleanAspect(String id, AspectTooltipFunction aspectTooltipFunction) {
        super(id, aspectTooltipFunction);
    }
}
