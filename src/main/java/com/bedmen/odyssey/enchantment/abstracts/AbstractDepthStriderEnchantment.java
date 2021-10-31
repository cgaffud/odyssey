package com.bedmen.odyssey.enchantment.abstracts;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public abstract class AbstractDepthStriderEnchantment extends Enchantment {
    public AbstractDepthStriderEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, EnchantmentType.ARMOR_FEET, equipmentSlotTypes);
    }

    public int getMinCost(int p_77321_1_) {
        return p_77321_1_ * 10;
    }

    public int getMaxCost(int p_223551_1_) {
        return this.getMinCost(p_223551_1_) + 15;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && !(enchantment instanceof AbstractFrostWalkerEnchantment);
    }
}
