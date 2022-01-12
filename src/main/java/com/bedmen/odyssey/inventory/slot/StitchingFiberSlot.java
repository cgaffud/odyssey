package com.bedmen.odyssey.inventory.slot;

import com.bedmen.odyssey.inventory.StitchingMenu;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class StitchingFiberSlot extends AbstractStitchingSlot {

    public StitchingFiberSlot(StitchingMenu stitchingMenu, Container container, int id, int x, int y) {
        super(stitchingMenu, container, id, x, y);
    }

    public boolean canUse(){
        if(container.getItem(0).isEmpty()){
            return false;
        }
        int id = this.getContainerSlot();
        if(!stitchingMenu.isQuadFiber() && (id == 2 || id == 5)){
            return false;
        }
        return true;
    }

    public boolean mayPlace(ItemStack itemStack) {
        return this.stitchingMenu.isFiber(itemStack) && this.canUse();
    }

    public int getMaxStackSize() {
        return 1;
    }
}
