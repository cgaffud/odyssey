package com.bedmen.odyssey.enchantment;

import com.bedmen.odyssey.enchantment.abstracts.AbstractAquaAffinityEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class MoltenAffinityEnchantment extends AbstractAquaAffinityEnchantment implements IUpgradedEnchantment {
    public MoltenAffinityEnchantment(Rarity rarity, EquipmentSlotType... equipmentSlotTypes) {
        super(rarity, equipmentSlotTypes);
    }

    public Enchantment getDowngrade(){
        return EnchantmentRegistry.AQUA_AFFINITY.get();
    }
}
