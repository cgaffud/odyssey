package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Aspect<T> {
    public final String id;
    public final Function<T, Float> weightFunction;
    public final AspectTooltipFunction<T> aspectTooltipFunction;
    public final Predicate<Item> itemPredicate;
    public final boolean isBuff;

    protected Aspect(String id, Function<T, Float> weightFunction, AspectTooltipFunction<T> aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff){
        this.id = id;
        this.weightFunction = weightFunction;
        this.aspectTooltipFunction = aspectTooltipFunction;
        this.itemPredicate = itemPredicate;
        this.isBuff = isBuff;
        Aspects.ASPECT_REGISTER.put(id, this);
    }

    public MutableComponent getComponent(){
        return Component.translatable("aspect.oddc."+this.id);
    }

    public abstract Tag valueToTag(T value);

    public abstract T tagToValue(Tag tag);

    public abstract AspectInstance<T> createInstanceForInfusion(AspectInstance<T> aspectInstance);

    public abstract BinaryOperator<T> getAddition();

    public abstract BiFunction<T, Integer, T> getScaler();

    public abstract T getBase();

    public abstract Class<T> getValueClass();

    public abstract T jsonElementToValue(JsonElement jsonElement);

    public abstract T stringToValue(String s);
}
