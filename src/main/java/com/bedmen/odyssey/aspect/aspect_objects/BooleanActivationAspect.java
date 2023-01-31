package com.bedmen.odyssey.aspect.aspect_objects;

import net.minecraft.network.chat.TranslatableComponent;

public class BooleanActivationAspect extends BooleanAspect implements ActivationAspect {

    private final TranslatableComponent key;

    protected BooleanActivationAspect(String id, String key) {
        super(id, AspectTooltipFunctions.NAME_AND_ACTIVATION_KEY);
        this.key = new TranslatableComponent(key);
    }

    @Override
    public TranslatableComponent getKey() {
        return this.key;
    }
}
