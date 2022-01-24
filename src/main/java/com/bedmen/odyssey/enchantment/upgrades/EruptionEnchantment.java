package com.bedmen.odyssey.enchantment.upgrades;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractRiptideEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;

public class EruptionEnchantment extends AbstractRiptideEnchantment {
    public EruptionEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, equipmentSlots);
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
        return (TieredEnchantment) EnchantmentRegistry.RIPTIDE.get();
    }
}
