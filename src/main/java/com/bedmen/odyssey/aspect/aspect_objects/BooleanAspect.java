package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import net.minecraft.network.chat.TranslatableComponent;

public class BooleanAspect extends Aspect {
    protected BooleanAspect(String id) {
        super(id, (strength, optionalLevel) -> new TranslatableComponent("aspect.oddc."+id));
    }
}
