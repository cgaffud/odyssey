package com.bedmen.odyssey.util;


import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class RecycleUtil {

    public static boolean pair(Item item1, Item item2){
        if(item1 == ItemRegistry.COPPER_NUGGET.get() && item2 == ItemRegistry.COPPER_INGOT.get())
            return true;
        if(item1 == Items.IRON_NUGGET && item2 == Items.IRON_INGOT)
            return true;
        if(item1 == Items.GOLD_NUGGET && item2 == Items.GOLD_INGOT)
            return true;
        if(item1 == ItemRegistry.SILVER_NUGGET.get() && item2 == ItemRegistry.SILVER_INGOT.get())
            return true;
        if(item1 == ItemRegistry.STERLING_SILVER_NUGGET.get() && item2 == ItemRegistry.STERLING_SILVER_INGOT.get())
            return true;
        if(item1 == ItemRegistry.NETHERITE_NUGGET.get() && item2 == Items.NETHERITE_INGOT)
            return true;
        return false;
    }

    public static ItemStack getIngot(ItemStack nuggetStack){
        Item nugget = nuggetStack.getItem();
        if(nugget == ItemRegistry.COPPER_NUGGET.get())
            return ItemRegistry.COPPER_INGOT.get().getDefaultInstance();
        if(nugget == Items.IRON_NUGGET)
            return Items.IRON_INGOT.getDefaultInstance();
        if(nugget == Items.GOLD_NUGGET)
            return Items.GOLD_INGOT.getDefaultInstance();
        if(nugget == ItemRegistry.SILVER_NUGGET.get())
            return ItemRegistry.SILVER_INGOT.get().getDefaultInstance();
        if(nugget == ItemRegistry.STERLING_SILVER_NUGGET.get())
            return ItemRegistry.STERLING_SILVER_INGOT.get().getDefaultInstance();
        if(nugget == ItemRegistry.NETHERITE_NUGGET.get())
            return Items.NETHERITE_INGOT.getDefaultInstance();
        return Items.COOKED_CHICKEN.getDefaultInstance();
    }

}
