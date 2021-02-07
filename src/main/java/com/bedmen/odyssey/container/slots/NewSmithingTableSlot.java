package com.bedmen.odyssey.container.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class NewSmithingTableSlot extends Slot{

    public NewSmithingTableSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean shouldBeUsed() {
        return false;
    }

}