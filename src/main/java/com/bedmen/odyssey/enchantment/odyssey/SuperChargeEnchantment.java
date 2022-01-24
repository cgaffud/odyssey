package com.bedmen.odyssey.enchantment.odyssey;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SuperChargeEnchantment extends Enchantment {
    public SuperChargeEnchantment(Rarity p_i50016_1_, EquipmentSlot... p_i50016_2_) {
        super(p_i50016_1_, EnchantmentCategory.BOW, p_i50016_2_);
    }

    public int getMaxLevel() {
        return 3;
    }
}