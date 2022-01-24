package com.bedmen.odyssey.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class TieredEnchantment extends Enchantment {
    protected TieredEnchantment(Rarity rarity, EnchantmentCategory enchantmentCategory, EquipmentSlot[] equipmentSlots) {
        super(rarity, enchantmentCategory, equipmentSlots);
    }

    public abstract boolean canUpgrade();

    public abstract boolean canDowngrade();

    public abstract TieredEnchantment getUpgrade();

    public abstract TieredEnchantment getDowngrade();
}
