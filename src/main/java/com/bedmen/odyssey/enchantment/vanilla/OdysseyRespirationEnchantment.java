package com.bedmen.odyssey.enchantment.vanilla;

import com.bedmen.odyssey.enchantment.IUpgradableEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractRespirationEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyRespirationEnchantment extends AbstractRespirationEnchantment implements IUpgradableEnchantment {
    public OdysseyRespirationEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, equipmentSlotTypes);
    }

    public Enchantment getUpgrade(){
        return EnchantmentRegistry.PYROPNEUMATIC.get();
    }
}