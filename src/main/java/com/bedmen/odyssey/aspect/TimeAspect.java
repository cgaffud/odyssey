package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;

public class TimeAspect extends IntegerAspect {
    protected TimeAspect(String id) {
        super(id, f -> new TranslatableComponent("aspect.oddc."+id, StringUtil.timeFormat(f.intValue())));
    }
}
