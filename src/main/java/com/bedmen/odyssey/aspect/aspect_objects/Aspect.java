package com.bedmen.odyssey.aspect.aspect_objects;

import net.minecraft.network.chat.TranslatableComponent;

public class Aspect {
    public final String id;
    public final AspectTooltipFunction aspectTooltipFunction;

    protected Aspect(String id, AspectTooltipFunction aspectTooltipFunction){
        this.id = id;
        this.aspectTooltipFunction = aspectTooltipFunction;
        Aspects.ASPECT_REGISTER.put(id, this);
    }

    public TranslatableComponent getName(){
        return new TranslatableComponent("aspect.oddc."+this.id);
    }

}
