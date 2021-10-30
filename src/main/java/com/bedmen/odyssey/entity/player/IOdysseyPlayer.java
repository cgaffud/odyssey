package com.bedmen.odyssey.entity.player;

import net.minecraft.item.ItemStack;

public interface IOdysseyPlayer {
    int getLifeFruits();
    void setLifeFruits(int i);
    void incrementLifeFruits();
    ItemStack getTrinketSlot();
}
