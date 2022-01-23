package com.bedmen.odyssey.enchantment.abstracts;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractRespirationEnchantment extends TieredEnchantment {
    public AbstractRespirationEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EnchantmentCategory.ARMOR_HEAD, EquipmentSlotTypes);
    }

    public int getMaxLevel() {
        return 3;
    }
}
