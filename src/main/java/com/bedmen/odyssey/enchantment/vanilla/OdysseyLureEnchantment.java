package com.bedmen.odyssey.enchantment.vanilla;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyLureEnchantment extends Enchantment {
    public OdysseyLureEnchantment(Enchantment.Rarity p_i46729_1_, EnchantmentType p_i46729_2_, EquipmentSlotType... p_i46729_3_) {
        super(p_i46729_1_, p_i46729_2_, p_i46729_3_);
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
}