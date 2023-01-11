package com.bedmen.odyssey.modifier;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Function;

public class FloatModifier extends Modifier {
    protected FloatModifier(String id) {
        super(id, f -> new TranslatableComponent("modifier.oddc."+id, StringUtil.floatFormat(f)));
    }

    protected FloatModifier(String id, Function<Float, MutableComponent> mutableComponentFunction){
        super(id, mutableComponentFunction);
    }
}
