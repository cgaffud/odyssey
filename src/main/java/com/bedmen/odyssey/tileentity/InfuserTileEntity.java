package com.bedmen.odyssey.tileentity;

import javax.annotation.Nullable;

import com.bedmen.odyssey.container.InfuserContainer;
import com.bedmen.odyssey.recipes.EnchantedBookInfusingRecipe;
import com.bedmen.odyssey.recipes.InfusingRecipe;
import com.bedmen.odyssey.recipes.ModRecipeType;
import com.bedmen.odyssey.util.TileEntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashMap;
import java.util.Map;

public class InfuserTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
    protected NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);
    private int cookTime;
    private int cookTimeTotal;
    protected final IIntArray infuserData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return InfuserTileEntity.this.cookTime;
                case 1:
                    return InfuserTileEntity.this.cookTimeTotal;
                case 2:
                    return InfuserTileEntity.this.hasLight() ? 1 : 0;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    InfuserTileEntity.this.cookTime = value;
                    break;
                case 1:
                    InfuserTileEntity.this.cookTimeTotal = value;
                    break;
                case 2:
                    break;
            }

        }

        public int size() {
            return 3;
        }
    };
    protected final IRecipeType<InfusingRecipe> recipeType;

    public InfuserTileEntity() {
        super(TileEntityTypeRegistry.INFUSER.get());
        this.recipeType = ModRecipeType.INFUSING;
    }

    private boolean hasLight() {
        if(this.world == null) return false;
        else return this.world.getLightSubtracted(this.pos.add(0,1,0), 0) >= 15;
    }

    public void read(BlockState state, CompoundNBT nbt) { //TODO: MARK
        super.read(state, nbt);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        this.cookTime = nbt.getInt("CookTime");
        this.cookTimeTotal = nbt.getInt("CookTimeTotal");

    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    public void tick() {
        boolean flag1 = false;

        if (!this.world.isRemote) {
            if (this.hasLight()) {
                IRecipe<?> irecipe1 = this.world.getRecipeManager().getRecipe(this.recipeType, this, this.world).orElse(null);
                IRecipe<?> irecipe2 = this.world.getRecipeManager().getRecipe(ModRecipeType.ENCHANTED_BOOK_INFUSING, this, this.world).orElse(null);

                if (this.canInfuse1(irecipe1)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.infuse1(irecipe1);
                        flag1 = true;
                    }
                } else if(this.canInfuse2(irecipe2)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.infuse2(irecipe2);
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.hasLight() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 1, 0, this.cookTimeTotal);
            }
        }

        if(flag1) this.markDirty();
    }

    protected boolean canInfuse1(@Nullable IRecipe<?> recipeIn) {
        if (recipeIn != null) {
            ItemStack itemstack = recipeIn.getRecipeOutput();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.items.get(7);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    protected boolean canInfuse2(@Nullable IRecipe<?> recipeIn) {
        if (recipeIn != null) {
            ItemStack itemstack = this.items.get(7);
            if (itemstack.isEmpty()) {
                return false;
            } else {
                int level = ((EnchantedBookInfusingRecipe)recipeIn).getLevel();
                if(level <= 1 && (itemstack.getItem() != Items.BOOK || itemstack.getCount() > 1)) return false;
                else if(level > 1 && itemstack.getItem() != Items.ENCHANTED_BOOK) return false;
                return true;
            }
        }
        return false;
    }

    private void infuse1(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canInfuse1(recipe)) {
            ItemStack itemstack1 = this.items.get(1);
            ItemStack itemstack2 = this.items.get(2);
            ItemStack itemstack3 = this.items.get(3);
            ItemStack itemstack4 = this.items.get(4);
            ItemStack itemstack5 = this.items.get(5);
            ItemStack itemstack6 = this.items.get(6);
            ItemStack itemstackRO = recipe.getRecipeOutput();
            ItemStack itemstack7 = this.items.get(7);

            if (itemstack7.isEmpty()) {
                this.items.set(7, itemstackRO.copy());
            } else if (itemstack7.getItem() == itemstackRO.getItem()) {
                itemstack7.grow(itemstackRO.getCount());
            }

            itemstack1.shrink(1);
            itemstack2.shrink(1);
            itemstack3.shrink(1);
            itemstack4.shrink(1);
            itemstack5.shrink(1);
            itemstack6.shrink(1);
        }
    }

    private void infuse2(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canInfuse2(recipe)) {
            ItemStack itemstack1 = this.items.get(1);
            ItemStack itemstack2 = this.items.get(2);
            ItemStack itemstack3 = this.items.get(3);
            ItemStack itemstack4 = this.items.get(4);
            ItemStack itemstack5 = this.items.get(5);
            ItemStack itemstack6 = this.items.get(6);
            ItemStack itemStackRO = recipe.getCraftingResult(null);

            this.items.set(7, itemStackRO.copy());

            itemstack1.shrink(1);
            itemstack2.shrink(1);
            itemstack3.shrink(1);
            itemstack4.shrink(1);
            itemstack5.shrink(1);
            itemstack6.shrink(1);
        }
    }

    protected int getCookTime() {
        return 100;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && index == 3;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index != 7 && !flag) {
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.markDirty();
        }

    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    public void clear() {
        this.items.clear();
    }

    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
    }

    @Nullable
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    public void onCrafting(PlayerEntity player) {
    }

    public void fillStackedContents(RecipeItemHelper helper) {
        for(ItemStack itemstack : this.items) {
            helper.accountStack(itemstack);
        }

    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void remove() {
        super.remove();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.oddc.infuser");
    }

    protected Container createMenu(int id, PlayerInventory player) {
        return new InfuserContainer(id, player, this, this.infuserData);
    }


}