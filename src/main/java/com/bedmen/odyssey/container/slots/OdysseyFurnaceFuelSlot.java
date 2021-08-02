package com.bedmen.odyssey.container.slots;

import com.bedmen.odyssey.tileentity.AlloyFurnaceTileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class OdysseyFurnaceFuelSlot extends Slot {

    public OdysseyFurnaceFuelSlot(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    public boolean mayPlace(ItemStack stack) {
        return AlloyFurnaceTileEntity.isFuel(stack);
    }

    public int getMaxStackSize(ItemStack stack) {
        return super.getMaxStackSize(stack);
    }
}
