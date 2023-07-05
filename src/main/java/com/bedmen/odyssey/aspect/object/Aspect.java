package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public abstract class Aspect<T> {
    public final String id;
    protected final float weight;
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

    public float getWeight(Item item){
        return this.weight;
    }

    public AspectInstance generateInstanceWithModifiability(Item item, float modifiability){
        float weight = this.getWeight(item);
        if(weight <= 0.0f){
            return new AspectInstance(this, 1.0f);
        }
        return new AspectInstance(this, modifiability / weight);
    }

    public abstract T castStrength(float strength);
}
