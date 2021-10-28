package com.bedmen.odyssey.enchantment.vanilla;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyInfinityEnchantment extends Enchantment {
    public OdysseyInfinityEnchantment(Enchantment.Rarity p_i46736_1_, EquipmentSlotType... p_i46736_2_) {
        super(p_i46736_1_, OdysseyEnchantmentType.NONE, p_i46736_2_);
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int pEnchantmentLevel) {
        return 20;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 1;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean checkCompatibility(Enchantment pEnch) {
        return !(pEnch instanceof MendingEnchantment) && super.checkCompatibility(pEnch);
    }
}