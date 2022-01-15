package com.bedmen.odyssey.enchantment.odyssey;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BasicCurseEnchantment extends Enchantment {
    private int maxLevel;
    public BasicCurseEnchantment(Rarity rarity, int maxLevel, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.BREAKABLE, slots);
        this.maxLevel = maxLevel;
    }
    public BasicCurseEnchantment(Rarity rarity, EnchantmentCategory enchantmentType, int maxLevel, EquipmentSlot... slots) {
        super(rarity, enchantmentType, slots);
        this.maxLevel = maxLevel;
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

