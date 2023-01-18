package com.bedmen.odyssey.aspect;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface AspectTooltipFunction {
    MutableComponent apply(float strength, Optional<Level> optionalLevel);
}
