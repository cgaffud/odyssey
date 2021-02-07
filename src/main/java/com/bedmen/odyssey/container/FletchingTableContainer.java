package com.bedmen.odyssey.container;

import com.bedmen.odyssey.util.BlockRegistry;
import com.bedmen.odyssey.util.ContainerRegistry;
import com.bedmen.odyssey.recipes.ModRecipeType;
import com.bedmen.odyssey.recipes.FletchingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

public class FletchingTableContainer extends Container {
    protected final CraftResultInventory resultInv = new CraftResultInventory();
    protected final IInventory inv = new Inventory(5) {
        /**
         * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void markDirty() {
            super.markDirty();
            FletchingTableContainer.this.onCraftMatrixChanged(this);
        }
    };
    protected final IWorldPosCallable worldPos;
    protected final PlayerEntity player;
    private final World world;
    private FletchingRecipe recipe;
    private final IRecipeType<FletchingRecipe> recipeType;

    public FletchingTableContainer(int id, PlayerInventory playerInv) {
        this(id,playerInv,IWorldPosCallable.DUMMY);
    }

    public FletchingTableContainer(int id, PlayerInventory playerInv, IWorldPosCallable worldPos) {
        super(ContainerRegistry.FLETCHING_TABLE.get(), id);
        this.worldPos = worldPos;
        this.player = playerInv.player;
        this.world = playerInv.player.world;
        this.recipeType = ModRecipeType.FLETCHING;
        FletchingTableContainer con = this;
        Slot slot0 = this.addSlot(new Slot(this.inv, 0, 20, 35) {
            public boolean isItemValid(ItemStack stack) {
                List<FletchingRecipe> list = con.world.getRecipeManager().getRecipesForType(ModRecipeType.FLETCHING);
                for(int i1 = 0; i1 < list.size(); i1++) {
                    if(list.get(i1).base.test(stack)) {
                        return true;
                    }
                }
                return false;
            }
        });


        for(int i = 0; i < 3; i++) {
            this.addSlot(new Slot(this.inv, 1+i, 57+18*i, 53-18*i) {

                public boolean isItemValid(ItemStack stack) {
                    List<FletchingRecipe> list = con.world.getRecipeManager().getRecipesForType(ModRecipeType.FLETCHING);
                    for(int i1 = 0; i1 < list.size(); i1++) {
                        if(list.get(i1).addition.test(stack)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        this.addSlot(new Slot(this.resultInv, 4, 139, 35) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            /**
             * Return whether this slot's stack can be taken from this slot.
             */
            public boolean canTakeStack(PlayerEntity playerIn) {
                return FletchingTableContainer.this.func_230303_b_(playerIn, this.getHasStack());
            }

            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                return FletchingTableContainer.this.func_230301_a_(thePlayer, stack);
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        super.onCraftMatrixChanged(inventoryIn);
        if (inventoryIn == this.inv) {
            this.updateRepairOutput();
        }

    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.worldPos.consume((p_234647_2_, p_234647_3_) -> {
            this.clearContainer(playerIn, p_234647_2_, this.inv);
        });
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.worldPos.applyOrElse((p_234646_2_, p_234646_3_) -> {
            return !this.func_230302_a_(p_234646_2_.getBlockState(p_234646_3_)) ? false : playerIn.getDistanceSq((double)p_234646_3_.getX() + 0.5D, (double)p_234646_3_.getY() + 0.5D, (double)p_234646_3_.getZ() + 0.5D) <= 64.0D;
        }, true);
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
            if (index == 4) {
                if (!this.mergeItemStack(itemstack1, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index > 4) {
                if (index >= 5 && index < 41) {
                    if (!this.mergeItemStack(itemstack1, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(itemstack1, 5, 41, false)) {
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

    protected boolean func_230302_a_(BlockState p_230302_1_) {
        return p_230302_1_.isIn(BlockRegistry.FLETCHING_TABLE.get());
    }

    protected boolean func_230303_b_(PlayerEntity p_230303_1_, boolean p_230303_2_) {
        return this.recipe != null && this.recipe.matches(this.inv, this.world);
    }

    protected ItemStack func_230301_a_(PlayerEntity p_230301_1_, ItemStack p_230301_2_) {
        p_230301_2_.onCrafting(p_230301_1_.world, p_230301_1_, p_230301_2_.getCount());
        this.resultInv.onCrafting(p_230301_1_);
        for(int i = 0; i < 4; i++) {
            this.shrinkSlot(i);
        }
        this.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_HIT, SoundCategory.BLOCKS, 1.0F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        return p_230301_2_;
    }

    private void shrinkSlot(int index) {
        ItemStack itemstack = this.inv.getStackInSlot(index);
        if(!Ingredient.EMPTY.test(inv.getStackInSlot(index))) {
            itemstack.shrink(1);
        }
        this.inv.setInventorySlotContents(index, itemstack);
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    public void updateRepairOutput() {
        List<FletchingRecipe> list = this.world.getRecipeManager().getRecipes(this.recipeType, this.inv, this.world);
        if (list.isEmpty()) {
            this.resultInv.setInventorySlotContents(0, ItemStack.EMPTY);
        } else {
            this.recipe = list.get(0);
            ItemStack itemstack = this.recipe.getCraftingResult(this.inv);
            this.resultInv.setRecipeUsed(this.recipe);
            this.resultInv.setInventorySlotContents(0, itemstack);

        }
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
     * null for the initial slot that was double-clicked.
     */
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.resultInv && super.canMergeSlot(stack, slotIn);
    }
}