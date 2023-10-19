package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public abstract class Aspect<T> {
    public final String id;
    public final float weight;
    public final AspectTooltipFunction aspectTooltipFunction;
    public final Predicate<Item> itemPredicate;
    public final boolean isBuff;

    protected Aspect(String id, float weight, AspectTooltipFunction aspectTooltipFunction, Predicate<Item> itemPredicate, boolean isBuff){
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

    public AspectInstance generateInstanceWithModifiability(float modifiability){
        if(this.weight <= 0.0f){
            return new AspectInstance(this, 1.0f);
        }
        return new AspectInstance(this, modifiability / this.weight);
    }

    public abstract T castStrength(float strength);
}
