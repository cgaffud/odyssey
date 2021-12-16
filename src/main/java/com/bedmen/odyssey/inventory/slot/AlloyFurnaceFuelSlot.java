package com.bedmen.odyssey.inventory.slot;

import com.bedmen.odyssey.inventory.AlloyFurnaceMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AlloyFurnaceFuelSlot extends Slot {
    private final AlloyFurnaceMenu menu;

    public AlloyFurnaceFuelSlot(AlloyFurnaceMenu alloyFurnaceMenu, Container container, int index, int x, int y) {
        super(container, index, x, y);
        this.menu = alloyFurnaceMenu;
    }

    public boolean mayPlace(ItemStack itemStack) {
        return this.menu.isFuel(itemStack);
    }
}