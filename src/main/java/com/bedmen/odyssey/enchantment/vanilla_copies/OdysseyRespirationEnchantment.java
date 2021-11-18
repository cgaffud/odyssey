package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.IUpgradableEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractRespirationEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class OdysseyRespirationEnchantment extends AbstractRespirationEnchantment implements IUpgradableEnchantment {
    public OdysseyRespirationEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    public Enchantment getUpgrade(){
        return EnchantmentRegistry.PYROPNEUMATIC.get();
    }
}