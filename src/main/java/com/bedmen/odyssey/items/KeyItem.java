package com.bedmen.odyssey.items;

import com.bedmen.odyssey.lock.LockType;
import net.minecraft.world.item.Item;

public class KeyItem extends Item {
    public final LockType lockType;

    public KeyItem(Properties properties, LockType lockType) {
        super(properties);
        this.lockType = lockType;
    }
}
