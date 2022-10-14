package com.bedmen.odyssey.items;

import net.minecraft.world.item.Item;

public class QuillItem extends Item {

    public final boolean isCursed;
    public QuillItem(Properties properties, boolean isCursed) {
        super(properties);
        this.isCursed = isCursed;
    }
}
