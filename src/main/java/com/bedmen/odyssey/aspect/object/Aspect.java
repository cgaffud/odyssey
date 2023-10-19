package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public abstract class Aspect<T> {
    public final String id;
    public final float weight;
    public final AspectTooltipFunction aspectTooltipFunction;
    public final Predicate<Item> itemPredicate;

    protected Aspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate){
        this.id = id;
        this.weight = weight;
        this.aspectTooltipFunction = aspectTooltipFunction;
        this.itemPredicate = itemPredicate;
        Aspects.ASPECT_REGISTER.put(id, this);
    }

    public MutableComponent getComponent(){
        return Component.translatable("aspect.oddc."+this.id);
    }

    public abstract AspectInstance<T> generateInstanceWithModifiability(Item item, float modifiability);

    public abstract T castStrength(float strength);

    public abstract Tag valueToTag(T value);

    public abstract T tagToValue(Tag tag);

    public abstract AspectInstance<T> createWeakerInstanceForInfusion(AspectInstance<T> aspectInstance);
}
