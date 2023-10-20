package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public abstract class Aspect<T> {
    public final String id;
    public final float weight;
    public final AspectTooltipFunction<T> aspectTooltipFunction;
    public final Predicate<Item> itemPredicate;
    public final boolean isBuff;

    protected Aspect(String id, float weight, AspectTooltipFunction<T> aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff){
        this.id = id;
        this.weight = weight;
        this.aspectTooltipFunction = aspectTooltipFunction;
        this.itemPredicate = itemPredicate;
        this.isBuff = isBuff;
        Aspects.ASPECT_REGISTER.put(id, this);
    }

    public MutableComponent getComponent(){
        return Component.translatable("aspect.oddc."+this.id);
    }

    public abstract AspectInstance<T> generateInstanceWithModifiability(Item item, float modifiability);

    public abstract T floatToValue(float f);

    public abstract float valueToFloat(T value);

    public abstract Tag valueToTag(T value);

    public abstract T tagToValue(Tag tag);

    public abstract AspectInstance<T> createWeakerInstanceForInfusion(AspectInstance<T> aspectInstance);

    public abstract BinaryOperator<T> getAddition();

    public abstract BiFunction<T, Integer, T> getScaler();

    public abstract T getBase();

    public abstract Class<T> getValueClass();
}
