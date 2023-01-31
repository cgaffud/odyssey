package com.bedmen.odyssey.aspect.aspect_objects;

import net.minecraft.network.chat.TranslatableComponent;

public class GlideAspect extends IntegerAspect implements ActivationAspect {
    protected static final TranslatableComponent ACTIVATION_KEY = new TranslatableComponent("key.jump");

    protected GlideAspect(String id) {
        super(id, AspectTooltipFunctions.GLIDE);
    }

    public TranslatableComponent getKey() {
        return ACTIVATION_KEY;
    }
}
