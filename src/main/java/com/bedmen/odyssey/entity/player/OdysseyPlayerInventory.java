package com.bedmen.odyssey.entity.player;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import java.lang.reflect.Field;

public class OdysseyPlayerInventory extends PlayerInventory{

    public final NonNullList<ItemStack> trinket = NonNullList.withSize(1, ItemStack.EMPTY);

    public OdysseyPlayerInventory(PlayerEntity p_i1750_1_) {
        super(p_i1750_1_);
        try {
            Field compartmentsField = PlayerInventory.class.getDeclaredField("compartments");
            compartmentsField.setAccessible(true);
            compartmentsField.set(this, ImmutableList.of(this.items, this.armor, this.offhand, this.trinket));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ListNBT save(ListNBT listNBT) {
        for(int i = 0; i < this.trinket.size(); ++i) {
            if (!this.trinket.get(i).isEmpty()) {
                CompoundNBT compoundnbt2 = new CompoundNBT();
                compoundnbt2.putByte("Slot", (byte)(i + 200));
                this.trinket.get(i).save(compoundnbt2);
                listNBT.add(compoundnbt2);
            }
        }
        super.save(listNBT);
        return listNBT;
    }

    public void load(ListNBT p_70443_1_) {
        this.items.clear();
        this.armor.clear();
        this.offhand.clear();
        this.trinket.clear();

        for(int i = 0; i < p_70443_1_.size(); ++i) {
            CompoundNBT compoundnbt = p_70443_1_.getCompound(i);
            int j = compoundnbt.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.of(compoundnbt);
            if (!itemstack.isEmpty()) {
                if (j >= 0 && j < this.items.size()) {
                    this.items.set(j, itemstack);
                } else if (j >= 100 && j < this.armor.size() + 100) {
                    this.armor.set(j - 100, itemstack);
                } else if (j >= 150 && j < this.offhand.size() + 150) {
                    this.offhand.set(j - 150, itemstack);
                } else if (j >= 200 && j < this.trinket.size() + 200) {
                    this.trinket.set(j - 200, itemstack);
                }
            }
        }
    }

    public int getContainerSize() {
        return super.getContainerSize() + this.trinket.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack2 : this.trinket) {
            if (!itemstack2.isEmpty()) {
                return false;
            }
        }
        return super.isEmpty();
    }
}