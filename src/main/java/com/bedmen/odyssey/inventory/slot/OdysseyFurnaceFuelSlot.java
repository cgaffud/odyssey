package com.bedmen.odyssey.inventory.slot;

import com.bedmen.odyssey.inventory.OdysseyFurnaceMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OdysseyFurnaceFuelSlot extends Slot {
    private final OdysseyFurnaceMenu menu;

    public OdysseyFurnaceFuelSlot(OdysseyFurnaceMenu odysseyFurnaceMenu, Container container, int index, int x, int y) {
        super(container, index, x, y);
        this.menu = odysseyFurnaceMenu;
    }

    public boolean mayPlace(ItemStack itemStack) {
        return this.menu.isFuel(itemStack);
    }
}