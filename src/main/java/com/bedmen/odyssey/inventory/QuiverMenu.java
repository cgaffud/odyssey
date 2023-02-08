package com.bedmen.odyssey.inventory;

import com.bedmen.odyssey.combat.QuiverType;
import com.bedmen.odyssey.items.aspect_items.QuiverItem;
import com.bedmen.odyssey.registry.ContainerRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Predicate;

public class QuiverMenu extends AbstractContainerMenu {

    protected final Container inv;
    protected final int size;
    protected final Player player;
    protected final Predicate<ItemStack> allowedItems;
    public static final Predicate<ItemStack> ROCKET_ONLY = (itemStack1 -> itemStack1.getItem() == Items.FIREWORK_ROCKET);

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public static MenuType.MenuSupplier<QuiverMenu> QuiverMaker(QuiverType quiverType){
        return (int pContainerId, Inventory pPlayerInventory) ->  new QuiverMenu(pContainerId, pPlayerInventory, quiverType.size, quiverType.isRocketBag, ContainerRegistry.QUIVER_MAP.get(quiverType));
    }

    public QuiverMenu(int id, Inventory playerInventory, int size, boolean isRocketBag, MenuType<?> type){
        this(id, playerInventory, size, isRocketBag, ItemStack.EMPTY, type);
    }

    public QuiverMenu(int id, Inventory playerInv, int size, boolean isRocketBag, ItemStack itemStack, MenuType<?> type) {
        super(type, id);
        this.inv = new SimpleContainer(size);
        this.size = size;
        this.player = playerInv.player;
        if(isRocketBag){
            this.allowedItems = ROCKET_ONLY;
        } else {
            this.allowedItems = CrossbowItem.ARROW_OR_FIREWORK;
        }

        for(int k = 0; k < this.size; ++k) {
            this.addSlot(new Slot(inv, k, 89 + k * 18 - this.size * 9, 34){
                public boolean mayPlace(ItemStack stack) {
                    return allowedItems.test(stack);
                }
            });
        }

        CompoundTag compoundNBT = itemStack.getOrCreateTag();
        if (compoundNBT.contains("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(this.size, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(compoundNBT, nonnulllist);
            for(int i = 0; i < this.size; i++) {
                this.inv.setItem(i, nonnulllist.get(i));
            }
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142){
                public boolean mayPickup(Player playerIn) {
                    return !(getItem().getItem() instanceof QuiverItem);
                }
            });
        }
    }

    public void removed(Player playerIn) {
        super.removed(playerIn);
        ItemStack itemStack = this.player.getMainHandItem();
        EquipmentSlot equipmentSlot = EquipmentSlot.MAINHAND;
        if(!(itemStack.getItem() instanceof QuiverItem)){
            itemStack = this.player.getOffhandItem();
            equipmentSlot = EquipmentSlot.OFFHAND;
        }
        CompoundTag compoundNBT =  itemStack.getOrCreateTag();
        compoundNBT.remove("Items");
        NonNullList<ItemStack> list = NonNullList.withSize(this.size, ItemStack.EMPTY);
        for(int i = 0; i < this.size; i++){
            list.set(i, this.inv.getItem(i));
        }
        ContainerHelper.saveAllItems(compoundNBT, list, false);
        itemStack.setTag(compoundNBT);
        playerIn.setItemSlot(equipmentSlot, itemStack);
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index >= this.size) {
                if (index < this.size+36) {
                    if (!this.moveItemStackTo(itemstack1, 0, this.size, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, this.size, this.size+36, true)) {
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

    public int getSize(){
        return this.size;
    }
}
