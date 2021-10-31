package com.bedmen.odyssey.enchantment.abstracts;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentType;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public abstract class AbstractRiptideEnchantment extends Enchantment {
    public AbstractRiptideEnchantment(Enchantment.Rarity p_i48784_1_, EquipmentSlotType... p_i48784_2_) {
        super(p_i48784_1_, OdysseyEnchantmentType.ODYSSEY_TRIDENT, p_i48784_2_);
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