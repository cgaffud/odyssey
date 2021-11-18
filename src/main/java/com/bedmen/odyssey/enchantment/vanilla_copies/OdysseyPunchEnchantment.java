package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class OdysseyPunchEnchantment extends Enchantment {
    public OdysseyPunchEnchantment(Rarity p_i46735_1_, EquipmentSlot... p_i46735_2_) {
        super(p_i46735_1_, OdysseyEnchantmentCategory.ALL_BOW, p_i46735_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return 12 + (p_77321_1_ - 1) * 20;
    }

    public int getMaxCost(int p_223551_1_) {
        return this.getMinCost(p_223551_1_) + 25;
    }

    public int getMaxLevel() {
        return 2;
    }
}