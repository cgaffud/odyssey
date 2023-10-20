package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.object.Aspect;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RandomAspectList {

    protected final List<WeightedAspectEntry<?>> weightedAspectEntryList;
    protected final int totalWeight;

    protected RandomAspectList(List<WeightedAspectEntry<?>> weightedAspectEntryList){
        this.weightedAspectEntryList = weightedAspectEntryList;
        this.totalWeight = this.weightedAspectEntryList.stream().reduce(0, (accumulator, weightedAspectEntry) -> accumulator + weightedAspectEntry.weight, Integer::sum);
    }

    protected RandomAspectList filter(ItemStack itemStack){
        return new RandomAspectList(this.weightedAspectEntryList.stream()
                .filter(weightedAspectEntry ->
                        weightedAspectEntry.aspect().itemPredicate.test(itemStack.getItem()))
                .collect(Collectors.toList()));
    }

    public AspectInstance<?> getRandomAspectInstance(RandomSource randomSource){
        int randomInteger = randomSource.nextInt(totalWeight);
        int counter = 0;
        for(WeightedAspectEntry<?> weightedAspectEntry : this.weightedAspectEntryList){
            counter += weightedAspectEntry.weight;
            if(counter > randomInteger){
                return weightedAspectEntry.toAspectInstance();
            }
        }
        // Should never reach here
        return null;
    }

    public static class Builder {

        private final List<WeightedAspectEntry<?>> weightedAspectEntryList = new ArrayList<>();

        public Builder(){}

        public <T> Builder add(Aspect<T> aspect, T value, int weight){
            weightedAspectEntryList.add(new WeightedAspectEntry<>(aspect, value, weight));
            return this;
        }

        public RandomAspectList build(){
            return new RandomAspectList(this.weightedAspectEntryList);
        }
    }

    public record WeightedAspectEntry<T>(Aspect<T> aspect, T value, int weight){

        public AspectInstance<T> toAspectInstance(){
            return new AspectInstance<>(this.aspect, this.value);
        }
    }
}
