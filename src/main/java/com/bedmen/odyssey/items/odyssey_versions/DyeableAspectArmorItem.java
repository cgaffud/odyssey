package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.combat.OdysseyArmorMaterial;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeableLeatherItem;

import java.util.List;

public class DyeableAspectArmorItem extends AspectArmorItem implements DyeableLeatherItem {
    public DyeableAspectArmorItem(Properties properties, OdysseyArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, List<AspectInstance> abilityList, List<AspectInstance> innateModifierList) {
        super(properties, armorMaterial, equipmentSlot, abilityList, innateModifierList);
    }
}
