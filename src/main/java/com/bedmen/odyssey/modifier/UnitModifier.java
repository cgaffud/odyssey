package com.bedmen.odyssey.modifier;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Function;

public class UnitModifier extends FloatModifier {
    protected UnitModifier(String id) {
        super(id, modifierInstance -> new TranslatableComponent("modifier.oddc."+id, StringUtil.floatFormat(1.0f+modifierInstance.strength)));
    }
}
