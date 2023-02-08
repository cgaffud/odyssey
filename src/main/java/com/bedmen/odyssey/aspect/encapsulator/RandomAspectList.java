package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspect;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomAspectList {

    private final List<TieredAspectEntry> list;

    private RandomAspectList(List<TieredAspectEntry> list){
        this.list = list;
    }

    private RandomAspectList filter(ItemStack itemStack){
        return new RandomAspectList(
                this.list.stream()
                        .filter(tieredAspectEntry ->  tieredAspectEntry.aspect.itemPredicate.test(itemStack.getItem()))
                        .collect(Collectors.toList()));
    }

    private List<AspectInstance> generate(ItemStack itemStack, Random random, float chance){
        float modifiabilityLeft = AspectUtil.getTotalModifiability(itemStack);
        List<AspectInstance> aspectInstanceList = new ArrayList<>();
        while(modifiabilityLeft > 0.0f && !this.list.isEmpty()){
            TieredAspectEntry tieredAspectEntry = getRandomTieredAspectEntry(random);
            AspectInstance aspectInstance = tieredAspectEntry.aspect.generateInstanceWithModifiability(itemStack.getItem(), 0.5f);
            float modifiability = aspectInstance.getModifiability(itemStack);
            if(modifiability > modifiabilityLeft){
                this.list.remove(tieredAspectEntry);
            } else {
                List<AspectInstance> possibleNewAspectInstanceList = new ArrayList<>(aspectInstanceList);
                AspectUtil.addInstance(possibleNewAspectInstanceList, aspectInstance);
                AspectInstance newAspectInstance = possibleNewAspectInstanceList.stream().filter(aspectInstance1 -> aspectInstance1.aspect == aspectInstance.aspect).findFirst().get();
                if(newAspectInstance.strength > tieredAspectEntry.maxStrength){
                    this.list.remove(tieredAspectEntry);
                } else {
                    aspectInstanceList = possibleNewAspectInstanceList;
                    modifiabilityLeft -= modifiability;
                }
            }
        }
        return aspectInstanceList.stream().filter(aspectInstance -> random.nextFloat() < chance).collect(Collectors.toList());
    }

    private TieredAspectEntry getRandomTieredAspectEntry(Random random){
        int totalWeight = list.stream().reduce(0, (accumulator, tieredAspectEntry) -> accumulator + tieredAspectEntry.weight, Integer::sum);
        int randomInteger = random.nextInt(totalWeight);
        int counter = 0;
        for(TieredAspectEntry tieredAspectEntry: this.list){
            counter += tieredAspectEntry.weight;
            if(counter > randomInteger){
                return tieredAspectEntry;
            }
        }
        // Should never reach here
        return null;
    }

    public List<AspectInstance> generateAspectInstances(ItemStack itemStack, Random random, float chance){
        return this.filter(itemStack).generate(itemStack, random, chance);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        private final List<TieredAspectEntry> list = new ArrayList<>();

        private Builder(){

        }

        public Builder add(Aspect aspect, float maxStrength, int weight){
            list.add(new TieredAspectEntry(aspect, maxStrength, weight));
            return this;
        }

        public RandomAspectList build(){
            return new RandomAspectList(this.list);
        }

    }

    public static record TieredAspectEntry(Aspect aspect, float maxStrength, int weight){}
}
