package com.bedmen.odyssey.enchantment.odyssey;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SetBonusEnchantment extends Enchantment {
    private final int maxLevel;
    public SetBonusEnchantment(Rarity rarity, int maxLevel, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.ARMOR, slots);
        this.maxLevel = maxLevel;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean isCurse() {
        return false;
    }
}

