package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class OdysseyPiercingEnchantment extends Enchantment {
    public OdysseyPiercingEnchantment(Rarity p_i50019_1_, EquipmentSlot... p_i50019_2_) {
        super(p_i50019_1_, OdysseyEnchantmentCategory.ALL_BOW, p_i50019_2_);
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