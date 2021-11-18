package com.bedmen.odyssey.enchantment.abstracts;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractRiptideEnchantment extends Enchantment {
    public AbstractRiptideEnchantment(Rarity p_i48784_1_, EquipmentSlot... p_i48784_2_) {
        //TODO Change to OdysseyEnchantmentCategory.ODYSSEY_TRIDENT
        super(p_i48784_1_, EnchantmentCategory.TRIDENT, p_i48784_2_);
    }

    public int getMinCost(int p_77321_1_) {
        return 10 + p_77321_1_ * 7;
    }

    public int getMaxCost(int p_223551_1_) {
        return 50;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment != EnchantmentRegistry.LOYALTY.get() && enchantment != EnchantmentRegistry.CHANNELING.get();
    }
}