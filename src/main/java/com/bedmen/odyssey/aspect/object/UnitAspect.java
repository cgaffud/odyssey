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

public class UnitAspect extends ComparableAspect<Void> {

    // Buff constructor
    protected UnitAspect(String id){
        this(id, 1f, AspectTooltipFunctions.NAME, AspectItemPredicates.NONE, true);
    }
    protected UnitAspect(String id, Predicate<Item> itemPredicate, boolean isBuff){
        this(id, 1f, AspectTooltipFunctions.NAME, itemPredicate, isBuff);
    }

    protected UnitAspect(String id, float weight, Predicate<Item> itemPredicate, boolean isBuff){
        this(id, weight, AspectTooltipFunctions.NAME, itemPredicate, isBuff);
    }

    protected UnitAspect(String id, float weight, AspectTooltipFunction<Void> aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff){
        super(id, getWeightFunction(weight), aspectTooltipFunction, itemPredicate, isBuff);
    }

    public Tag valueToTag(Void value) {
        return null;
    }

    public Void tagToValue(Tag tag) {
        return null;
    }

    public AspectInstance<Void> createInstanceForInfusion(AspectInstance<Void> aspectInstance) {
        return aspectInstance;
    }

    public BinaryOperator<Void> getAddition() {
        return (v1, v2) -> v1;
    }

    public BiFunction<Void, Integer, Void> getScaler() {
        return (v, i) -> v;
    }

    public Void getBase() {
        return null;
    }

    public Class<Void> getValueClass() {
        return Void.class;
    }

    public Void jsonElementToValue(JsonElement jsonElement) {
        return null;
    }

    public Void stringToValue(String s) {
        return null;
    }

    public static Function<Void, Float> getWeightFunction(float weight){
        return v -> weight;
    }

    public Comparator<Void> getComparator(){
        return (v1,v2) -> 0;
    }
}
