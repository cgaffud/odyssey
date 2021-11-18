package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class OdysseyLootBonusEnchantment extends Enchantment {
    public OdysseyLootBonusEnchantment(Rarity p_i46726_1_, EnchantmentCategory p_i46726_2_, EquipmentSlot... p_i46726_3_) {
        super(p_i46726_1_, p_i46726_2_, p_i46726_3_);
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinCost(int pEnchantmentLevel) {
        return 15 + (pEnchantmentLevel - 1) * 9;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel() {
        return 3;
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean checkCompatibility(Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != EnchantmentRegistry.SILK_TOUCH.get();
    }
}