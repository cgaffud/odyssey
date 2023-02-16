package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.AspectUtil;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomBuffList extends RandomAspectList {

    protected RandomBuffList(List<WeightedAspectEntry> list) {
        super(list);
    }

    protected void generateAndAdd(ItemStack itemStack, Random random, float chance){
        float modifiabilityLeft = AspectUtil.getTotalModifiability(itemStack);
        List<AspectInstance> aspectInstanceList = new ArrayList<>();
        while(modifiabilityLeft > 0.0f && !this.filteredList.isEmpty()){
            WeightedAspectEntry weightedAspectEntry = getRandomWeightedAspectEntry(random);
            AspectInstance aspectInstance = weightedAspectEntry.aspect().generateInstanceWithModifiability(itemStack.getItem(), 0.5f);
            float modifiability = aspectInstance.getModifiability(itemStack);
            if(modifiability > modifiabilityLeft){
                this.filteredList.remove(weightedAspectEntry);
            } else {
                List<AspectInstance> possibleNewAspectInstanceList = new ArrayList<>(aspectInstanceList);
                AspectUtil.addInstance(possibleNewAspectInstanceList, aspectInstance);
                AspectInstance newAspectInstance = possibleNewAspectInstanceList.stream().filter(aspectInstance1 -> aspectInstance1.aspect == aspectInstance.aspect).findFirst().get();
                if(newAspectInstance.strength > weightedAspectEntry.strength()){
                    this.filteredList.remove(weightedAspectEntry);
                } else {
                    aspectInstanceList = possibleNewAspectInstanceList;
                    modifiabilityLeft -= modifiability;
                }
            }
        }
        aspectInstanceList = aspectInstanceList.stream().filter(aspectInstance -> random.nextFloat() < chance).collect(Collectors.toList());
        aspectInstanceList.forEach(aspectInstance -> AspectUtil.addModifier(itemStack, aspectInstance));
    }

    public static Builder<RandomBuffList> builder() {
        return new Builder<>(RandomBuffList.class);
    }
}
