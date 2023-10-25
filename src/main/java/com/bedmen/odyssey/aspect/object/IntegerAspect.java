package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public class IntegerAspect extends Aspect<Integer> {
    public final boolean hasInfusionPenalty;

    // Buff constructor
    protected IntegerAspect(String id, AspectTooltipFunction<Integer> aspectTooltipFunction) {
        this(id, 0.0f, aspectTooltipFunction, AspectItemPredicates.NONE, true, false);
    }
    protected IntegerAspect(String id, float weight, AspectTooltipFunction<Integer> aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff, boolean hasInfusionPenalty){
        super(id, weight, aspectTooltipFunction, itemPredicate, isBuff);
        this.hasInfusionPenalty = hasInfusionPenalty;
    }

    public Integer floatToValue(float strength){
        return (int)strength;
    }

    public float valueToFloat(Integer value) {
        return value;
    }

    public Tag valueToTag(Integer value) {
        return IntTag.valueOf(value);
    }

    public Integer tagToValue(Tag tag) {
        return ((IntTag)tag).getAsInt();
    }

    public AspectInstance<Integer> createWeakerInstanceForInfusion(AspectInstance<Integer> aspectInstance) {
        if(this.hasInfusionPenalty){
            return new AspectInstance<>(this, Mth.floor(aspectInstance.value * AspectInstance.INFUSION_PENALTY), aspectInstance.aspectTooltipDisplaySetting, aspectInstance.obfuscated);
        } else {
            return aspectInstance;
        }
    }

    public BinaryOperator<Integer> getAddition() {
        return Integer::sum;
    }

    public BiFunction<Integer, Integer, Integer> getScaler() {
        return (i1,i2) -> i1*i2;
    }

    public Integer getBase() {
        return 0;
    }

    public Class<Integer> getValueClass() {
        return Integer.class;
    }
}
