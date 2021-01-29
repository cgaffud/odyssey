package com.bedmen.odyssey.container;

import com.bedmen.odyssey.client.gui.NewEnchantmentScreen;
import com.bedmen.odyssey.util.ContainerRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;

import java.util.*;

public class NewEnchantmentContainer extends Container {
    private final IInventory inv;
    protected final World world;
    private final IIntArray tableData1;
    private final IIntArray tableData2;
    private List<String> idList = new ArrayList<String>();
    private List<Integer> levelList = new ArrayList<Integer>();

    public NewEnchantmentContainer(int p_i241921_4_, PlayerInventory p_i241921_5_) {
        this(p_i241921_4_, p_i241921_5_, new Inventory(3), new IntArray(96), new IntArray(96));
    }

    public NewEnchantmentContainer(int id, PlayerInventory playerInventory, IInventory inv, IIntArray tableData1, IIntArray tableData2) {
        super(ContainerRegistry.ENCHANTMENT.get(), id);
        assertInventorySize(inv, 3);
        this.inv = inv;
        this.world = playerInventory.player.world;
        assertIntArraySize(tableData1, 96);
        assertIntArraySize(tableData2, 96);
        this.tableData1 = tableData1;
        this.tableData2 = tableData2;
        NewEnchantmentContainer con = this;
        Slot slot0 = this.addSlot(new Slot(this.inv, 0, 15, 27){
            public int getSlotStackLimit() {
                return 1;
            }
        });
        Slot slot1 = this.addSlot(new Slot(this.inv, 1, 35, 27){
            public int getSlotStackLimit() {
                return 1;
            }
        });
        Slot slot2 = this.addSlot(new Slot(this.inv, 2, 35, 47) {
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem().equals(Items.LAPIS_LAZULI);
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.trackIntArray(tableData1);
        this.trackIntArray(tableData2);
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
            if (index >= 0 && index <= 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index > 2) {
                if(itemstack1.getItem().equals(Items.LAPIS_LAZULI)){
                    if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 3 && index < 39) {
                    if (!this.mergeItemStack(itemstack1, 0, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
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

    private void generateLists(){
        this.idList = new ArrayList<String>();
        this.levelList = new ArrayList<Integer>();
        Set<String> set = new HashSet<>();
        ItemStack itemStack = this.inv.getStackInSlot(0);

        for(int i = 0; i < this.tableData1.size(); i++){
            int i1 = this.tableData1.get(i);
            int i2 = this.tableData2.get(i);
            if(i1 != -1 && !set.contains(i1+" "+i2) && EnchantmentUtil.canBeApplied(itemStack,i1)){
                ItemStack itemStack1 = this.inv.getStackInSlot(1);
                if(itemStack1.isEmpty()){
                    set.add(i1+" "+i2);
                    this.idList.add(EnchantmentUtil.intToString(i1));
                    this.levelList.add(i2);
                } else if(EnchantmentUtil.stringToItem(EnchantmentUtil.intToString(i1)).getItem() == itemStack1.getItem()){
                    set.add(i1+" "+i2);
                    this.idList.add(EnchantmentUtil.intToString(i1));
                    this.levelList.add(i2);
                }
            }
        }
    }

    public int numPages(){
        this.generateLists();
        return (this.idList.size() + NewEnchantmentScreen.ENCHANT_PER_PAGE - 1)/(NewEnchantmentScreen.ENCHANT_PER_PAGE);
    }

    public List<String> getIdList(){
        return this.idList;
    }

    public List<Integer> getLevelList(){
        return this.levelList;
    }
}