package com.bedmen.odyssey.aspect;

import net.minecraft.network.chat.TranslatableComponent;

public class BooleanAspect extends Aspect {
    protected BooleanAspect(String id) {
        super(id, modifierInstance -> new TranslatableComponent("aspect.oddc."+id));
    }
}
