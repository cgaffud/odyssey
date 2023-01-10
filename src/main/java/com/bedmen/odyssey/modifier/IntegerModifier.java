package com.bedmen.odyssey.modifier;

import net.minecraft.network.chat.TranslatableComponent;

public class IntegerModifier extends Modifier {
    protected IntegerModifier(String id) {
        super(id, modifierInstance -> new TranslatableComponent("modifier.oddc."+id, (int)modifierInstance.strength));
    }
}
