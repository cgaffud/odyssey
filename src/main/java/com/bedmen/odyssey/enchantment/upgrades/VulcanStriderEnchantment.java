package com.bedmen.odyssey.enchantment.upgrades;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractDepthStriderEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;

public class VulcanStriderEnchantment extends AbstractDepthStriderEnchantment {
    public VulcanStriderEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
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
        return (TieredEnchantment) EnchantmentRegistry.VULCAN_STRIDER.get();
    }
}
