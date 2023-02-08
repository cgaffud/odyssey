package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.network.chat.TranslatableComponent;

public class BooleanActivationAspect extends BooleanAspect implements ActivationAspect {

    private final TranslatableComponent key;

    protected BooleanActivationAspect(String id, String key) {
        super(id, 0.0f, AspectTooltipFunctions.NAME_AND_ACTIVATION_KEY, AspectItemPredicates.NONE);
        this.key = new TranslatableComponent(key);
    }

    public TranslatableComponent getKey() {
        return this.key;
    }
}
