package com.bedmen.odyssey.util;

import com.bedmen.odyssey.items.QuiverItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class BowUtil {

    public static String getStringType(ItemStack itemStack){
        CompoundNBT compoundnbt = itemStack.getTag();
        if(compoundnbt != null && compoundnbt.contains("StringType")){
            return compoundnbt.get("StringType").getAsString();
        }
        return "normal";
    }

    public static float getStringSpeedModifier(ItemStack itemStack){
        switch(getStringType(itemStack)){
            case "silver":
                return 1.25f;
            default:
                return 1.0f;
        }
    }

    public static boolean flameString(ItemStack itemStack){
        switch(getStringType(itemStack)){
            case "flame":
                return true;
            default:
                return false;
        }
    }

    public static boolean consumeQuiverAmmo(PlayerEntity playerEntity, ItemStack ammo){
        NonNullList<ItemStack> offhand = playerEntity.inventory.offhand;

        for(ItemStack itemstack1 : offhand) {
            Item item = itemstack1.getItem();
            if (item instanceof QuiverItem) {
                CompoundNBT compoundNBT = itemstack1.getOrCreateTag();
                if (compoundNBT.contains("Items", 9)) {
                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(((QuiverItem) item).getSize(), ItemStack.EMPTY);
                    ItemStackHelper.loadAllItems(compoundNBT, nonnulllist);
                    for(int j = 0; j < nonnulllist.size(); j++){
                        ItemStack itemstack2 = nonnulllist.get(j);
                        if (ammo.getItem() == itemstack2.getItem()) {
                            itemstack2.shrink(1);
                            nonnulllist.set(j, itemstack2.copy());
                            ItemStack itemstack3 = itemstack1.copy();
                            ItemStackHelper.saveAllItems(compoundNBT, nonnulllist, true);
                            itemstack3.setTag(compoundNBT);
                            offhand.set(0, itemstack3);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static ITextComponent getTranslationComponent(String s) {
        switch(s){
            case "silver":
                return (new TranslationTextComponent(ItemRegistry.SILVER_STRING.get().getDescriptionId())).withStyle(TextFormatting.WHITE);
            case "flame":
                return (new TranslationTextComponent(ItemRegistry.FLAME_STRING.get().getDescriptionId())).withStyle(TextFormatting.RED);
            default:
                return (new TranslationTextComponent(Items.STRING.getDescriptionId())).withStyle(TextFormatting.GRAY);
        }
    }
}
