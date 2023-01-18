package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import net.minecraft.network.chat.TranslatableComponent;

public class IntegerAspect extends Aspect {
    protected IntegerAspect(String id) {
        super(id, (strength, optionalLevel) -> new TranslatableComponent("aspect.oddc."+id, (int)strength));
    }

    protected IntegerAspect(String id, AspectTooltipFunction aspectTooltipFunction){
        super(id, aspectTooltipFunction);
    }
}
