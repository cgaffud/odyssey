package com.bedmen.odyssey.enchantment.upgrades;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractAquaAffinityEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;

public class MoltenAffinityEnchantment extends AbstractAquaAffinityEnchantment {
    public MoltenAffinityEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
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
        return (TieredEnchantment) EnchantmentRegistry.AQUA_AFFINITY.get();
    }
}
