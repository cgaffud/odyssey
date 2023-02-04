package com.bedmen.odyssey.aspect.tooltip;

import net.minecraft.network.chat.MutableComponent;

public interface AspectTooltipFunction {
    MutableComponent apply(AspectTooltipFunctionInput aspectTooltipFunctionInput);
}
