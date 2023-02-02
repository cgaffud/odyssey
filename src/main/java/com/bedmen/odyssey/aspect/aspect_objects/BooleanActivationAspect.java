package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.AspectTooltipFunctions;
import net.minecraft.network.chat.TranslatableComponent;

public class BooleanActivationAspect extends BooleanAspect implements ActivationAspect {

    private final TranslatableComponent key;

    protected BooleanActivationAspect(String id, String key) {
        super(id, AspectTooltipFunctions.NAME_AND_ACTIVATION_KEY, AspectItemPredicates.NONE);
        this.key = new TranslatableComponent(key);
    }

    public TranslatableComponent getKey() {
        return this.key;
    }
}
