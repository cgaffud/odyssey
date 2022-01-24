package com.bedmen.odyssey.enchantment.upgrades;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractRespirationEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;

public class PyropneumaticEnchantment extends AbstractRespirationEnchantment {
    public PyropneumaticEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public boolean canDowngrade() {
        return true;
    }

    @Override
    public TieredEnchantment getUpgrade() {
        return null;
    }

    @Override
    public TieredEnchantment getDowngrade() {
        return (TieredEnchantment) EnchantmentRegistry.RESPIRATION.get();
    }
}
