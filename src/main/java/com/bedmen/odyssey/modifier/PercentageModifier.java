package com.bedmen.odyssey.modifier;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;

public class PercentageModifier extends FloatModifier {
    protected PercentageModifier(String id) {
        super(id, modifierInstance -> new TranslatableComponent("modifier.oddc."+modifierInstance.modifier.id, StringUtil.percentFormat(modifierInstance.strength)));
    }
}
