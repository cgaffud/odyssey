package com.bedmen.odyssey.enchantment.vanilla;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyLootBonusEnchantment extends Enchantment {
    public OdysseyLootBonusEnchantment(Enchantment.Rarity p_i46726_1_, EnchantmentType p_i46726_2_, EquipmentSlotType... p_i46726_3_) {
        super(p_i46726_1_, p_i46726_2_, p_i46726_3_);
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int pEnchantmentLevel) {
        return 15 + (pEnchantmentLevel - 1) * 9;
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

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean checkCompatibility(Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != Enchantments.SILK_TOUCH;
    }
}