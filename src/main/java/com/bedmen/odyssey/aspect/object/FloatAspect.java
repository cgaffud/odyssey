package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import com.google.gson.JsonElement;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class FloatAspect extends ComparableAspect<Float> {

    // Buff constructor
    protected FloatAspect(String id, AspectTooltipFunction<Float> aspectTooltipFunction) {
        this(id, 0f, aspectTooltipFunction, AspectItemPredicates.NONE, true);
    }
    protected FloatAspect(String id, float weight, AspectTooltipFunction<Float> aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff){
        super(id, getWeightFunction(weight), aspectTooltipFunction, itemPredicate, isBuff);
    }

    public Tag valueToTag(Float value) {
        return FloatTag.valueOf(value);
    }

    public Float tagToValue(Tag tag) {
        return ((FloatTag)tag).getAsFloat();
    }

    public AspectInstance<Float> createInstanceForInfusion(AspectInstance<Float> aspectInstance) {
        return new AspectInstance<>(aspectInstance.aspect, aspectInstance.value * AspectInstance.INFUSION_PENALTY, aspectInstance.aspectTooltipDisplaySetting, aspectInstance.obfuscated);
    }

    public BinaryOperator<Float> getAddition() {
        return Float::sum;
    }

    public BiFunction<Float, Integer, Float> getScaler() {
        return (f, i) -> f * (float)i;
    }

    public Float getBase() {
        return 0f;
    }

    public Class<Float> getValueClass() {
        return Float.class;
    }

    public Float jsonElementToValue(JsonElement jsonElement) {
        return jsonElement.getAsFloat();
    }

    public Float stringToValue(String s) {
        return Float.parseFloat(s);
    }

    public static Function<Float, Float> getWeightFunction(float weight){
        return value -> value * weight;
    }

    public Comparator<Float> getComparator(){
        return Float::compare;
    }
}
