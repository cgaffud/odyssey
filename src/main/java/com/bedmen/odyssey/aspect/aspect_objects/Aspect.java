package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectTooltipFunction;
import net.minecraft.network.chat.TranslatableComponent;

public class Aspect {
    public final String id;
    public final AspectTooltipFunction aspectTooltipFunction;

    protected Aspect(String id) {
        this(id, (strength, optionalLevel) -> new TranslatableComponent("aspect.oddc."+id, strength));
    }

    protected Aspect(String id, AspectTooltipFunction aspectTooltipFunction){
        this.id = id;
        this.aspectTooltipFunction = aspectTooltipFunction;
        Aspects.ASPECT_REGISTER.put(id, this);
    }

    
}
