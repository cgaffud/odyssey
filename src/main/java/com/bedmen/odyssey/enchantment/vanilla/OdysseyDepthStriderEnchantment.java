package com.bedmen.odyssey.enchantment.vanilla;

import com.bedmen.odyssey.enchantment.IUpgradableEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractDepthStriderEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyDepthStriderEnchantment extends AbstractDepthStriderEnchantment implements IUpgradableEnchantment {
    public OdysseyDepthStriderEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, equipmentSlotTypes);
    }

    public Enchantment getUpgrade(){
        return EnchantmentRegistry.VULCAN_STRIDER.get();
    }
}