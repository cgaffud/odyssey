package com.bedmen.odyssey.aspect;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Function;

public class Aspect {
    public final String id;
    public final Function<Float, MutableComponent> mutableComponentFunction;

    protected Aspect(String id) {
        this(id, strength -> new TranslatableComponent("aspect.oddc."+id, strength));
    }

    protected Aspect(String id, Function<Float, MutableComponent> mutableComponentFunction){
        this.id = id;
        this.mutableComponentFunction = mutableComponentFunction;
        Aspects.ASPECT_REGISTER.put(id, this);
    }
}
