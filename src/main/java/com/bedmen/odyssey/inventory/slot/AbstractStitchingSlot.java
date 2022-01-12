package com.bedmen.odyssey.inventory.slot;

import com.bedmen.odyssey.inventory.StitchingMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public abstract class AbstractStitchingSlot extends Slot {
    protected final StitchingMenu stitchingMenu;
    public AbstractStitchingSlot(StitchingMenu stitchingMenu, Container container, int id, int x, int y) {
        super(container, id, x, y);
        this.stitchingMenu = stitchingMenu;
    }

    public abstract boolean canUse();
}