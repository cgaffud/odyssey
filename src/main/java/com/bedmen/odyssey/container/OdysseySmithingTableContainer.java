package com.bedmen.odyssey.container;

import com.bedmen.odyssey.container.slots.OdysseySmithingTableSlot;
import com.bedmen.odyssey.util.BlockRegistry;
import com.bedmen.odyssey.util.ContainerRegistry;
import com.bedmen.odyssey.recipes.ModRecipeType;
import com.bedmen.odyssey.recipes.OdysseySmithingRecipe;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.World;

import java.util.List;

public class OdysseySmithingTableContainer extends Container {
    protected final CraftResultInventory resultInv = new CraftResultInventory();
    protected final IInventory inv = new Inventory(10) {
        /**
         * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void setChanged() {
            super.setChanged();
            OdysseySmithingTableContainer.this.slotsChanged(this);
        }
    };
    protected final IWorldPosCallable worldPos;
    protected final PlayerEntity player;
    private final World world;
    private OdysseySmithingRecipe recipe;
    private final IRecipeType<OdysseySmithingRecipe> recipeType;

    public OdysseySmithingTableContainer(int id, PlayerInventory playerInv) {
        this(id,playerInv,IWorldPosCallable.NULL);
    }

    public OdysseySmithingTableContainer(int id, PlayerInventory playerInv, IWorldPosCallable worldPos) {
        super(ContainerRegistry.SMITHING_TABLE.get(), id);
        this.worldPos = worldPos;
        this.player = playerInv.player;
        this.world = playerInv.player.level;
        this.recipeType = ModRecipeType.NEW_SMITHING;
        OdysseySmithingTableContainer con = this;
        Slot slot0 = this.addSlot(new Slot(this.inv, 0, 20, 35) {
            public boolean mayPlace(ItemStack stack) {
                List<OdysseySmithingRecipe> list = con.world.getRecipeManager().getAllRecipesFor(ModRecipeType.NEW_SMITHING);
                for(int i1 = 0; i1 < list.size(); i1++) {
                    if(list.get(i1).base.test(stack)) {
                        return true;
                    }
                }
                return false;
            }
        });


        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                int k = j+i*3;
                this.addSlot(new OdysseySmithingTableSlot(this.inv, 1+k, 57+18*j, 17+18*i) {

                    public boolean shouldBeUsed() {
                        List<OdysseySmithingRecipe> list = con.world.getRecipeManager().getAllRecipesFor(ModRecipeType.NEW_SMITHING);
                        if (slot0 != null && slot0.hasItem()) {
                            for(int i1 = 0; i1 < list.size(); i1++) {
                                if(list.get(i1).base.test(slot0.getItem())) {
                                    return !(list.get(i1).pattern.get(k) == Ingredient.EMPTY);
                                }
                            }
                        }
                        return false;
                    }

                    public boolean mayPlace(ItemStack stack) {
                        List<OdysseySmithingRecipe> list = con.world.getRecipeManager().getAllRecipesFor(ModRecipeType.NEW_SMITHING);
                        for(int i1 = 0; i1 < list.size(); i1++) {
                            if(list.get(i1).base.test(slot0.getItem())) {
                                if(list.get(i1).addition.test(stack) || list.get(i1).addition2.test(stack)) {
                                    return this.shouldBeUsed();
                                }
                            }
                        }
                        return false;
                    }
                });
            }
        }

        this.addSlot(new Slot(this.resultInv, 10, 139, 35) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            /**
             * Return whether this slot's stack can be taken from this slot.
             */
            public boolean mayPickup(PlayerEntity playerIn) {
                return OdysseySmithingTableContainer.this.mayPickup(playerIn, this.hasItem());
            }

            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                return OdysseySmithingTableContainer.this.onTake(thePlayer, stack);
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
    public void slotsChanged(IInventory inventoryIn) {
        super.slotsChanged(inventoryIn);
        if (inventoryIn == this.inv) {
            this.updateRepairOutput();
        }

    }

    /**
     * Called when the container is closed.
     */
    public void removed(PlayerEntity playerIn) {
        super.removed(playerIn);
        this.worldPos.execute((p_234647_2_, p_234647_3_) -> {
            this.clearContainer(playerIn, p_234647_2_, this.inv);
        });
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(PlayerEntity playerIn) {
        return this.worldPos.evaluate((p_234646_2_, p_234646_3_) -> {
            return !this.isValidBlock(p_234646_2_.getBlockState(p_234646_3_)) ? false : playerIn.distanceToSqr((double)p_234646_3_.getX() + 0.5D, (double)p_234646_3_.getY() + 0.5D, (double)p_234646_3_.getZ() + 0.5D) <= 64.0D;
        }, true);
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
            if (index == 10) {
                if (!this.moveItemStackTo(itemstack1, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > 10) {
                if (index >= 11 && index < 47) {
                    if (!this.moveItemStackTo(itemstack1, 0, 10, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 11, 47, false)) {
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

    protected boolean isValidBlock(BlockState p_230302_1_) {
        return p_230302_1_.is(BlockRegistry.SMITHING_TABLE.get());
    }

    protected boolean mayPickup(PlayerEntity p_230303_1_, boolean p_230303_2_) {
        return this.recipe != null && this.recipe.matches(this.inv, this.world);
    }

    protected ItemStack onTake(PlayerEntity p_230301_1_, ItemStack p_230301_2_) {
        p_230301_2_.onCraftedBy(p_230301_1_.level, p_230301_1_, p_230301_2_.getCount());
        this.resultInv.awardUsedRecipes(p_230301_1_);
        for(int i = 0; i < 10; i++) {
            this.shrinkSlot(i);
        }
        this.worldPos.execute((p_234653_0_, p_234653_1_) -> {
            p_234653_0_.levelEvent(1044, p_234653_1_, 0);
        });
        return p_230301_2_;
    }

    private void shrinkSlot(int index) {
        ItemStack itemstack = this.inv.getItem(index);
        if(!Ingredient.EMPTY.test(inv.getItem(index))) {
            itemstack.shrink(1);
        }
        this.inv.setItem(index, itemstack);
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    public void updateRepairOutput() {
        List<OdysseySmithingRecipe> list = this.world.getRecipeManager().getRecipesFor(this.recipeType, this.inv, this.world);
        if (list.isEmpty()) {
            this.resultInv.setItem(0, ItemStack.EMPTY);
        } else {
            this.recipe = list.get(0);
            ItemStack itemstack = this.recipe.assemble(this.inv);
            this.resultInv.setRecipeUsed(this.recipe);
            this.resultInv.setItem(0, itemstack);
        }

    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
     * null for the initial slot that was double-clicked.
     */
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container != this.resultInv && super.canTakeItemForPickAll(stack, slotIn);
    }
}