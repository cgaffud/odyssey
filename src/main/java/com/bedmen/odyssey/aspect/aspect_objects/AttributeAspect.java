package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.util.Lazy;

public class AttributeAspect extends FloatAspect {
    private final Lazy<Attribute> attributeLazy;
    public final AttributeModifier.Operation operation;
    protected AttributeAspect(String id, Lazy<Attribute> attributeLazy, AttributeModifier.Operation operation) {
        super(id, operation == AttributeModifier.Operation.ADDITION ?
                (strength, optionalLevel) -> new TranslatableComponent("aspect.oddc."+id, StringUtil.floatFormat(strength)) :
                (strength, optionalLevel) -> new TranslatableComponent("aspect.oddc."+id, StringUtil.percentFormat(strength)));
        this.attributeLazy = attributeLazy;
        this.operation = operation;
    }

    public Attribute getAttribute(){
        return attributeLazy.get();
    }
}
