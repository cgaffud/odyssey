package com.bedmen.odyssey.enchantment.abstracts;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractFrostWalkerEnchantment extends TieredEnchantment {
    public AbstractFrostWalkerEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EnchantmentCategory.ARMOR_FEET, EquipmentSlotTypes);
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public int getMaxLevel() {
        return 2;
    }

    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && !(enchantment instanceof AbstractDepthStriderEnchantment);
    }
}
