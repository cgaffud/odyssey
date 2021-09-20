package com.bedmen.odyssey.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.RespirationEnchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class PyropneumaticEnchantment extends RespirationEnchantment {
    public PyropneumaticEnchantment(Rarity p_i46724_1_, EquipmentSlotType... p_i46724_2_) {
        super(p_i46724_1_, p_i46724_2_);
    }

    public Enchantment getDowngrade(){
        return Enchantments.RESPIRATION;
    }
}
