package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Function;

public class Aspect {
    public final String id;
    public final Function<AspectInstance, MutableComponent> mutableComponentFunction;

    protected Aspect(String id){
        this.id = id;
        this.mutableComponentFunction = aspectInstance -> new TranslatableComponent("aspect.oddc."+this.id, StringUtil.floatFormat(aspectInstance.strength));
    }

    protected Aspect(String id, Function<AspectInstance, MutableComponent> mutableComponentFunction){
        this.id = id;
        this.mutableComponentFunction = mutableComponentFunction;
    }
}
