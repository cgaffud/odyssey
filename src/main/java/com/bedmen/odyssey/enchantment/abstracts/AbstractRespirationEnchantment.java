package com.bedmen.odyssey.enchantment.abstracts;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractRespirationEnchantment extends Enchantment {
    public AbstractRespirationEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EnchantmentCategory.ARMOR_HEAD, EquipmentSlotTypes);
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
