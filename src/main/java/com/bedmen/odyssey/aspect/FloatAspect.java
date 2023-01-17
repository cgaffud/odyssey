package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Function;

public class FloatAspect extends Aspect {
    protected FloatAspect(String id) {
        super(id, f -> new TranslatableComponent("aspect.oddc."+id, StringUtil.floatFormat(f)));
    }

    protected FloatAspect(String id, Function<Float, MutableComponent> mutableComponentFunction){
        super(id, mutableComponentFunction);
    }
}
