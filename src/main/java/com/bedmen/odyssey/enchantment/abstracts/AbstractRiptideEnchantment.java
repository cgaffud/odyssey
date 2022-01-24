package com.bedmen.odyssey.enchantment.abstracts;

import com.bedmen.odyssey.enchantment.OdysseyEnchantmentCategory;
import com.bedmen.odyssey.enchantment.TieredEnchantment;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public abstract class AbstractRiptideEnchantment extends TieredEnchantment {
    public AbstractRiptideEnchantment(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, OdysseyEnchantmentCategory.ODYSSEY_TRIDENT, equipmentSlots);
    }

    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment) && enchantment != EnchantmentRegistry.LOYALTY.get() && enchantment != EnchantmentRegistry.CHANNELING.get();
    }
}