package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class UnitActivationAspect extends UnitAspect implements ActivationAspect {

    private final MutableComponent key;

    protected UnitActivationAspect(String id, String key) {
        super(id, 0.0f, AspectTooltipFunctions.NAME_AND_ACTIVATION_KEY, AspectItemPredicates.NONE, false);
        this.key = Component.translatable(key);
    }

    public MutableComponent getKeyboardKey() {
        return this.key;
    }
}
