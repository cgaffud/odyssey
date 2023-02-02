package com.bedmen.odyssey.aspect.aspect_objects;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.AspectTooltipFunction;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.util.Lazy;

public class AttributeAspect extends FloatAspect {
    private final Lazy<Attribute> attributeLazy;
    public final AttributeModifier.Operation operation;
    protected AttributeAspect(String id, AspectTooltipFunction aspectTooltipFunction, Lazy<Attribute> attributeLazy, AttributeModifier.Operation operation) {
        super(id, aspectTooltipFunction, AspectItemPredicates.DAMAGEABLE);
        this.attributeLazy = attributeLazy;
        this.operation = operation;
    }

    public Attribute getAttribute(){
        return attributeLazy.get();
    }
}
