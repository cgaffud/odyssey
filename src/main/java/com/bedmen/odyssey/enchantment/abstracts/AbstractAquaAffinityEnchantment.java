package com.bedmen.odyssey.enchantment.abstracts;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractAquaAffinityEnchantment extends Enchantment {
    public AbstractAquaAffinityEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EnchantmentCategory.DIGGER, EquipmentSlotTypes);
    }

    public int getMinCost(int p_77321_1_) {
        return 1;
    }

    public int getMaxCost(int p_223551_1_) {
        return this.getMinCost(p_223551_1_) + 40;
    }

    public int getMaxLevel() {
        return 1;
    }
}
