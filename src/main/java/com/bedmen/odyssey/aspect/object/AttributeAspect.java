package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Predicate;

public class AttributeAspect extends FloatAspect {
    private final Lazy<Attribute> attributeLazy;
    public final AttributeModifier.Operation operation;
    protected AttributeAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate, Lazy<Attribute> attributeLazy, AttributeModifier.Operation operation) {
        super(id, weight, aspectTooltipFunction, itemPredicate);
        this.attributeLazy = attributeLazy;
        this.operation = operation;
    }

    public Attribute getAttribute(){
        return attributeLazy.get();
    }
}
