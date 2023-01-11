package com.bedmen.odyssey.modifier;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Function;

public class Modifier {
    public final String id;
    public final Function<Float, MutableComponent> mutableComponentFunction;

    protected Modifier(String id) {
        this(id, f -> new TranslatableComponent("modifier.oddc."+id, StringUtil.floatFormat(f)));
    }

    protected Modifier(String id, Function<Float, MutableComponent> mutableComponentFunction){
        this.id = id;
        this.mutableComponentFunction = mutableComponentFunction;
        Modifiers.modifierRegister.put(id, this);
    }
}
