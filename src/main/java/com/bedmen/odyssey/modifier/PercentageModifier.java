package com.bedmen.odyssey.modifier;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;

public class PercentageModifier extends FloatModifier {
    protected PercentageModifier(String id) {
        super(id, f -> new TranslatableComponent("modifier.oddc."+id, StringUtil.percentFormat(f)));
    }
}
