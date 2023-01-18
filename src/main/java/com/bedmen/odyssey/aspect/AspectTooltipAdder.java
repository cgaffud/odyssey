package com.bedmen.odyssey.aspect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public interface AspectTooltipAdder {
    void addTooltip(List<Component> tooltip, TooltipFlag tooltipFlag, Optional<Level> optionalLevel);
}
