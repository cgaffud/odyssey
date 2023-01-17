package com.bedmen.odyssey.aspect;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Function;

public class IntegerAspect extends Aspect {
    protected IntegerAspect(String id) {
        super(id, f -> new TranslatableComponent("aspect.oddc."+id, f.intValue()));
    }

    protected IntegerAspect(String id, Function<Float, MutableComponent> mutableComponentFunction){
        super(id, mutableComponentFunction);
    }
}
