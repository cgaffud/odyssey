package com.bedmen.odyssey.container;

import com.bedmen.odyssey.items.QuiverItem;
import com.bedmen.odyssey.util.ContainerRegistry;
import com.bedmen.odyssey.util.ItemRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class QuiverContainer extends Container {

    protected final IInventory inv;
    protected final int size;
    protected final PlayerEntity player;

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public static QuiverContainer Quiver3(int id, PlayerInventory playerInventory){
        return new QuiverContainer(id, playerInventory, 3, ContainerRegistry.QUIVER3.get());
    }
    public static QuiverContainer Quiver5(int id, PlayerInventory playerInventory){
        return new QuiverContainer(id, playerInventory, 5, ContainerRegistry.QUIVER5.get());
    }
    public static QuiverContainer Quiver7(int id, PlayerInventory playerInventory){
        return new QuiverContainer(id, playerInventory, 7, ContainerRegistry.QUIVER7.get());
    }
    public static QuiverContainer Quiver9(int id, PlayerInventory playerInventory){
        return new QuiverContainer(id, playerInventory, 9, ContainerRegistry.QUIVER9.get());
    }

    public QuiverContainer(int id, PlayerInventory playerInventory, int size, ContainerType<?> type){
        this(id, playerInventory, size, ItemStack.EMPTY, type);
    }

    public QuiverContainer(int id, PlayerInventory playerInv, int size, ItemStack itemStack, ContainerType<?> type) {
        super(type, id);
        this.inv = new Inventory(size);
        this.size = size;
        this.player = playerInv.player;

        for(int k = 0; k < this.size; ++k) {
            this.addSlot(new Slot(inv, k, 89 + k * 18 - this.size * 9, 34){
                public boolean isItemValid(ItemStack stack) {
                    if(stack.getItem() instanceof ArrowItem) return true;
                    if(stack.getItem() instanceof SpectralArrowItem) return true;
                    if(stack.getItem() instanceof TippedArrowItem) return true;
                    if(stack.getItem() instanceof FireworkRocketItem) return true;
                    return false;
                }
            });
        }

        CompoundNBT compoundNBT = itemStack.getOrCreateTag();
        if (compoundNBT.contains("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(this.size, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(compoundNBT, nonnulllist);
            for(int i = 0; i < this.size; i++) {
                this.inv.setInventorySlotContents(i, nonnulllist.get(i));
            }
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142){
                public boolean canTakeStack(PlayerEntity playerIn) {
                    return !(getStack().getItem() instanceof QuiverItem);
                }
            });
        }
    }

    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        ItemStack itemStack1 = this.player.getHeldItemMainhand();
        boolean flag = false;
        if(!(itemStack1.getItem() instanceof QuiverItem)){
            itemStack1 = this.player.getHeldItemOffhand();
            flag = true;
        }
        CompoundNBT compoundNBT =  itemStack1.getOrCreateTag();
        compoundNBT.remove("Items");
        NonNullList<ItemStack> list = NonNullList.withSize(this.size, ItemStack.EMPTY);
        for(int i = 0; i < this.size; i++){
            list.set(i, this.inv.getStackInSlot(i));
        }
        ItemStackHelper.saveAllItems(compoundNBT, list, false);
        itemStack1.setTag(compoundNBT);
        if(flag) playerIn.setItemStackToSlot(EquipmentSlotType.OFFHAND, itemStack1);
        else playerIn.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemStack1);
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index >= this.size) {
                if (index < this.size+36) {
                    if (!this.mergeItemStack(itemstack1, 0, this.size, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(itemstack1, this.size, this.size+36, true)) {
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

    public int getSize(){
        return this.size;
    }

}
