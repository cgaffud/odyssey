package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class OdysseyBowEnchantment extends Enchantment {
    public OdysseyBowEnchantment(Enchantment.Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, OdysseyEnchantmentCategory.ALL_BOW, equipmentSlots);
    }

    public int getMaxLevel() {
        return 10;
    }
}