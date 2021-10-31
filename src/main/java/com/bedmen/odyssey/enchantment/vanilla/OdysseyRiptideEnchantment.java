package com.bedmen.odyssey.enchantment.vanilla;

import com.bedmen.odyssey.enchantment.abstracts.AbstractRiptideEnchantment;
import com.bedmen.odyssey.enchantment.IUpgradableEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class OdysseyRiptideEnchantment extends AbstractRiptideEnchantment implements IUpgradableEnchantment {
    public OdysseyRiptideEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, equipmentSlotTypes);
    }

    public Enchantment getUpgrade(){
        return EnchantmentRegistry.ERUPTION.get();
    }
}