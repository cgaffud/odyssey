package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;

public class UnitAspect extends FloatAspect {
    protected UnitAspect(String id) {
        super(id, f -> new TranslatableComponent("aspect.oddc."+id, StringUtil.floatFormat(1.0f+f)));
    }
}
