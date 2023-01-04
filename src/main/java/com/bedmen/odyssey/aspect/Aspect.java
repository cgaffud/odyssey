package com.bedmen.odyssey.aspect;

import net.minecraft.network.chat.MutableComponent;

public abstract class Aspect {
    public final String id;

    protected Aspect(String id){
        this.id = id;
    }

    public abstract MutableComponent getMutableComponent(Object... args);
}
