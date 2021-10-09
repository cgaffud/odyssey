package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import net.minecraft.item.IItemTier;

public class HatchetItem extends EquipmentMeleeItem {
    public HatchetItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builderIn, LevEnchSup... levEnchSups) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn, levEnchSups);
    }
}
