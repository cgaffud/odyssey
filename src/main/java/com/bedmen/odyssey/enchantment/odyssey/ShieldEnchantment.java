package com.bedmen.odyssey.enchantment.odyssey;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class ShieldEnchantment extends Enchantment {
    public ShieldEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, OdysseyEnchantmentCategory.SHIELD, equipmentSlots);
    }

    public int getMaxLevel() {
        return 4;
    }
}
