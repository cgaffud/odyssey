package com.bedmen.odyssey.enchantment.abstracts;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public abstract class AbstractRespirationEnchantment extends Enchantment {
    public AbstractRespirationEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, EnchantmentType.ARMOR_HEAD, equipmentSlotTypes);
    }

    public int getMinCost(int p_77321_1_) {
        return 10 * p_77321_1_;
    }

    public int getMaxCost(int p_223551_1_) {
        return this.getMinCost(p_223551_1_) + 30;
    }

    public int getMaxLevel() {
        return 3;
    }
}
