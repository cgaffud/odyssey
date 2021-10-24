package com.bedmen.odyssey.enchantment.vanilla;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseySilkTouchEnchantment extends Enchantment {
    public OdysseySilkTouchEnchantment(Enchantment.Rarity p_i46721_1_, EquipmentSlotType... p_i46721_2_) {
        super(p_i46721_1_, EnchantmentType.DIGGER, p_i46721_2_);
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int pEnchantmentLevel) {
        return 15;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 1;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean checkCompatibility(Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != Enchantments.BLOCK_FORTUNE;
    }
}