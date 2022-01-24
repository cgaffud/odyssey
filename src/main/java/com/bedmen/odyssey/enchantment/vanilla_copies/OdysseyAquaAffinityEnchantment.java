package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.TieredEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractAquaAffinityEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;

public class OdysseyAquaAffinityEnchantment extends AbstractAquaAffinityEnchantment {
    public OdysseyAquaAffinityEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    @Override
    public boolean canUpgrade() {
        return true;
    }

    @Override
    public boolean canDowngrade() {
        return false;
    }

    @Override
    public TieredEnchantment getUpgrade() {
        return (TieredEnchantment) EnchantmentRegistry.MOLTEN_AFFINITY.get();
    }

    @Override
    public TieredEnchantment getDowngrade() {
        return null;
    }
}