package com.bedmen.odyssey.items.equipment;

import com.bedmen.odyssey.enchantment.LevEnchSup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;

public class DualWieldItem extends EquipmentMeleeItem {
    public DualWieldItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builderIn, LevEnchSup... levEnchSups) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn, levEnchSups);
    }

    public static boolean isDuelWieldingHatchets(PlayerEntity player){
        Item mainHandItem = player.getMainHandItem().getItem();
        return mainHandItem instanceof DualWieldItem && player.getOffhandItem().getItem() == mainHandItem;
    }
}
