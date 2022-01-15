package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class OdysseyTridentEnchantment extends Enchantment {
    public OdysseyTridentEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, OdysseyEnchantmentCategory.ODYSSEY_TRIDENT, equipmentSlots);
    }

    public int getMaxLevel() {
        return 10;
    }
}