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

        public int getCount() {
            return 3;
        }
    };
    protected final IRecipeType<InfusingRecipe> recipeType;

    public InfuserTileEntity() {
        super(TileEntityTypeRegistry.INFUSER.get());
        this.recipeType = ModRecipeType.INFUSING;
    }

    private boolean hasLight() {
        if(this.level == null) return false;
        else return this.level.getRawBrightness(this.worldPosition.offset(0,1,0), 0) >= 15;
    }

    public void load(BlockState state, CompoundNBT nbt) { //TODO: MARK
        super.load(state, nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        this.cookTime = nbt.getInt("CookTime");
        this.cookTimeTotal = nbt.getInt("CookTimeTotal");

    }

    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    public void tick() {
        boolean flag1 = false;

        if (!this.level.isClientSide) {
            if (this.hasLight()) {
                IRecipe<?> irecipe1 = this.level.getRecipeManager().getRecipeFor(this.recipeType, this, this.level).orElse(null);
                IRecipe<?> irecipe2 = this.level.getRecipeManager().getRecipeFor(ModRecipeType.ENCHANTED_BOOK_INFUSING, this, this.level).orElse(null);

                if (this.canInfuse(irecipe1)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.infuse(irecipe1);
                        flag1 = true;
                    }
                } else if(this.canInfuse(irecipe2)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.infuse(irecipe2);
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.hasLight() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 1, 0, this.cookTimeTotal);
            }
        }

        if(flag1) this.setChanged();
    }

    protected boolean canInfuse(@Nullable IRecipe<?> recipeIn) {
        if (recipeIn != null) {
            ItemStack itemstack = this.items.get(7);
            if (itemstack.isEmpty()) return false;
            else if(recipeIn instanceof InfusingRecipe){
                return ((InfusingRecipe)recipeIn).getBase().test(itemstack);
            } else if(recipeIn instanceof EnchantedBookInfusingRecipe) {
                int level = ((EnchantedBookInfusingRecipe)recipeIn).getLevel();
                if(level <= 1 && (itemstack.getItem() != Items.BOOK || itemstack.getCount() > 1)) return false;
                else if(level > 1 && itemstack.getItem() != Items.ENCHANTED_BOOK) return false;
                return true;
            }
            return false;
        }
        return false;
    }

    private void infuse(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canInfuse(recipe)) {
            ItemStack itemstack1 = this.items.get(1);
            ItemStack itemstack2 = this.items.get(2);
            ItemStack itemstack3 = this.items.get(3);
            ItemStack itemstack4 = this.items.get(4);
            ItemStack itemstack5 = this.items.get(5);
            ItemStack itemstack6 = this.items.get(6);
            ItemStack itemstackRO = recipe.assemble(null);

            this.items.set(7, itemstackRO.copy());

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
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && index == 3;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getContainerSize() {
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
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack removeItem(int index, int count) {
        return ItemStackHelper.removeItem(this.items, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(this.items, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.tagMatches(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (index != 7 && !flag) {
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.setChanged();
        }

    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean stillValid(PlayerEntity player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean canPlaceItem(int index, ItemStack stack) {
        return false;
    }

    public void clearContent() {
        this.items.clear();
    }

    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
    }

    @Nullable
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipes(PlayerEntity player) {
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
        if (!this.remove && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
    public void setRemoved() {
        super.setRemoved();
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