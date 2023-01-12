package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.combat.OdysseyArmorMaterials;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.items.equipment.base.EquipmentArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeableLeatherItem;

public class DyeableEquipmentArmorItem extends EquipmentArmorItem implements DyeableLeatherItem {
    public DyeableEquipmentArmorItem(OdysseyArmorMaterials armorMaterial, EquipmentSlot slotType, Properties properties, LevEnchSup... levEnchSups) {
        super(armorMaterial, slotType, properties, levEnchSups);
    }
}
