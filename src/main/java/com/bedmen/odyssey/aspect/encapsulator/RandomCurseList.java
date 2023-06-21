package com.bedmen.odyssey.aspect.encapsulator;

import com.bedmen.odyssey.aspect.AspectUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class RandomCurseList extends RandomAspectList {

    private static final int MAX_CURSES = 2;

    protected RandomCurseList(List<WeightedAspectEntry> list) {
        super(list);
    }

    protected void generateAndAdd(ItemStack itemStack, RandomSource randomSource, float chance){
        for(int i = 0; i < MAX_CURSES && randomSource.nextFloat() < chance && !this.filteredList.isEmpty(); i++){
            WeightedAspectEntry weightedAspectEntry = this.getRandomWeightedAspectEntry(randomSource);
            this.filteredList.remove(weightedAspectEntry);
            AspectInstance aspectInstance = new AspectInstance(weightedAspectEntry.aspect(), weightedAspectEntry.strength()).withObfuscation();
            AspectUtil.addModifier(itemStack, aspectInstance);
        }
    }

    public static Builder<RandomCurseList> builder(){
        return new Builder<>(RandomCurseList.class);
    }
}
