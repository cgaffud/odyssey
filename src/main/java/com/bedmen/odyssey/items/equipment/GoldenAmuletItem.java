package com.bedmen.odyssey.items.equipment;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class GoldenAmuletItem extends EquipmentTrinketItem {

    public GoldenAmuletItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer)
    {
        return true;
    }
}
