package com.bedmen.odyssey.enchantment.upgrades;

import com.bedmen.odyssey.enchantment.IUpgradedEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractRespirationEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class PyropneumaticEnchantment extends AbstractRespirationEnchantment implements IUpgradedEnchantment {
    public PyropneumaticEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    public Enchantment getDowngrade(){
        return EnchantmentRegistry.RESPIRATION.get();
    }
}
