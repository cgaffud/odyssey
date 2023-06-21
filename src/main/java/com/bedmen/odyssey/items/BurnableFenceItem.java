package com.bedmen.odyssey.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item.Properties;

public class BurnableFenceItem extends BlockItem {
    public BurnableFenceItem(Block p_i48527_1_, Properties p_i48527_2_) {
        super(p_i48527_1_, p_i48527_2_);
    }

    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType)
    {
        return 300;
    }
}
