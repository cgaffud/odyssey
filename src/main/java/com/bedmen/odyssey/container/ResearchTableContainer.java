package com.bedmen.odyssey.container;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.tags.OdysseyItemTags;
import com.bedmen.odyssey.util.ContainerRegistry;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ResearchTableContainer extends Container {
    private final IInventory tableInventory;
    private final IIntArray tableData;

    public ResearchTableContainer(int p_i50095_1_, PlayerInventory p_i50095_2_) {
        this(p_i50095_1_, p_i50095_2_, new Inventory(11), new IntArray(2));
    }

    public ResearchTableContainer(int id, PlayerInventory playerInventory, IInventory tableInventory, IIntArray tableData) {
        super(ContainerRegistry.RESEARCH_TABLE.get(), id);
        checkContainerSize(tableInventory, 11);
        checkContainerDataCount(tableData, 2);
        this.tableInventory = tableInventory;
        this.tableData = tableData;
        for(int i = 0; i < 8; i++){
            this.addSlot(new ResearchTableContainer.ResearchSlot(tableInventory, i, 17+18*i, 17));
        }
        this.addSlot(new ResearchTableContainer.FuelSlot(tableInventory, 8, 17, 50));
        this.addSlot(new ResearchTableContainer.QuillSlot(tableInventory, 9, 53, 50));
        this.addSlot(new ResearchTableContainer.BookSlot(tableInventory, 10, 143, 50));
        this.addDataSlots(tableData);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

    }

    public boolean stillValid(PlayerEntity p_75145_1_) {
        return this.tableInventory.stillValid(p_75145_1_);
    }

    public ItemStack quickMoveStack(PlayerEntity p_82846_1_, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if(slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if(i > 10) {
                if(ResearchTableContainer.FuelSlot.mayPlaceItem(itemstack)) {
                    if(this.moveItemStackTo(itemstack1, 8, 9, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(ResearchTableContainer.QuillSlot.mayPlaceItem(itemstack)){
                    if(!this.moveItemStackTo(itemstack1, 9, 10, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(ResearchTableContainer.BookSlot.mayPlaceItem(itemstack)){
                    if(!this.moveItemStackTo(itemstack1, 10, 11, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if(!this.moveItemStackTo(itemstack1, 0, 7, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if(!this.moveItemStackTo(itemstack1, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(p_82846_1_, itemstack1);
        }

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getFuel() {
        return this.tableData.get(1);
    }

    @OnlyIn(Dist.CLIENT)
    public int getBrewingTicks() {
        return this.tableData.get(0);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getCurse() {
        return this.tableInventory.getItem(9).getItem() == ItemRegistry.MALEVOLENT_QUILL.get();
    }

    static class FuelSlot extends Slot {
        public FuelSlot(IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return mayPlaceItem(itemStack);
        }

        public static boolean mayPlaceItem(ItemStack itemStack) {
            return itemStack.getItem() == Items.INK_SAC;
        }

        public int getMaxStackSize() {
            return 64;
        }
    }

    static class ResearchSlot extends Slot {
        public ResearchSlot(IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean mayPlace(ItemStack p_75214_1_) {
            return true;
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class BookSlot extends Slot {
        public BookSlot(IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return mayPlaceItem(itemStack);
        }

        public static boolean mayPlaceItem(ItemStack itemStack) {
            return itemStack.getItem() == Items.BOOK;
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class QuillSlot extends Slot {
        public QuillSlot(IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return mayPlaceItem(itemStack);
        }

        public static boolean mayPlaceItem(ItemStack itemStack) {
            return OdysseyItemTags.QUILL_TAG.contains(itemStack.getItem());
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
