package com.bedmen.odyssey.util;

import com.bedmen.odyssey.items.QuiverItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

import java.util.function.Predicate;

public class BowUtil {

    public static float getStringSpeedModifier(ItemStack stack){
        CompoundNBT compoundnbt = stack.getTag();
        if(compoundnbt != null && compoundnbt.contains("StringType")){
            String s = compoundnbt.get("StringType").getString();
            switch(s){
                case "silver":
                    return 1.25f;
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

    public static void consumeQuiverAmmo(PlayerEntity playerEntity, ItemStack ammo){
        NonNullList<ItemStack> offhand = playerEntity.inventory.offHandInventory;

        for(ItemStack itemstack1 : offhand) {
            Item item = itemstack1.getItem();
            if (item instanceof QuiverItem) {
                CompoundNBT compoundNBT = itemstack1.getOrCreateTag();
                if (compoundNBT.contains("Items", 9)) {
                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize( ((QuiverItem) item).getSize(), ItemStack.EMPTY);
                    ItemStackHelper.loadAllItems(compoundNBT, nonnulllist);
                    for(int j = 0; j < nonnulllist.size(); j++){
                        ItemStack itemstack2 = nonnulllist.get(j);
                        if (ammo.getItem() == itemstack2.getItem()) {
                            itemstack2.shrink(1);
                            nonnulllist.set(j, itemstack2.copy());

                            ItemStack itemstack3 = itemstack1.copy();
                            ItemStackHelper.saveAllItems(compoundNBT, nonnulllist, false);
                            itemstack3.setTag(compoundNBT);
                            offhand.set(0, itemstack3);
                            return;
                        }
                    }
                }
            }
        }
    }

}
