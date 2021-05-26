package com.bedmen.odyssey.container.slots;

import com.bedmen.odyssey.container.AlloyFurnaceContainer;
import com.bedmen.odyssey.container.RecycleFurnaceContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class RecycleFurnaceFuelSlot extends Slot {
    private final RecycleFurnaceContainer menu;

    public RecycleFurnaceFuelSlot(RecycleFurnaceContainer p_i50084_1_, IInventory p_i50084_2_, int p_i50084_3_, int p_i50084_4_, int p_i50084_5_) {
        super(p_i50084_2_, p_i50084_3_, p_i50084_4_, p_i50084_5_);
        this.menu = p_i50084_1_;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return this.menu.isFuel(stack);
    }

    public int getMaxStackSize(ItemStack stack) {
        return super.getMaxStackSize(stack);
    }
}