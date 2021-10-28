package com.bedmen.odyssey.enchantment.vanilla;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyFireAspectEnchantment extends Enchantment {
    public OdysseyFireAspectEnchantment(Enchantment.Rarity p_i46730_1_, EquipmentSlotType... p_i46730_2_) {
        super(p_i46730_1_, EnchantmentType.WEAPON, p_i46730_2_);
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int pEnchantmentLevel) {
        return 10 + 20 * (pEnchantmentLevel - 1);
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 2;
    }
}