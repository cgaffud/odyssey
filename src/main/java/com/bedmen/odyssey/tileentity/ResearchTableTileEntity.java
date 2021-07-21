package com.bedmen.odyssey.tileentity;

import java.util.Map;
import javax.annotation.Nullable;

import com.bedmen.odyssey.container.ResearchTableContainer;
import com.bedmen.odyssey.recipes.ModRecipeType;
import com.bedmen.odyssey.recipes.ResearchRecipe;
import com.bedmen.odyssey.util.TileEntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ResearchTableTileEntity extends LockableTileEntity implements ITickableTileEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY);
    private int researchTime;
    private int ink;
    protected final IIntArray dataAccess = new IIntArray() {
        public int get(int p_221476_1_) {
            switch(p_221476_1_) {
                case 0:
                    return ResearchTableTileEntity.this.researchTime;
                case 1:
                    return ResearchTableTileEntity.this.ink;
                default:
                    return 0;
            }
        }

        public void set(int p_221477_1_, int p_221477_2_) {
            switch(p_221477_1_) {
                case 0:
                    ResearchTableTileEntity.this.researchTime = p_221477_2_;
                    break;
                case 1:
                    ResearchTableTileEntity.this.ink = p_221477_2_;
            }

        }

        public int getCount() {
            return 2;
        }
    };
    protected final IRecipeType<ResearchRecipe> recipeType;

    public ResearchTableTileEntity() {
        super(TileEntityTypeRegistry.RESEARCH_TABLE.get());
        this.recipeType = ModRecipeType.RESEARCH;
    }

    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.oddc.research_table");
    }

    public int getContainerSize() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public void tick() {
        ItemStack itemstack = this.items.get(8);
        if (this.ink <= 0 && itemstack.getItem() == Items.INK_SAC) {
            this.ink = 5;
            itemstack.shrink(1);
            this.setChanged();
        }

        IRecipe<?> irecipe = this.level.getRecipeManager().getRecipeFor((IRecipeType<ResearchRecipe>)this.recipeType, this, this.level).orElse(null);
        boolean flag = this.isResearchable(irecipe);
        boolean flag1 = this.researchTime > 0;
        if (flag1) {
            --this.researchTime;
            boolean flag2 = this.researchTime == 0;
            if (flag2 && flag) {
                this.doResearch(irecipe);
                this.setChanged();
            } else if (!flag) {
                this.researchTime = 0;
                this.setChanged();
            }
        } else if (flag && this.ink > 0) {
            --this.ink;
            this.researchTime = 400;
            this.setChanged();
        }
    }

    private boolean isResearchable(@Nullable IRecipe<?> recipe) {
        if(recipe == null) return false;
        return this.getItem(9).getItem() == Items.BOOK;
    }

    private void doResearch(@Nullable IRecipe<?> recipe) {
        if (this.isResearchable(recipe)) {
            ItemStack itemstack = recipe.getResultItem();
            BlockPos blockpos = this.getBlockPos();
            this.items.set(9, itemstack);
            assert this.level != null;
            this.level.playLocalSound(blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
    }

    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(p_230337_2_, this.items);
        this.researchTime = p_230337_2_.getShort("ResearchTime");
        this.ink = p_230337_2_.getByte("Ink");
    }

    public CompoundNBT save(CompoundNBT p_189515_1_) {
        super.save(p_189515_1_);
        p_189515_1_.putShort("ResearchTime", (short)this.researchTime);
        ItemStackHelper.saveAllItems(p_189515_1_, this.items);
        p_189515_1_.putByte("Ink", (byte)this.ink);
        return p_189515_1_;
    }

    public ItemStack getItem(int p_70301_1_) {
        return p_70301_1_ >= 0 && p_70301_1_ < this.items.size() ? this.items.get(p_70301_1_) : ItemStack.EMPTY;
    }

    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        return ItemStackHelper.removeItem(this.items, p_70298_1_, p_70298_2_);
    }

    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return ItemStackHelper.takeItem(this.items, p_70304_1_);
    }

    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
        if (p_70299_1_ >= 0 && p_70299_1_ < this.items.size()) {
            this.items.set(p_70299_1_, p_70299_2_);
        }
    }

    public boolean stillValid(PlayerEntity p_70300_1_) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(p_70300_1_.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    public void clearContent() {
        this.items.clear();
    }

    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return new ResearchTableContainer(p_213906_1_, p_213906_2_, this, this.dataAccess);
    }
}
