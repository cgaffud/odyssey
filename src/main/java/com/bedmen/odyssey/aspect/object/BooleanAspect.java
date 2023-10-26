package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunctions;
import com.google.gson.JsonElement;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class BooleanAspect extends ComparableAspect<Boolean> {

    // Buff constructor
    protected BooleanAspect(String id){
        this(id, 1f, AspectTooltipFunctions.NAME, AspectItemPredicates.NONE, true);
    }
    protected BooleanAspect(String id, Predicate<Item> itemPredicate, boolean isBuff){
        this(id, 1f, AspectTooltipFunctions.NAME, itemPredicate, isBuff);
    }

    protected BooleanAspect(String id, float weight, Predicate<Item> itemPredicate, boolean isBuff){
        this(id, weight, AspectTooltipFunctions.NAME, itemPredicate, isBuff);
    }

    protected BooleanAspect(String id, float weight, AspectTooltipFunction<Boolean> aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff){
        super(id, getWeightFunction(weight), aspectTooltipFunction, itemPredicate, isBuff);
    }

    public Tag valueToTag(Boolean value) {
        return ByteTag.valueOf(value);
    }

    public Boolean tagToValue(Tag tag) {
        return ((ByteTag)tag).getAsByte() > 0;
    }

    public AspectInstance<Boolean> createInstanceForInfusion(AspectInstance<Boolean> aspectInstance) {
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

    public Boolean jsonElementToValue(JsonElement jsonElement) {
        return jsonElement.getAsBoolean();
    }

    public Boolean stringToValue(String s) {
        return Boolean.parseBoolean(s);
    }

    public static Function<Boolean, Float> getWeightFunction(float weight){
        return value -> value ? weight : 0.0f;
    }

    public Comparator<Boolean> getComparator(){
        return Boolean::compare;
    }
}
