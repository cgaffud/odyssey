package com.bedmen.odyssey.enchantment.vanilla_copies;

import com.bedmen.odyssey.enchantment.abstracts.AbstractRiptideEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class OdysseyChannelingOrLoyaltyEnchantment extends OdysseyTridentEnchantment {
    public OdysseyChannelingOrLoyaltyEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, equipmentSlots);
    }

    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && !(enchantment instanceof AbstractRiptideEnchantment);
    }
}