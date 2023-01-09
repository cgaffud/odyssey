package com.bedmen.odyssey.items.innate_aspect_items;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.weapon.MeleeWeaponClass;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;

import java.util.List;

public class FastInnateAspectMeleeItem extends InnateAspectMeleeItem {
    public FastInnateAspectMeleeItem(Properties properties, Tier tier, MeleeWeaponClass meleeWeaponClass, float damage, List<AspectInstance> innateAspectList, float speedBonus) {
        super(properties, tier, meleeWeaponClass, damage, innateAspectList);
        this.attributeModifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier("Weapon modifier", speedBonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
