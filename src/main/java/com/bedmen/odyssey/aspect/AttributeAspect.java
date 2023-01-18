package com.bedmen.odyssey.aspect;

import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.util.Lazy;

import java.util.UUID;

public class AttributeAspect extends FloatAspect {
    private final Lazy<Attribute> attributeLazy;
    public final AttributeModifier.Operation operation;
    protected AttributeAspect(String id, Lazy<Attribute> attributeLazy, AttributeModifier.Operation operation) {
        super(id, operation == AttributeModifier.Operation.ADDITION ?
                f -> new TranslatableComponent("aspect.oddc."+id, StringUtil.floatFormat(f)) :
                f -> new TranslatableComponent("aspect.oddc."+id, StringUtil.percentFormat(f)));
        this.attributeLazy = attributeLazy;
        this.operation = operation;
    }

    public Attribute getAttribute(){
        return attributeLazy.get();
    }
}
