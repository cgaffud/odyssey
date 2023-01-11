package com.bedmen.odyssey.modifier;

import net.minecraft.network.chat.TranslatableComponent;

public class IntegerModifier extends Modifier {
    protected IntegerModifier(String id) {
        super(id, f -> new TranslatableComponent("modifier.oddc."+id, f.intValue()));
    }
}
