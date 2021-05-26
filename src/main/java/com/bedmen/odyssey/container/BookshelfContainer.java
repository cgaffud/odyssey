package com.bedmen.odyssey.container;

import com.bedmen.odyssey.util.ContainerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BookshelfContainer extends Container {
    private final IInventory inv;
    protected final World world;

    public BookshelfContainer(int p_i241921_4_, PlayerInventory p_i241921_5_) {
        this(p_i241921_4_, p_i241921_5_, new Inventory(3));
    }

    public BookshelfContainer(int id, PlayerInventory playerInventory, IInventory inv) {
        super(ContainerRegistry.BOOKSHELF.get(), id);
        checkContainerSize(inv, 3);
        this.inv = inv;
        this.world = playerInventory.player.level;
        BookshelfContainer con = this;

        for(int i = 0; i < 3; i++){
            this.addSlot(new Slot(this.inv, i, 44+i*36, 34) {
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem().equals(Items.ENCHANTED_BOOK);
                }
            });
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(PlayerEntity playerIn) {
        return this.inv.stillValid(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index >= 0 && index <= 2) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > 2) {
                if (index >= 3 && index < 39) {
                    if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}