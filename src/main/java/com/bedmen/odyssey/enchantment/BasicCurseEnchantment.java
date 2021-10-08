package com.bedmen.odyssey.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class BasicCurseEnchantment extends Enchantment {
    private int maxLevel;
    public BasicCurseEnchantment(Rarity rarity, int maxLevel, EquipmentSlotType... slots) {
        super(rarity, EnchantmentType.BREAKABLE, slots);
        this.maxLevel = maxLevel;
    }
    public BasicCurseEnchantment(Rarity rarity, EnchantmentType enchantmentType, int maxLevel, EquipmentSlotType... slots) {
        super(rarity, enchantmentType, slots);
        this.maxLevel = maxLevel;
    }
    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int enchantmentLevel) {
        return 25;
    }

    public int getMaxCost(int enchantmentLevel) {
        return 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return this.maxLevel;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean isCurse() {
        return true;
    }
}

