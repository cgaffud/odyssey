package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.items.NewShieldItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;

public class RecoveryEnchantment extends Enchantment{
    public RecoveryEnchantment(Enchantment.Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, EnchantmentType.create("shield", (Item item) -> {return item instanceof NewShieldItem || item instanceof ShieldItem;}), slots);
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int enchantmentLevel) {
        return 1;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 1;
    }

    public boolean isTreasureEnchantment() {
        return false;
    }

    public boolean isCurse() {
        return false;
    }
}
