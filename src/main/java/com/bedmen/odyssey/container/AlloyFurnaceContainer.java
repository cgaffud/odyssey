package com.bedmen.odyssey.container;

import com.bedmen.odyssey.recipes.AlloyRecipe;
import com.bedmen.odyssey.recipes.ModRecipeType;
import com.bedmen.odyssey.tileentity.AlloyFurnaceTileEntity;
import com.bedmen.odyssey.util.ContainerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AlloyFurnaceContainer extends Container {
    private final IInventory furnaceInventory;
    private final IIntArray furnaceData;
    protected final World world;
    private final IRecipeType<AlloyRecipe> recipeType;

    public AlloyFurnaceContainer(int p_i241921_4_, PlayerInventory p_i241921_5_) {
        this(p_i241921_4_, p_i241921_5_, new Inventory(4), new IntArray(4));
    }

    public AlloyFurnaceContainer(int id, PlayerInventory playerInventory, IInventory furnaceInventory, IIntArray furnaceData) {
        super(ContainerRegistry.ALLOY_FURNACE.get(), id);
        this.recipeType = ModRecipeType.ALLOYING;
        assertInventorySize(furnaceInventory, 4);
        assertIntArraySize(furnaceData, 4);
        this.furnaceInventory = furnaceInventory;
        this.furnaceData = furnaceData;
        this.world = playerInventory.player.world;
        this.addSlot(new Slot(furnaceInventory, 0, 47, 17));
        this.addSlot(new Slot(furnaceInventory, 1, 65, 17));
        this.addSlot(new AlloyFurnaceFuelSlot(this, furnaceInventory, 2, 56, 53));
        this.addSlot(new ModFurnaceResultSlot(playerInventory.player, furnaceInventory, 3, 116, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.trackIntArray(furnaceData);
    }

    public void fillStackedContents(RecipeItemHelper itemHelperIn) {
        if (this.furnaceInventory instanceof IRecipeHelperPopulator) {
            ((IRecipeHelperPopulator)this.furnaceInventory).fillStackedContents(itemHelperIn);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 4;
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.furnaceInventory.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 3) {
                if (!this.mergeItemStack(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0 && index != 2) {
                if (this.isFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(itemstack1, 0, 2, false)) {
                    return ItemStack.EMPTY;
                } else if (index >= 4 && index < 31) {
                    if (!this.mergeItemStack(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 31 && index < 40 && !this.mergeItemStack(itemstack1, 4, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    protected boolean isFuel(ItemStack stack) {
        return AlloyFurnaceTileEntity.isFuel(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.furnaceData.get(2);
        int j = this.furnaceData.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int i = this.furnaceData.get(1);
        if (i == 0) {
            i = 200;
        }

        return Integer.min(12,this.furnaceData.get(0) * 13 / i);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.furnaceData.get(0) > 0;
    }
}