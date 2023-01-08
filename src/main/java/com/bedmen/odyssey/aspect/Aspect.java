package com.bedmen.odyssey.aspect;

import net.minecraft.network.chat.MutableComponent;

import java.util.function.Function;

public class Aspect {
    public final String id;
    public final Function<AspectInstance, MutableComponent> mutableComponentFunction;

    protected Aspect(String id, Function<AspectInstance, MutableComponent> mutableComponentFunction){
        this.id = id;
        this.mutableComponentFunction = mutableComponentFunction;
    }
}
