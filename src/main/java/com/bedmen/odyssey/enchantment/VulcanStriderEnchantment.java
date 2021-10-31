package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.enchantment.abstracts.AbstractDepthStriderEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class VulcanStriderEnchantment extends AbstractDepthStriderEnchantment implements IUpgradedEnchantment {
    public VulcanStriderEnchantment(Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, equipmentSlotTypes);
    }

    public Enchantment getDowngrade(){
        return EnchantmentRegistry.DEPTH_STRIDER.get();
    }
}
