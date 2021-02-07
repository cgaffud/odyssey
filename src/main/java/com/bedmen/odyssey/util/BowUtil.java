package com.bedmen.odyssey.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class BowUtil {

    public static float getStringSpeedModifier(ItemStack stack){
        CompoundNBT compoundnbt = stack.getTag();
        if(compoundnbt != null && compoundnbt.contains("StringType")){
            String s = compoundnbt.get("StringType").getString();
            switch(s){
                case "silver":
                    return 1.33333f;
                default:
                    return 1.0f;
            }
        }
        return 1.0f;
    }

    public static boolean flameString(ItemStack stack){
        CompoundNBT compoundnbt = stack.getTag();
        if(compoundnbt != null && compoundnbt.contains("StringType")){
            String s = compoundnbt.get("StringType").getString();
            switch(s){
                case "flame":
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

}
