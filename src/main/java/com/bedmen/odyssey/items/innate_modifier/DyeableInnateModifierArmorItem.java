package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.combat.InnateModifierArmorMaterial;
import com.bedmen.odyssey.modifier.InnateModifierHolder;
import com.bedmen.odyssey.modifier.ModifierInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeableLeatherItem;

import java.util.List;

public class DyeableInnateModifierArmorItem extends InnateModifierArmorItem implements DyeableLeatherItem {
    public DyeableInnateModifierArmorItem(Properties properties, InnateModifierArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<ModifierInstance> innateModifierList) {
        super(properties, armorMaterial, equipmentSlot, innateModifierList);
    }
}
