package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public interface AspectHolder {
    void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag, AspectTooltipContext aspectTooltipContext);
}
