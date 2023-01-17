package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;

public class PercentageAspect extends FloatAspect {
    protected PercentageAspect(String id) {
        super(id, f -> new TranslatableComponent("aspect.oddc."+id, StringUtil.percentFormat(f)));
    }
}
