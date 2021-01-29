package com.bedmen.odyssey.container;

import com.bedmen.odyssey.recipes.InfusingRecipe;
import com.bedmen.odyssey.recipes.ModRecipeType;
import com.bedmen.odyssey.util.ContainerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class InfuserContainer extends Container {
    private final IInventory inv;
    private final IIntArray infuserData;
    protected final World world;
    private final IRecipeType<InfusingRecipe> recipeType;

    public InfuserContainer(int p_i241921_4_, PlayerInventory p_i241921_5_) {
        this(p_i241921_4_, p_i241921_5_, new Inventory(11), new IntArray(2));
    }

    public InfuserContainer(int id, PlayerInventory playerInventory, IInventory inv, IIntArray infuserData) {
        super(ContainerRegistry.INFUSER.get(), id);
        this.recipeType = ModRecipeType.INFUSING;
        assertInventorySize(inv, 11);
        assertIntArraySize(infuserData, 2);
        this.inv = inv;
        this.infuserData = infuserData;
        this.world = playerInventory.player.world;
        InfuserContainer con = this;
        Slot slot0 = this.addSlot(new Slot(this.inv, 0, 48, 17) {
            public boolean isItemValid(ItemStack stack) {
                List<InfusingRecipe> list = con.world.getRecipeManager().getRecipesForType(ModRecipeType.INFUSING);
                for(int i1 = 0; i1 < list.size(); i1++) {
                    if(list.get(i1).getLens().test(stack)) {
                        return true;
                    }
                }
                return false;
            }
        });

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                this.addSlot(new Slot(this.inv, j+i*3+1, 30+18*j, 49+18*i));
            }
        }

        this.addSlot(new ModFurnaceResultSlot(playerInventory.player, this.inv, 10, 124, 67));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 116 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 174));
        }

        this.trackIntArray(infuserData);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 2;
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.inv.isUsableByPlayer(playerIn);
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
            if (index == 10) {
                if (!this.mergeItemStack(itemstack1, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index > 10) {
                if (index >= 11 && index < 47) {
                    if (!this.mergeItemStack(itemstack1, 0, 10, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(itemstack1, 11, 47, false)) {
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

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.infuserData.get(0);
        int j = this.infuserData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
}