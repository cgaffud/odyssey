package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class OdysseyChannelingEnchantment extends Enchantment {
    public OdysseyChannelingEnchantment(Rarity p_i48787_1_, EquipmentSlot... p_i48787_2_) {
        //TODO Change to OdysseyEnchantmentCategory.ODYSSEY_TRIDENT
        super(p_i48787_1_, EnchantmentCategory.TRIDENT, p_i48787_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return 25;
    }

    public int getMaxCost(int p_223551_1_) {
        return 50;
    }

    public int getMaxLevel() {
        return 1;
    }

    public boolean checkCompatibility(Enchantment p_77326_1_) {
        return super.checkCompatibility(p_77326_1_);
    }
}