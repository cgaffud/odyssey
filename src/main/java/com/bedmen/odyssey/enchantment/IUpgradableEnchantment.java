package com.bedmen.odyssey.enchantment;

import net.minecraft.enchantment.Enchantment;

public interface IUpgradableEnchantment {
    default Enchantment getUpgrade(){
        return null;
    }
    default Enchantment getDowngrade(){
        return null;
    }
}
