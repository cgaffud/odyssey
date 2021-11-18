package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class OdysseyFlameEnchantment extends Enchantment {
    public OdysseyFlameEnchantment(Rarity p_i46737_1_, EquipmentSlot... p_i46737_2_) {
        super(p_i46737_1_, OdysseyEnchantmentCategory.ALL_BOW, p_i46737_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return 20;
    }

    public int getMaxCost(int p_223551_1_) {
        return 50;
    }

    public int getMaxLevel() {
        return 1;
    }
}