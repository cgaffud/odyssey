package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.combat.OdysseyArmorMaterials;
import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.items.equipment.base.EquipmentArmorItem;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;
import java.util.function.Supplier;

public class AttributeArmorItem extends EquipmentArmorItem {
    public AttributeArmorItem(OdysseyArmorMaterials armorMaterial, EquipmentSlot slotType, Map<Attribute, Supplier<AttributeModifier>> modifierMap, Properties properties, LevEnchSup... levEnchSups) {
        super(armorMaterial, slotType, properties, levEnchSups);
        Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();
        attributeModifiers.putAll(this.getDefaultAttributeModifiers(slotType));
        for(Map.Entry<Attribute, Supplier<AttributeModifier>> entry : modifierMap.entrySet()){
            attributeModifiers.put(entry.getKey(), entry.getValue().get());
        }
        this.defaultModifiers = attributeModifiers;
    }
}
