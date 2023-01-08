package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import com.bedmen.odyssey.weapon.MeleeWeaponClass;
import com.bedmen.odyssey.items.equipment.base.EquipmentMeleeItem;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;

public class FastMeleeItem extends EquipmentMeleeItem {
    public FastMeleeItem(Properties builderIn, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, float speedBonus, LevEnchSup... levEnchSups) {
        super(builderIn, tier, meleeWeaponClass, damage, levEnchSups);
        this.attributeModifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier("Weapon modifier", speedBonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
