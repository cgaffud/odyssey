package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.aspect.object.BooleanAspect;
import com.bedmen.odyssey.aspect.object.IntegerAspect;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class RandomAspectList {

    protected final List<WeightedAspectEntry> weightedAspectEntryList;
    protected List<WeightedAspectEntry> filteredList;

    protected RandomAspectList(List<WeightedAspectEntry> weightedAspectEntryList){
        this.weightedAspectEntryList = weightedAspectEntryList;
    }

    public void addAspectInstances(ItemStack itemStack, Random random, float chance){
        this.filter(itemStack);
        this.generateAndAdd(itemStack, random, chance);
    }

    protected void filter(ItemStack itemStack){
        this.filteredList = this.weightedAspectEntryList.stream()
                .filter(weightedAspectEntry ->
                        weightedAspectEntry.aspect().itemPredicate.test(itemStack.getItem())
                        && AspectUtil.getAspectStrength(itemStack, weightedAspectEntry.aspect) <= 0.0f)
                .collect(Collectors.toList());
    }

    protected abstract void generateAndAdd(ItemStack itemStack, Random random, float chance);

    protected WeightedAspectEntry getRandomWeightedAspectEntry(Random random){
        int totalWeight = this.filteredList.stream().reduce(0, (accumulator, weightedAspectEntry) -> accumulator + weightedAspectEntry.weight, Integer::sum);
        int randomInteger = random.nextInt(totalWeight);
        int counter = 0;
        for(WeightedAspectEntry weightedAspectEntry : this.filteredList){
            counter += weightedAspectEntry.weight;
            if(counter > randomInteger){
                return weightedAspectEntry;
            }
        }
        // Should never reach here
        return null;
    }

    public static class Builder<R extends RandomAspectList>{

        private final List<WeightedAspectEntry> weightedAspectEntryList = new ArrayList<>();
        private final Class<R> clazz;

        protected Builder(Class<R> clazz){
            this.clazz = clazz;
        }

        public Builder<R> add(Aspect aspect, float strength, int weight){
            if(aspect instanceof IntegerAspect && strength != (float)(int)strength){
                throw new IllegalArgumentException("Must be an integer strength for IntegerAspects");
            }
            if(aspect instanceof BooleanAspect){
                strength = 1.0f;
            }
            weightedAspectEntryList.add(new WeightedAspectEntry(aspect, strength, weight));
            return this;
        }

        public R build(){
            try{
                return this.clazz.getDeclaredConstructor(List.class).newInstance(this.weightedAspectEntryList);
            } catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException noSuchMethodException){
                Odyssey.LOGGER.error("RandomAspectList.Builder build failed.");
                for(StackTraceElement stackTraceElement: noSuchMethodException.getStackTrace()){
                    Odyssey.LOGGER.error(stackTraceElement);
                }
                return null;
            }
        }

    }

    public static record WeightedAspectEntry(Aspect aspect, float strength, int weight){}
}
