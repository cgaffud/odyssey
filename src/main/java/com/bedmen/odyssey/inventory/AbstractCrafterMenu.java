package com.bedmen.odyssey.inventory;

import com.bedmen.odyssey.block.entity.AbstractCrafterBlockEntity;
import com.bedmen.odyssey.inventory.slot.OdysseyFurnaceFuelSlot;
import com.bedmen.odyssey.inventory.slot.OdysseyFurnaceResultSlot;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AbstractCrafterMenu extends AbstractContainerMenu {
    public static final int SLOT_INPUT = AbstractCrafterBlockEntity.SLOT_INPUT;
    public static final int SLOT_FUEL = AbstractCrafterBlockEntity.SLOT_FUEL;
    public static final int[][] SLOT_RESULTS = AbstractCrafterBlockEntity.SLOT_RESULTS;
    public static final int NUM_ROWS = AbstractCrafterBlockEntity.NUM_ROWS;
    public static final int NUM_COLUMNS = AbstractCrafterBlockEntity.NUM_COLUMNS;
    public static final int SLOT_COUNT = AbstractCrafterBlockEntity.SLOT_COUNT;
    public static final int DATA_COUNT = AbstractCrafterBlockEntity.DATA_COUNT;
    private static final int USE_ROW_SLOT_START = SLOT_COUNT + 27;
    private static final int USE_ROW_SLOT_END = USE_ROW_SLOT_START + 9;

    protected final Container container;
    protected final ContainerData data;

    public AbstractCrafterMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
    }

    public AbstractCrafterMenu(int id, Inventory inventory, Container container, ContainerData data) {
        super(ContainerRegistry.ABSTRACT_CRAFTER.get(), id);
        this.container = container;
        this.data = data;
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(data, DATA_COUNT);
        this.addSlot(new Slot(container, SLOT_INPUT, 34, 17));
        this.addSlot(new OdysseyFurnaceFuelSlot(this, container, SLOT_FUEL, 34, 53));

        for(int i = 0; i < NUM_ROWS; ++i) {
            for(int j = 0; j < NUM_COLUMNS; ++j) {
                this.addSlot(new OdysseyFurnaceResultSlot(inventory.player, container, SLOT_RESULTS[i][j], 90 + j * 18, 17 + i * 18));
            }
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.addDataSlots(data);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index >= SLOT_RESULTS[0][0] && index <= SLOT_RESULTS[NUM_ROWS-1][NUM_COLUMNS-1]) {
                if (!this.moveItemStackTo(itemstack1, SLOT_COUNT, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > SLOT_RESULTS[NUM_ROWS-1][NUM_COLUMNS-1]) {
                if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, SLOT_FUEL, SLOT_RESULTS[0][0], false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, SLOT_INPUT, SLOT_FUEL, false)) {
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

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public boolean isFuel(ItemStack itemStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(itemStack, RecipeTypeRegistry.RECYCLING.get()) > 0;
    }

    public int getBurnProgress() {
        int i = this.data.get(2);
        int j = this.data.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getLitProgress() {
        int i = this.data.get(1);
        if (i == 0) {
            i = 200;
        }
        return Integer.min(12, this.data.get(0) * 13 / i);
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }
}
