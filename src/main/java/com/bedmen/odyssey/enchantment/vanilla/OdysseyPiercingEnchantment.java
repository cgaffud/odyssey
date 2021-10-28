package com.bedmen.odyssey.enchantment.vanilla;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyPiercingEnchantment extends Enchantment {
    public OdysseyPiercingEnchantment(Enchantment.Rarity p_i50019_1_, EquipmentSlotType... p_i50019_2_) {
        super(p_i50019_1_, OdysseyEnchantmentType.ALL_BOW, p_i50019_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return 1 + (p_77321_1_ - 1) * 10;
    }

    public int getMaxCost(int p_223551_1_) {
        return 50;
    }

    public int getMaxLevel() {
        return 4;
    }
}