package com.bedmen.odyssey.weapon;

import net.minecraft.world.item.ItemStack;

public interface OdysseyRangedAmmoWeapon extends OdysseyRangedWeapon {
    float getDamageMultiplier(ItemStack itemStack);
}
