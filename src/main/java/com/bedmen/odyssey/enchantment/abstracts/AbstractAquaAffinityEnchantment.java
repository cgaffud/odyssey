package com.bedmen.odyssey.enchantment.abstracts;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public abstract class AbstractAquaAffinityEnchantment extends Enchantment {
    public AbstractAquaAffinityEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, EnchantmentType.DIGGER, equipmentSlotTypes);
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
