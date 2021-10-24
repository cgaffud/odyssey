package com.bedmen.odyssey.enchantment.vanilla;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyPowerEnchantment extends Enchantment {
    public OdysseyPowerEnchantment(Enchantment.Rarity p_i46738_1_, EquipmentSlotType... p_i46738_2_) {
        super(p_i46738_1_, OdysseyEnchantmentType.ALL_BOW, p_i46738_2_);
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int pEnchantmentLevel) {
        return 1 + (pEnchantmentLevel - 1) * 10;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return this.getMinCost(pEnchantmentLevel) + 15;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 5;
    }
}