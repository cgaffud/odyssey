package com.bedmen.odyssey.enchantment.abstracts;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractDepthStriderEnchantment extends Enchantment {
    public AbstractDepthStriderEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EnchantmentCategory.ARMOR_FEET, EquipmentSlotTypes);
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
