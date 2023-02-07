package com.bedmen.odyssey.aspect.object;

import com.bedmen.odyssey.aspect.tooltip.AspectTooltipFunction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class Aspect {
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

    public TranslatableComponent getComponent(){
        return new TranslatableComponent("aspect.oddc."+this.id);
    }

    public float getWeight(Item item){
        return this.weight;
    }
}
