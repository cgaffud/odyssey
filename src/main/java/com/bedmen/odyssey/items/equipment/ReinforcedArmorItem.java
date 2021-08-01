package com.bedmen.odyssey.items.equipment;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraftforge.common.util.Lazy;

public class ReinforcedArmorItem extends EquipmentArmorItem {
    public ReinforcedArmorItem(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties properties) {
        super(armorMaterial, slotType, properties);
        this.enchantmentLazyMap.put(Lazy.of(()->Enchantments.BLAST_PROTECTION), 1);
    }
}
