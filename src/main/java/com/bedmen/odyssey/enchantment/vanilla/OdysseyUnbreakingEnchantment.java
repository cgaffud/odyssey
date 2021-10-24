package com.bedmen.odyssey.enchantment.vanilla;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyUnbreakingEnchantment extends Enchantment {
    public OdysseyUnbreakingEnchantment(Enchantment.Rarity p_i46733_1_, EquipmentSlotType... p_i46733_2_) {
        super(p_i46733_1_, EnchantmentType.BREAKABLE, p_i46733_2_);
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int pEnchantmentLevel) {
        return 5 + (pEnchantmentLevel - 1) * 8;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 3;
    }
}