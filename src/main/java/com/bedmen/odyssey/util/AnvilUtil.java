package com.bedmen.odyssey.util;

import com.bedmen.odyssey.items.OdysseyShieldItem;
import net.minecraft.item.*;

public class AnvilUtil {

    public static int getRepairAmount(ItemStack itemStack){
        int i = getRepairFraction(itemStack);
        int maxDamage = itemStack.getMaxDamage();
        return (int) Math.ceil(((double)maxDamage)/((double)i));
    }

    private static int getRepairFraction(ItemStack itemStack){
        Item item = itemStack.getItem();
        if(item instanceof ShovelItem) return 1;
        if(item instanceof SwordItem || item instanceof HoeItem) return 2;
        if(item instanceof AxeItem || item instanceof PickaxeItem) return 3;
        if(item instanceof ArmorItem){
            switch(((ArmorItem)item).getSlot()){
                case CHEST:
                    return 8;
                case LEGS:
                    return 7;
                case HEAD:
                    return 5;
                default:
                    return 4;
            }
        }
        //if(item instanceof OdysseyBowItem) return check(BOW_ENCHANTS, id);
        //if(item instanceof OdysseyCrossbowItem) return check(CROSSBOW_ENCHANTS, id);
        //if(item instanceof OdysseyTridentItem) return check(TRIDENT_ENCHANTS, id);
        //if(item instanceof FishingRodItem) return check(FISHING_ROD_ENCHANTS, id);
        if(item instanceof OdysseyShieldItem) return 6;
        return 20;
    }
}
