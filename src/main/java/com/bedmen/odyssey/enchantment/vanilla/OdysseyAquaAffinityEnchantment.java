package com.bedmen.odyssey.enchantment.vanilla;

import com.bedmen.odyssey.enchantment.IUpgradableEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractAquaAffinityEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyAquaAffinityEnchantment extends AbstractAquaAffinityEnchantment implements IUpgradableEnchantment {
    public OdysseyAquaAffinityEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, equipmentSlotTypes);
    }

    public Enchantment getUpgrade(){
        return EnchantmentRegistry.MOLTEN_AFFINITY.get();
    }
}