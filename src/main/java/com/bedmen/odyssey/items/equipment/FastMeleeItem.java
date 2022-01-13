package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;

public class FastMeleeItem extends EquipmentMeleeItem {
    public FastMeleeItem(Tier tier, float attackDamageIn, float attackSpeedIn, float speedBonus, boolean canSweep, Properties builderIn, LevEnchSup... levEnchSups) {
        super(tier, attackDamageIn, attackSpeedIn, canSweep, builderIn, levEnchSups);
        this.attributeModifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier("Weapon modifier", (double)speedBonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
