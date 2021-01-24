package com.bedmen.odyssey.container;

import javax.annotation.Nullable;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.bedmen.odyssey.util.ContainerRegistry;

import java.util.List;

public class NewBeaconContainer extends Container {
    private final IInventory tileBeacon = new Inventory(1) {
        /**
         * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
         * guis use Slot.isItemValid
         */
        public boolean isItemValidForSlot(int index, ItemStack stack) {
            return stack.getItem().isIn(ItemTags.BEACON_PAYMENT_ITEMS);
        }

        /**
         * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
         */
        public int getInventoryStackLimit() {
            return 1;
        }
    };
    private final NewBeaconContainer.BeaconSlot beaconSlot;
    private final IWorldPosCallable worldPosCallable;
    private final IIntArray beaconInfo;
    private boolean completionError1;
    private boolean completionError2;

    public NewBeaconContainer(int id, IInventory p_i50099_2_) {
        this(id, p_i50099_2_, new IntArray(11), IWorldPosCallable.DUMMY);
    }

    public NewBeaconContainer(int id, IInventory inventory, IIntArray beaconInfo, IWorldPosCallable worldPosCallable) {
        super(ContainerRegistry.BEACON.get(), id);
        assertIntArraySize(beaconInfo, 11);
        this.beaconInfo = beaconInfo;
        this.worldPosCallable = worldPosCallable;
        this.beaconSlot = new NewBeaconContainer.BeaconSlot(this, this.tileBeacon, 0, 116, 54);
        this.addSlot(this.beaconSlot);
        this.trackIntArray(beaconInfo);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.completionError1 = false;
        this.completionError2 = false;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        if (!playerIn.world.isRemote) {
            ItemStack itemstack = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit());
            if (!itemstack.isEmpty()) {
                playerIn.dropItem(itemstack, false);
            }

        }
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, Blocks.BEACON);
    }

    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
        this.detectAndSendChanges();
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
            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (this.mergeItemStack(itemstack1, 0, 1, false)) { //Forge Fix Shift Clicking in beacons with stacks larger then 1.
                return ItemStack.EMPTY;
            } else if (index >= 1 && index < 28) {
                if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 28 && index < 37) {
                if (!this.mergeItemStack(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 1, 37, false)) {
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
    public int getCompletion(int i) {
        return this.beaconInfo.get(i);
    }

    @OnlyIn(Dist.CLIENT)
    public int getBlocks() {
        int total = 0;
        for(int i = 0; i < 9; i++) total += getCompletion(i);
        return total;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public int getEffect() {
        return this.beaconInfo.get(9);
    }

    public void setEffect() {
        Effect e = this.beaconSlot.getEffect();
        int i = this.beaconSlot.getAmplifier();
        if (e != null && i != -1) {
            this.beaconInfo.set(9, Effect.getId(e));
            this.beaconInfo.set(10, i);
            this.beaconSlot.decrStackSize(1);
            this.tileBeacon.setInventorySlotContents(0,Items.GLASS_BOTTLE.getDefaultInstance());
        }
    }

    public boolean getCompletionError1(){
        return this.completionError1;
    }

    public void setCompletionError1(boolean b){
        this.completionError1 = b;
    }

    public boolean getCompletionError2(){
        return this.completionError2;
    }

    public void setCompletionError2(boolean b){
        this.completionError2 = b;
    }

    public int getAmplifier(){
        return this.beaconInfo.get(10);
    }

    public void setAmplifier(int value){
        this.beaconInfo.set(10, value);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean func_216970_h() {
        return !this.tileBeacon.getStackInSlot(0).isEmpty();
    }

    class BeaconSlot extends Slot {
        private NewBeaconContainer beacon;

        public BeaconSlot(NewBeaconContainer beacon, IInventory inventoryIn, int index, int xIn, int yIn) {
            super(inventoryIn, index, xIn, yIn);
            this.beacon = beacon;
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         */
        public boolean isItemValid(ItemStack stack) {
            if(stack.getItem().equals(Items.POTION)){
                EffectInstance effectInstance = effectListToEffect(PotionUtils.getEffectsFromStack(stack));
                if(effectInstance == null) return false;
                else if(effectInstance.getPotion().equals(Effects.INSTANT_DAMAGE)) return false;
                else if(effectInstance.getPotion().equals(Effects.INSTANT_HEALTH)) return false;
                else if(beacon.getCompletion(4) == 0) {
                    beacon.setCompletionError1(true);
                    return false;
                }
                else if(effectInstance.getPotion().equals(Effects.RESISTANCE)){
                    if(effectInstance.getAmplifier() == 2 || beacon.getBlocks() == 9) return true;
                    else{
                        beacon.setCompletionError2(true);
                        return false;
                    }
                }
                else if(effectInstance.getAmplifier() == 0 || beacon.getBlocks() == 9) return true;
                else{
                    beacon.setCompletionError2(true);
                    return false;
                }
            }
            return false;
        }

        /**
         * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the
         * case of armor slots)
         */
        public int getSlotStackLimit() {
            return 1;
        }

        public Effect getEffect(){
            ItemStack stack = this.getStack();
            if(stack != null && stack.getItem().equals(Items.POTION)){
                EffectInstance e = effectListToEffect(PotionUtils.getEffectsFromStack(this.getStack()));
                return e.getPotion();
            }
            return null;
        }

        public int getAmplifier(){
            ItemStack stack = this.getStack();
            if(stack != null && stack.getItem().equals(Items.POTION)){
                EffectInstance e = effectListToEffect(PotionUtils.getEffectsFromStack(this.getStack()));
                if(e.getPotion().equals(Effects.RESISTANCE)) return e.getAmplifier()-2;
                if(e.getPotion().equals(Effects.SLOWNESS) && e.getAmplifier() == 3) return 1;
                if(e.getPotion().equals(Effects.LEVITATION) && e.getAmplifier() == 3) return 1;
                return e.getAmplifier();
            }
            return -1;
        }

        public void onSlotChanged() {
            super.onSlotChanged();
            setEffect();
        }

        public EffectInstance effectListToEffect(List<EffectInstance> effects){
            if(effects.size() == 0) return null;
            if(effects.size() == 1) return effects.get(0);
            else return  effects.get(1);
        }

    }
}
