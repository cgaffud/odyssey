package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.enchantment.abstracts.AbstractRiptideEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class EruptionEnchantment extends AbstractRiptideEnchantment implements IUpgradedEnchantment {
    public EruptionEnchantment(Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, equipmentSlotTypes);
    }

    public Enchantment getDowngrade(){
        return EnchantmentRegistry.RIPTIDE.get();
    }
}
