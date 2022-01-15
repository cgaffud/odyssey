package com.bedmen.odyssey.enchantment.abstracts;

import com.bedmen.odyssey.enchantment.vanilla_copies.OdysseyTridentEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public abstract class AbstractRiptideEnchantment extends OdysseyTridentEnchantment {
    public AbstractRiptideEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, equipmentSlots);
    }

    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment != EnchantmentRegistry.LOYALTY.get() && enchantment != EnchantmentRegistry.CHANNELING.get();
    }
}