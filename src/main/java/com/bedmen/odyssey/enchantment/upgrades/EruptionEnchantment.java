package com.bedmen.odyssey.enchantment.upgrades;

import com.bedmen.odyssey.enchantment.IUpgradedEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractRiptideEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class EruptionEnchantment extends AbstractRiptideEnchantment implements IUpgradedEnchantment {
    public EruptionEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    public Enchantment getDowngrade(){
        return EnchantmentRegistry.RIPTIDE.get();
    }
}
