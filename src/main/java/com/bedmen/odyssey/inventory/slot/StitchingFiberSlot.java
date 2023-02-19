package com.bedmen.odyssey.inventory.slot;

import com.bedmen.odyssey.inventory.StitchingMenu;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class StitchingFiberSlot extends AbstractStitchingSlot {

    public StitchingFiberSlot(StitchingMenu stitchingMenu, Container container, int id, int x, int y) {
        super(stitchingMenu, container, id, x, y);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return this.stitchingMenu.isFiber(itemStack);
    }
}
