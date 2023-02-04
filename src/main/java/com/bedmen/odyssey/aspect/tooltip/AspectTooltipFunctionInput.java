package com.bedmen.odyssey.aspect.tooltip;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record AspectTooltipFunctionInput(AspectInstance aspectInstance,
                                         Optional<Level> optionalLevel,
                                         ItemStack itemStack) {
}
