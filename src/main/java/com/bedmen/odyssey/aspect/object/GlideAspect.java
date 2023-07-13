package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class GlideAspect extends IntegerAspect implements ActivationAspect {
    protected static final MutableComponent ACTIVATION_KEY = Component.translatable("key.jump");

    protected GlideAspect() {
        super("glide", 0.0f, AspectTooltipFunctions.GLIDE, AspectItemPredicates.CHEST, false, true);
    }

    public MutableComponent getKeyboardKey() {
        return ACTIVATION_KEY;
    }
}
