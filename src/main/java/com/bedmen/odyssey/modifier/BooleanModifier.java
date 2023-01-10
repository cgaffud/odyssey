package com.bedmen.odyssey.modifier;

import net.minecraft.network.chat.TranslatableComponent;

public class BooleanModifier extends Modifier {
    protected BooleanModifier(String id) {
        super(id, modifierInstance -> new TranslatableComponent("modifier.oddc."+id));
    }
}
