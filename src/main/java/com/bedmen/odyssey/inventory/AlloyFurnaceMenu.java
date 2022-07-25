package com.bedmen.odyssey.inventory;

import com.bedmen.odyssey.block.entity.AlloyFurnaceBlockEntity;
import com.bedmen.odyssey.inventory.slot.OdysseyFurnaceFuelSlot;
import com.bedmen.odyssey.inventory.slot.OdysseyFurnaceResultSlot;
import com.bedmen.odyssey.registry.ContainerRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AlloyFurnaceMenu extends OdysseyFurnaceMenu {
    public static final int SLOT_INPUT_0 = AlloyFurnaceBlockEntity.SLOT_INPUT_0;
    public static final int SLOT_INPUT_1 = AlloyFurnaceBlockEntity.SLOT_INPUT_1;
    public static final int SLOT_FUEL = AlloyFurnaceBlockEntity.SLOT_FUEL;
    public static final int SLOT_RESULT = AlloyFurnaceBlockEntity.SLOT_RESULT;
    public static final int SLOT_COUNT = AlloyFurnaceBlockEntity.SLOT_COUNT;
    public static final int DATA_COUNT = AlloyFurnaceBlockEntity.DATA_COUNT;
    private static final int USE_ROW_SLOT_START = SLOT_COUNT + 27;
    private static final int USE_ROW_SLOT_END = USE_ROW_SLOT_START + 9;

    public AlloyFurnaceMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
    }

    public AlloyFurnaceMenu(int id, Inventory playerInventory, Container container, ContainerData data) {
        super(ContainerRegistry.ALLOY_FURNACE.get(), id, container, data);
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(data, DATA_COUNT);
        this.addSlot(new Slot(container, SLOT_INPUT_0, 47, 17));
        this.addSlot(new Slot(container, SLOT_INPUT_1, 65, 17));
        this.addSlot(new OdysseyFurnaceFuelSlot(this, container, SLOT_FUEL, 56, 53));
        this.addSlot(new OdysseyFurnaceResultSlot(playerInventory.player, container, SLOT_RESULT, 116, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlots(data);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == SLOT_RESULT) {
                if (!this.moveItemStackTo(itemstack1, SLOT_COUNT, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > SLOT_RESULT) {
                if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, SLOT_FUEL, SLOT_RESULT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, SLOT_INPUT_0, SLOT_FUEL, false)) {
                        return ItemStack.EMPTY;
                } else if (index < USE_ROW_SLOT_START) {
                    if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < USE_ROW_SLOT_END && !this.moveItemStackTo(itemstack1, SLOT_COUNT, USE_ROW_SLOT_START, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, SLOT_COUNT, USE_ROW_SLOT_END, false)) {
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

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
