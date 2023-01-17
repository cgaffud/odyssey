package com.bedmen.odyssey.items.odyssey_versions;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.combat.MeleeWeaponClass;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;

import java.util.List;

public class FastMeleeItem extends AspectMeleeItem {
    public FastMeleeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> additionalAbilityList, List<AspectInstance> innateModifierList, float speedBonus) {
        super(properties, tier, meleeWeaponClass, damage, additionalAbilityList, innateModifierList);
        this.attributeModifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier("Weapon modifier", speedBonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
