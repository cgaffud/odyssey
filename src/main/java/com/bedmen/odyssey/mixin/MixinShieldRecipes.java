package com.bedmen.odyssey.mixin;

import com.bedmen.odyssey.items.NewShieldItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.crafting.ShieldRecipes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShieldRecipes.class)
public abstract class MixinShieldRecipes {

    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;

        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack2 = inv.getStackInSlot(i);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof BannerItem) {
                    if (!itemstack1.isEmpty()) {
                        return false;
                    }

                    itemstack1 = itemstack2;
                } else {
                    if (!(itemstack2.getItem() instanceof ShieldItem || itemstack2.getItem() instanceof NewShieldItem)) {
                        return false;
                    }

                    if (!itemstack.isEmpty()) {
                        return false;
                    }

                    if (itemstack2.getChildTag("BlockEntityTag") != null) {
                        return false;
                    }

                    itemstack = itemstack2;
                }
            }
        }

        return !itemstack.isEmpty() && !itemstack1.isEmpty();
    }

    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;

        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack2 = inv.getStackInSlot(i);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof BannerItem) {
                    itemstack = itemstack2;
                } else if (itemstack2.getItem() instanceof ShieldItem || itemstack2.getItem() instanceof NewShieldItem) {
                    itemstack1 = itemstack2.copy();
                }
            }
        }

        if (itemstack1.isEmpty()) {
            return itemstack1;
        } else {
            CompoundNBT compoundnbt = itemstack.getChildTag("BlockEntityTag");
            CompoundNBT compoundnbt1 = compoundnbt == null ? new CompoundNBT() : compoundnbt.copy();
            compoundnbt1.putInt("Base", ((BannerItem)itemstack.getItem()).getColor().getId());
            itemstack1.setTagInfo("BlockEntityTag", compoundnbt1);
            return itemstack1;
        }
    }

}
