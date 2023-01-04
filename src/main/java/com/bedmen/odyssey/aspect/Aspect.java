package com.bedmen.odyssey.aspect;

import net.minecraft.network.chat.MutableComponent;

public abstract class Aspect {
    public final String name;

    protected Aspect(String name){
        this.name = name;
    }

    public abstract MutableComponent getMutableComponent(Object... args);
}
