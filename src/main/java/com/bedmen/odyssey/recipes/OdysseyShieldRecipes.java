package com.bedmen.odyssey.recipes;

import com.bedmen.odyssey.items.OdysseyShieldItem;
import com.bedmen.odyssey.registry.RecipeRegistry;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class OdysseyShieldRecipes extends SpecialRecipe {
    public OdysseyShieldRecipes(ResourceLocation p_i48160_1_) {
        super(p_i48160_1_);
    }

    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;

        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack2 = inv.getItem(i);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof BannerItem) {
                    if (!itemstack1.isEmpty()) {
                        return false;
                    }

                    itemstack1 = itemstack2;
                } else {
                    if (!(itemstack2.getItem() instanceof OdysseyShieldItem)) {
                        return false;
                    }

                    if (!itemstack.isEmpty()) {
                        return false;
                    }

                    if (itemstack2.getTagElement("BlockEntityTag") != null) {
                        return false;
                    }

                    itemstack = itemstack2;
                }
            }
        }

        return !itemstack.isEmpty() && !itemstack1.isEmpty();
    }

    public ItemStack assemble(CraftingInventory inv) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;

        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack2 = inv.getItem(i);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof BannerItem) {
                    itemstack = itemstack2;
                } else if (itemstack2.getItem() instanceof OdysseyShieldItem) {
                    itemstack1 = itemstack2.copy();
                }
            }
        }

        if (itemstack1.isEmpty()) {
            return itemstack1;
        } else {
            CompoundNBT compoundnbt = itemstack.getTagElement("BlockEntityTag");
            CompoundNBT compoundnbt1 = compoundnbt == null ? new CompoundNBT() : compoundnbt.copy();
            compoundnbt1.putInt("Base", ((BannerItem)itemstack.getItem()).getColor().getId());
            itemstack1.addTagElement("BlockEntityTag", compoundnbt1);
            return itemstack1;
        }
    }

    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ * p_194133_2_ >= 2;
    }

    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.SHIELD_DECORATION.get();
    }
}