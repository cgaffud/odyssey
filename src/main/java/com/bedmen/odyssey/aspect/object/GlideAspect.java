package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.network.chat.TranslatableComponent;

public class GlideAspect extends IntegerAspect implements ActivationAspect {
    protected static final TranslatableComponent ACTIVATION_KEY = new TranslatableComponent("key.jump");

    protected GlideAspect() {
        super("glide", 0.0f, AspectTooltipFunctions.GLIDE, AspectItemPredicates.CHEST, true);
    }

    public TranslatableComponent getKey() {
        return ACTIVATION_KEY;
    }
}
