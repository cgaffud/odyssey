package com.bedmen.odyssey.inventory.slot;

import com.bedmen.odyssey.inventory.StitchingMenu;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class StitchingIngredientSlot extends AbstractStitchingSlot {

    public StitchingIngredientSlot(StitchingMenu stitchingMenu, Container container, int id, int x, int y) {
        super(stitchingMenu, container, id, x, y);
    }

    public boolean canUse(){
        if(container.getItem(0).isEmpty() && this.getContainerSlot() == 1){
            return false;
        }
        return true;
    }

    public boolean mayPlace(ItemStack itemStack) {
        return this.stitchingMenu.isIngredient(itemStack) && this.canUse();
    }

    public int getMaxStackSize() {
        return 1;
    }
}
