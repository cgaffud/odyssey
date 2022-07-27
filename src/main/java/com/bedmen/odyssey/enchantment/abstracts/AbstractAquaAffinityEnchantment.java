package com.bedmen.odyssey.enchantment.abstracts;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AbstractAquaAffinityEnchantment extends TieredEnchantment {
    public AbstractAquaAffinityEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EnchantmentCategory.DIGGER, EquipmentSlotTypes);
    }

    public int getMaxLevel() {
        return 1;
    }
}
