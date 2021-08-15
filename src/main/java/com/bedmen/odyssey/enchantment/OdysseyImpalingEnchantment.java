package com.bedmen.odyssey.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyImpalingEnchantment extends Enchantment {
    public OdysseyImpalingEnchantment(Enchantment.Rarity p_i48786_1_, EquipmentSlotType... p_i48786_2_) {
        super(p_i48786_1_, OdysseyEnchantmentType.ODYSSEY_TRIDENT, p_i48786_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return 1 + (p_77321_1_ - 1) * 8;
    }

    public int getMaxCost(int p_223551_1_) {
        return this.getMinCost(p_223551_1_) + 20;
    }

    public int getMaxLevel() {
        return 5;
    }

    public float getDamageBonus(int p_152376_1_, CreatureAttribute p_152376_2_) {
        return p_152376_2_ == CreatureAttribute.WATER ? (float)p_152376_1_ * 2.5F : 0.0F;
    }
}