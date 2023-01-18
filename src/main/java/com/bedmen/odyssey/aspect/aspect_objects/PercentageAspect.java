package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;

public class PercentageAspect extends FloatAspect {
    protected PercentageAspect(String id) {
        super(id, (strength, optionalLevel) -> new TranslatableComponent("aspect.oddc."+id, StringUtil.percentFormat(strength)));
    }
}
