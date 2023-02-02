package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.AspectTooltipFunctions;
import net.minecraft.network.chat.TranslatableComponent;

public class GlideAspect extends IntegerAspect implements ActivationAspect {
    protected static final TranslatableComponent ACTIVATION_KEY = new TranslatableComponent("key.jump");

    protected GlideAspect(String id) {
        super(id, AspectTooltipFunctions.GLIDE, AspectItemPredicates.CHEST);
    }

    public TranslatableComponent getKey() {
        return ACTIVATION_KEY;
    }
}
