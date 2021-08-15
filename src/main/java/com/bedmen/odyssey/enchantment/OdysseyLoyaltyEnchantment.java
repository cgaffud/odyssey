package com.bedmen.odyssey.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyLoyaltyEnchantment extends Enchantment {
    public OdysseyLoyaltyEnchantment(Enchantment.Rarity p_i48785_1_, EquipmentSlotType... p_i48785_2_) {
        super(p_i48785_1_, OdysseyEnchantmentType.ODYSSEY_TRIDENT, p_i48785_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return 5 + p_77321_1_ * 7;
    }

    public int getMaxCost(int p_223551_1_) {
        return 50;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean checkCompatibility(Enchantment p_77326_1_) {
        return super.checkCompatibility(p_77326_1_);
    }
}