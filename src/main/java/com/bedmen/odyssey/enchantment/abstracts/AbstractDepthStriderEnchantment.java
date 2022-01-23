package com.bedmen.odyssey.enchantment.abstracts;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractDepthStriderEnchantment extends TieredEnchantment {
    public AbstractDepthStriderEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EnchantmentCategory.ARMOR_FEET, EquipmentSlotTypes);
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && !(enchantment instanceof AbstractFrostWalkerEnchantment);
    }
}
