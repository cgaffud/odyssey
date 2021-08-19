package com.bedmen.odyssey.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyAquaAffinityEnchantment extends Enchantment {
    public OdysseyAquaAffinityEnchantment(Enchantment.Rarity p_i46719_1_, EquipmentSlotType... p_i46719_2_) {
        super(p_i46719_1_, EnchantmentType.DIGGER, p_i46719_2_);
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