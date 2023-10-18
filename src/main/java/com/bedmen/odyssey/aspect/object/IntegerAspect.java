package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class IntegerAspect extends Aspect<Integer> {
    public final boolean hasInfusionPenalty;
    protected IntegerAspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate, boolean hasInfusionPenalty){
        super(id, weight, aspectTooltipFunction, itemPredicate);
        this.hasInfusionPenalty = hasInfusionPenalty;
    }

    public AspectInstance generateInstanceWithModifiability(float modifiability){
        if(this.weight <= 0.0f){
            return new AspectInstance(this, 1);
        }
        return new AspectInstance(this, Integer.max(1, (int)(modifiability / this.weight)));
    }

    public float getWeight(Item item, Integer value) {
        return this.weight * value;
    }

    public Integer castStrength(float strength){
        return (int)strength;
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
}
