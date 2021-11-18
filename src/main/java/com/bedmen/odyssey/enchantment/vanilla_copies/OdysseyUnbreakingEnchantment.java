package com.bedmen.odyssey.enchantment.vanilla_copies;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class OdysseyUnbreakingEnchantment extends Enchantment {
    public OdysseyUnbreakingEnchantment(Rarity p_i46733_1_, EquipmentSlot... p_i46733_2_) {
        super(p_i46733_1_, EnchantmentCategory.BREAKABLE, p_i46733_2_);
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