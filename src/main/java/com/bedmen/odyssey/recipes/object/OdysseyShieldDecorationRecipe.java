package com.bedmen.odyssey.recipes.object;

import com.bedmen.odyssey.items.aspect_items.AspectShieldItem;
import com.bedmen.odyssey.registry.RecipeSerializerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class OdysseyShieldDecorationRecipe extends CustomRecipe {
    public OdysseyShieldDecorationRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    public boolean matches(CraftingContainer craftingContainer, Level level) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;

        for(int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack itemstack2 = craftingContainer.getItem(i);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof BannerItem) {
                    if (!itemstack1.isEmpty()) {
                        return false;
                    }

                    itemstack1 = itemstack2;
                } else {
                    if (!(itemstack2.getItem() instanceof AspectShieldItem)) {
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

    public ItemStack assemble(CraftingContainer craftingContainer) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;

        for(int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack itemstack2 = craftingContainer.getItem(i);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof BannerItem) {
                    itemstack = itemstack2;
                } else if (itemstack2.getItem() instanceof AspectShieldItem) {
                    itemstack1 = itemstack2.copy();
                }
            }
        }

        if (itemstack1.isEmpty()) {
            return itemstack1;
        } else {
            CompoundTag compoundnbt = itemstack.getTagElement("BlockEntityTag");
            CompoundTag compoundnbt1 = compoundnbt == null ? new CompoundTag() : compoundnbt.copy();
            compoundnbt1.putInt("Base", ((BannerItem)itemstack.getItem()).getColor().getId());
            itemstack1.addTagElement("BlockEntityTag", compoundnbt1);
            return itemstack1;
        }
    }

    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.SHIELD_DECORATION.get();
    }
}