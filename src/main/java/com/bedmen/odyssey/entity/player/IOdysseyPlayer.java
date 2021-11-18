package com.bedmen.odyssey.entity.player;

import net.minecraft.world.item.ItemStack;

public interface IOdysseyPlayer {
    int getLifeFruits();
    void setLifeFruits(int i);
    void incrementLifeFruits();
    ItemStack getTrinketSlot();
}
