package com.bedmen.odyssey.combat;

import net.minecraft.world.item.ItemStack;

public interface OdysseyRangedAmmoWeapon extends OdysseyRangedWeapon {
    float getDamageMultiplier(ItemStack itemStack);
}
