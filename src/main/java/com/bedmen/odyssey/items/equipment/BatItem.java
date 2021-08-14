package com.bedmen.odyssey.items.equipment;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;

public class BatItem extends EquipmentMeleeItem implements IVanishable {

    public BatItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, int level, Item.Properties builderIn) {
        super(tier, Enchantments.KNOCKBACK, attackDamageIn, attackSpeedIn, level, builderIn);
    }
}
