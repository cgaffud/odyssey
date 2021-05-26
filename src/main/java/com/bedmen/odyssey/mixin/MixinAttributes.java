package com.bedmen.odyssey.mixin;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.*;

@Mixin(Attributes.class)
public abstract class MixinAttributes {

    private static Attribute register(String id, Attribute attribute) {
        if(id.equals("generic.armor")){
            return Registry.register(Registry.ATTRIBUTE, id, (new RangedAttribute("attribute.name.generic.armor", 0.0D, 0.0D, 120.0D)).setSyncable(true));
        }
        return Registry.register(Registry.ATTRIBUTE, id, attribute);
    }

}
