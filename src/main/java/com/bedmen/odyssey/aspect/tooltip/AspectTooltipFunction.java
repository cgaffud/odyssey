package com.bedmen.odyssey.aspect.tooltip;

import net.minecraft.network.chat.MutableComponent;

public interface AspectTooltipFunction<T> {
    MutableComponent apply(AspectTooltipFunctionInput<T> aspectTooltipFunctionInput);
}
