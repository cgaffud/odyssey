package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;

public class FloatAspect extends Aspect {
    protected FloatAspect(String id) {
        super(id, (strength, optionalLevel) -> new TranslatableComponent("aspect.oddc."+id, StringUtil.floatFormat(strength)));
    }

    protected FloatAspect(String id, AspectTooltipFunction aspectTooltipFunction){
        super(id, aspectTooltipFunction);
    }
}
