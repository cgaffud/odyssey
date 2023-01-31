package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectInstance;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface AspectTooltipFunction {
    MutableComponent apply(AspectInstance aspectInstance, Optional<Level> optionalLevel);
}
