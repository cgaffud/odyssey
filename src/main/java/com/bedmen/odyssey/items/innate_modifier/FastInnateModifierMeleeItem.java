package com.bedmen.odyssey.items.innate_modifier;

import com.bedmen.odyssey.modifier.ModifierInstance;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;

import java.util.List;

public class FastInnateModifierMeleeItem extends InnateModifierMeleeItem {
    public FastInnateModifierMeleeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<ModifierInstance> innateModifierList, float speedBonus) {
        super(properties, tier, meleeWeaponClass, damage, innateModifierList);
        this.attributeModifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier("Weapon modifier", speedBonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
