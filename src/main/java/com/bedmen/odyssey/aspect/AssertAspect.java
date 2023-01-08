package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class AssertAspect extends Aspect {
    protected AssertAspect(String id) {
        super(id);
    }

    public MutableComponent getMutableComponent(Object... args){
        return new TextComponent(StringUtil.floatFormat((Float) args[0])+" ").append(new TranslatableComponent("aspect.oddc."+this.id));
    }
}
