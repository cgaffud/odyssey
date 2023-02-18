package com.bedmen.odyssey.inventory.slot;

import com.bedmen.odyssey.inventory.StitchingMenu;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class StitchingIngredientSlot extends AbstractStitchingSlot {

    public StitchingIngredientSlot(StitchingMenu stitchingMenu, Container container, int id, int x, int y) {
        super(stitchingMenu, container, id, x, y);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return this.stitchingMenu.isIngredient(itemStack);
    }
}
