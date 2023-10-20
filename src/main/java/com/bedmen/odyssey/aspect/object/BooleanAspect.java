package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public class BooleanAspect extends Aspect<Boolean> {

    // Buff constructor
    protected BooleanAspect(String id){
        this(id, 1.0f, AspectTooltipFunctions.NAME, AspectItemPredicates.NONE, true);
    }
    protected BooleanAspect(String id, Predicate<Item> itemPredicate, boolean isBuff){
        this(id, 1.0f, AspectTooltipFunctions.NAME, itemPredicate, isBuff);
    }

    protected BooleanAspect(String id, float weight, Predicate<Item> itemPredicate, boolean isBuff){
        this(id, weight, AspectTooltipFunctions.NAME, itemPredicate, isBuff);
    }

    protected BooleanAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff){
        super(id, weight, aspectTooltipFunction, itemPredicate, isBuff);
    }

    public AspectInstance generateInstanceWithModifiability(Item item, float modifiability){
        return new AspectInstance<>(this);
    }

    public Boolean floatToValue(float strength){
        return strength > 0f;
    }

    public float valueToFloat(Boolean value) {
        return value ? 1.0f : 0.0f;
    }

    public Tag valueToTag(Boolean value) {
        return ByteTag.valueOf(value);
    }

    public Boolean tagToValue(Tag tag) {
        return ((ByteTag)tag).getAsByte() > 0;
    }

    public AspectInstance<Boolean> createWeakerInstanceForInfusion(AspectInstance<Boolean> aspectInstance) {
        return aspectInstance;
    }

    public BinaryOperator<Boolean> getAddition() {
        return Boolean::logicalAnd;
    }

    public BiFunction<Boolean, Integer, Boolean> getScaler() {
        return (b, i) -> b && i != 0;
    }

    public Boolean getBase() {
        return false;
    }

    public Class<Boolean> getValueClass() {
        return Boolean.class;
    }
}
