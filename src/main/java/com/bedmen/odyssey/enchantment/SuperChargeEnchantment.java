package com.bedmen.odyssey.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class SuperChargeEnchantment extends Enchantment {
    public SuperChargeEnchantment(Enchantment.Rarity p_i50016_1_, EquipmentSlotType... p_i50016_2_) {
        super(p_i50016_1_, EnchantmentType.BOW, p_i50016_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return 10 + (p_77321_1_ - 1) * 20;
    }

    public int getMaxCost(int p_223551_1_) {
        return 50;
    }

    public int getMaxLevel() {
        return 3;
    }
}