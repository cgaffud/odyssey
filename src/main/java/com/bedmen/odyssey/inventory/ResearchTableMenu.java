package com.bedmen.odyssey.inventory;

import com.bedmen.odyssey.block.entity.ResearchTableBlockEntity;
import com.bedmen.odyssey.items.QuillItem;
import com.bedmen.odyssey.items.TomeItem;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ResearchTableMenu extends AbstractContainerMenu {
    public static final int SLOT_INPUT = ResearchTableBlockEntity.SLOT_INPUT;
    public static final int SLOT_QUILL = ResearchTableBlockEntity.SLOT_QUILL;
    public static final int SLOT_INK = ResearchTableBlockEntity.SLOT_INK;
    public static final int SLOT_TOME = ResearchTableBlockEntity.SLOT_TOME;
    public static final int SLOT_COUNT = ResearchTableBlockEntity.SLOT_COUNT;
    public static final int DATA_COUNT = ResearchTableBlockEntity.DATA_COUNT;
    private static final int USE_ROW_SLOT_START = SLOT_COUNT + 27;
    private static final int USE_ROW_SLOT_END = USE_ROW_SLOT_START + 9;
    protected final Container container;
    protected final ContainerData data;

    public ResearchTableMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
    }

    public ResearchTableMenu(int id, Inventory playerInventory, Container container, ContainerData data) {
        super(ContainerRegistry.RESEARCH_TABLE.get(), id);
        this.container = container;
        this.data = data;
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(data, DATA_COUNT);
        this.addSlot(new Slot(container, SLOT_INPUT, 80, 17));
        this.addSlot(new Slot(container, SLOT_QUILL, 53, 50));
        this.addSlot(new Slot(container, SLOT_INK, 17, 50));
        this.addSlot(new Slot(container, SLOT_TOME, 143, 50));

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
            if (index < SLOT_COUNT) {
                if (!this.moveItemStackTo(itemstack1, SLOT_COUNT, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else {
                if (itemstack1.getItem() instanceof QuillItem) {
                    if (!this.moveItemStackTo(itemstack1, SLOT_QUILL, SLOT_INK, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.is(Items.GLOW_INK_SAC)) {
                    if (!this.moveItemStackTo(itemstack1, SLOT_INK, SLOT_TOME, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem() instanceof TomeItem) {
                    if (!this.moveItemStackTo(itemstack1, SLOT_TOME, SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, SLOT_INPUT, SLOT_QUILL, false)) {
                    return ItemStack.EMPTY;
                } else if (index < USE_ROW_SLOT_START) {
                    if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < USE_ROW_SLOT_END && !this.moveItemStackTo(itemstack1, SLOT_COUNT, USE_ROW_SLOT_START, false)) {
                    return ItemStack.EMPTY;
                }
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

    public int getResearchTicks() {
        return this.data.get(0);
    }

    public int getInk() {
        return this.data.get(1);
    }

    public boolean getCurse() {
        return this.container.getItem(SLOT_QUILL).getItem() instanceof QuillItem quillItem && quillItem.isCursed;
    }

    public boolean isResearching() {
        return this.data.get(0) > -1;
    }
}
