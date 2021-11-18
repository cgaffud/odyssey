package com.bedmen.odyssey.enchantment.upgrades;

import com.bedmen.odyssey.enchantment.IUpgradedEnchantment;
import com.bedmen.odyssey.enchantment.abstracts.AbstractAquaAffinityEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class MoltenAffinityEnchantment extends AbstractAquaAffinityEnchantment implements IUpgradedEnchantment {
    public MoltenAffinityEnchantment(Rarity rarity, EquipmentSlot... EquipmentSlotTypes) {
        super(rarity, EquipmentSlotTypes);
    }

    public Enchantment getDowngrade(){
        return EnchantmentRegistry.AQUA_AFFINITY.get();
    }
}
