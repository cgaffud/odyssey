package com.bedmen.odyssey.container;

import com.bedmen.odyssey.container.slots.InfuserResultSlot;
import com.bedmen.odyssey.recipes.EnchantedBookInfusingRecipe;
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
import net.minecraft.item.Items;
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

    public InfuserContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(11), new IntArray(3));
    }

    public InfuserContainer(int id, PlayerInventory playerInventory, IInventory inv, IIntArray infuserData) {
        super(ContainerRegistry.INFUSER.get(), id);
        this.recipeType = ModRecipeType.INFUSING;
        assertInventorySize(inv, 8);
        assertIntArraySize(infuserData, 3);
        this.inv = inv;
        this.infuserData = infuserData;
        this.world = playerInventory.player.world;
        InfuserContainer con = this;

        this.addSlot(new Slot(this.inv, 0, 121, 18) {
            public boolean isItemValid(ItemStack stack) {
                List<InfusingRecipe> list = con.world.getRecipeManager().getRecipesForType(ModRecipeType.INFUSING);
                for(int i1 = 0; i1 < list.size(); i1++) {
                    if(list.get(i1).getLens().test(stack)) {
                        return true;
                    }
                }
                List<EnchantedBookInfusingRecipe> list1 = con.world.getRecipeManager().getRecipesForType(ModRecipeType.ENCHANTED_BOOK_INFUSING);
                for(int i1 = 0; i1 < list1.size(); i1++) {
                    if(list1.get(i1).getLens().test(stack)) {
                        return true;
                    }
                }
                return false;
            }

            public int getSlotStackLimit(){
                return 1;
            }
        });

        int[] xpos = {62,84,84,62,40,40};
        int[] ypos = {37,48,70,81,70,48};

        for(int i = 0; i < 6; i++){
            this.addSlot(new Slot(this.inv, i+1, xpos[i], ypos[i]){
                public int getSlotStackLimit(){
                    return 1;
                }
            });
        }

        this.addSlot(new InfuserResultSlot(playerInventory.player, this.inv, 7, 62, 59){
            public boolean isItemValid(ItemStack stack) {
                List<InfusingRecipe> list = con.world.getRecipeManager().getRecipesForType(ModRecipeType.INFUSING);
                for(int i1 = 0; i1 < list.size(); i1++) {
                    if(list.get(i1).getBase().test(stack)) {
                        return true;
                    }
                }
                return stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK;
            }
        });

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
            if (index == 7) {
                if (!this.mergeItemStack(itemstack1, 8, 44, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index > 7) {
                if (index < 44) {
                    if (!this.mergeItemStack(itemstack1, 7, 8, false) && !this.mergeItemStack(itemstack1, 0, 7, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(itemstack1, 8, 44, false)) {
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
        return j != 0 && i != 0 ? i * 31 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean hasLight() {
        if(this.infuserData.get(2) > 0) return true;
        return false;
    }
}