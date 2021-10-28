package com.bedmen.odyssey.util;

import com.bedmen.odyssey.container.QuiverContainer;
import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.registry.ContainerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

import java.util.HashMap;
import java.util.Map;

public class BowUtil {
    public static final float BASE_ARROW_VELOCITY = 3.0f;
    public static final Map<Integer, ContainerType<QuiverContainer>> QUIVER_MAP = new HashMap<>();

    public static void init(){
        QUIVER_MAP.put(3, ContainerRegistry.QUIVER3.get());
        QUIVER_MAP.put(5, ContainerRegistry.QUIVER5.get());
    }

    public static boolean consumeQuiverAmmo(PlayerEntity playerEntity, ItemStack ammo){
        NonNullList<ItemStack> offhand = playerEntity.inventory.offhand;

        for(ItemStack itemstack1 : offhand) {
            Item item = itemstack1.getItem();
            if (item instanceof QuiverItem) {
                CompoundNBT compoundNBT = itemstack1.getOrCreateTag();
                if (compoundNBT.contains("Items", 9)) {
                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(((QuiverItem) item).getQuiverType().getSize(), ItemStack.EMPTY);
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
}
