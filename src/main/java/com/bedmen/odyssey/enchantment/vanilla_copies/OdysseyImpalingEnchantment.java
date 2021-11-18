package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class OdysseyImpalingEnchantment extends Enchantment {
    public OdysseyImpalingEnchantment(Rarity p_i48786_1_, EquipmentSlot... p_i48786_2_) {
        //TODO Change to OdysseyEnchantmentCategory.ODYSSEY_TRIDENT
        super(p_i48786_1_, EnchantmentCategory.TRIDENT, p_i48786_2_);
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

    public float getDamageBonus(int p_152376_1_, MobType p_152376_2_) {
        return p_152376_2_ == MobType.WATER ? (float)p_152376_1_ * 2.5F : 0.0F;
    }
}