package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.enchantment.abstracts.AbstractRespirationEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class PyropneumaticEnchantment extends AbstractRespirationEnchantment implements IUpgradedEnchantment {
    public PyropneumaticEnchantment(Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, equipmentSlotTypes);
    }

    public Enchantment getDowngrade(){
        return EnchantmentRegistry.RESPIRATION.get();
    }
}
