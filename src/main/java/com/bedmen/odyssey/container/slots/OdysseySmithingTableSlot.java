package com.bedmen.odyssey.container.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class OdysseySmithingTableSlot extends Slot{

    public OdysseySmithingTableSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean shouldBeUsed(){
        return false;
    }

    public int getMaxStackSize() {
        return 1;
    }
}