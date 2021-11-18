package com.bedmen.odyssey.enchantment.vanilla_copies;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class OdysseyFireAspectEnchantment extends Enchantment {
    public OdysseyFireAspectEnchantment(Rarity p_i46730_1_, EquipmentSlot... p_i46730_2_) {
        super(p_i46730_1_, EnchantmentCategory.WEAPON, p_i46730_2_);
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