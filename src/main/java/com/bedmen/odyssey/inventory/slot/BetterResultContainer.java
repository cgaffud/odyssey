package com.bedmen.odyssey.inventory.slot;

import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

public class BetterResultContainer extends ResultContainer {

    public void setItem(ItemStack itemStack) {
        this.setItem(0, itemStack);
    }

    public ItemStack getItem(){
        return this.getItem(0);
    }
}
