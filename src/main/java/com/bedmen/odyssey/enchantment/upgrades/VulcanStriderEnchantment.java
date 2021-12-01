package com.bedmen.odyssey.enchantment.upgrades;

import com.bedmen.odyssey.enchantment.IUpgradedEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractDepthStriderEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class VulcanStriderEnchantment extends AbstractDepthStriderEnchantment implements IUpgradedEnchantment {
    public VulcanStriderEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    public Enchantment getDowngrade(){
        return EnchantmentRegistry.DEPTH_STRIDER.get();
    }
}