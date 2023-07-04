package com.bedmen.odyssey.items.aspect_items;

import net.minecraft.world.item.ItemStack;

public interface ParryableWeaponItem {

    int getRecoveryTime(ItemStack shield);
}
