package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.items.NewShieldItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ShieldItem;

public class UnenchantableEnchantment extends Enchantment {
    public UnenchantableEnchantment(Rarity rarity, EquipmentSlotType... slots) {
        super(rarity, EnchantmentType.BREAKABLE, slots);
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
        return 1;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean isCurse() {
        return true;
    }
}
