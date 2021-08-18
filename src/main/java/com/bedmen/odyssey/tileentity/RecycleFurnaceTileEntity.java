package com.bedmen.odyssey.tileentity;

import com.bedmen.odyssey.blocks.RecycleFurnaceBlock;
import com.bedmen.odyssey.container.RecycleFurnaceContainer;
import com.bedmen.odyssey.recipes.ModRecipeType;
import com.bedmen.odyssey.recipes.RecycleRecipe;
import com.bedmen.odyssey.util.RecycleUtil;
import com.bedmen.odyssey.registry.TileEntityTypeRegistry;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

public class RecycleFurnaceTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
    private static final int[] SLOTS_UP = new int[]{0};
    private static final int[] SLOTS_DOWN = new int[]{2,3};
    private static final int[] SLOTS_HORIZONTAL = new int[]{1};
    protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private int burnTime;
    private int recipesUsed;
    private int cookTime;
    private int cookTimeTotal;
    protected final IIntArray furnaceData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return RecycleFurnaceTileEntity.this.burnTime;
                case 1:
                    return RecycleFurnaceTileEntity.this.recipesUsed;
                case 2:
                    return RecycleFurnaceTileEntity.this.cookTime;
                case 3:
                    return RecycleFurnaceTileEntity.this.cookTimeTotal;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    RecycleFurnaceTileEntity.this.burnTime = value;
                    break;
                case 1:
                    RecycleFurnaceTileEntity.this.recipesUsed = value;
                    break;
                case 2:
                    RecycleFurnaceTileEntity.this.cookTime = value;
                    break;
                case 3:
                    RecycleFurnaceTileEntity.this.cookTimeTotal = value;
            }

        }

        public int getCount() {
            return 4;
        }
    };
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    protected final IRecipeType<RecycleRecipe> recipeType;

    public RecycleFurnaceTileEntity() {
        super(TileEntityTypeRegistry.RECYCLE_FURNACE.get());
        this.recipeType = ModRecipeType.RECYCLING;
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public void load(BlockState state, CompoundNBT nbt) { //TODO: MARK
        super.load(state, nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        this.burnTime = nbt.getInt("BurnTime");
        this.cookTime = nbt.getInt("CookTime");
        this.cookTimeTotal = nbt.getInt("CookTimeTotal");
        this.recipesUsed = this.getBurnTime(this.items.get(2));
        CompoundNBT compoundnbt = nbt.getCompound("RecipesUsed");

        for(String s : compoundnbt.getAllKeys()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }

    }

    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        ItemStackHelper.saveAllItems(compound, this.items);
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        compound.put("RecipesUsed", compoundnbt);
        return compound;
    }

    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!this.level.isClientSide) {
            ItemStack itemstack = this.items.get(1);
            if (this.isBurning() || !itemstack.isEmpty() && !this.items.get(0).isEmpty()) {
                IRecipe<?> irecipe = this.level.getRecipeManager().getRecipeFor((IRecipeType<RecycleRecipe>)this.recipeType, this, this.level).orElse(null);
                if (!this.isBurning() && this.canSmelt(irecipe)) {
                    this.burnTime = this.getBurnTime(itemstack);
                    this.recipesUsed = this.burnTime;
                    if (this.isBurning()) {
                        flag1 = true;
                        if (itemstack.hasContainerItem())
                            this.items.set(1, itemstack.getContainerItem());
                        else
                        if (!itemstack.isEmpty()) {
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                this.items.set(1, itemstack.getContainerItem());
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt(irecipe)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.smelt(irecipe);
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(RecycleFurnaceBlock.LIT, Boolean.valueOf(this.isBurning())), 3);
            }
        }

        if (flag1) {
            this.setChanged();
        }

    }

    protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
        RecycleRecipe recycleRecipe = (RecycleRecipe)recipeIn;
        if (!this.items.get(0).isEmpty() && recipeIn != null) {
            ItemStack itemstack = recipeIn.getResultItem();
            int n = recycleRecipe.getRecipeOutputCount(this);
            ItemStack itemstackIngot = recycleRecipe.getRecipeOutputIngot();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.items.get(2);
                ItemStack itemstack2  = this.items.get(3);
                if (itemstack1.isEmpty()) {
                    if(itemstack2.isEmpty()) {
                        return n <= 10*64;
                    }
                    if (!itemstack2.sameItem(itemstackIngot)){
                        return n <= 64;
                    }
                    return n <= 9*(64-itemstack2.getCount()) + 64;
                } else if (!itemstack1.sameItem(itemstack)) {
                    if (n % 9 == 0) {
                        if (itemstack2.isEmpty())
                            return n <= 9 * 64;
                        if (!itemstack2.sameItem(itemstackIngot))
                            return false;
                        return n <= 9 * (64 - itemstack2.getCount());
                    }
                    return false;
                } else if(n + itemstack1.getCount() <= 64)
                    return true;
                else if(!itemstack2.sameItem(itemstackIngot))
                    return false;
                return n <= 10*64 - itemstack1.getCount() - itemstack2.getCount() * 9;
            }
        } else {
            return false;
        }
    }

    private void smelt(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            RecycleRecipe recycleRecipe = (RecycleRecipe) recipe;
            ItemStack itemstack = this.items.get(0);
            ItemStack itemstackOut = recipe.getResultItem();
            ItemStack itemstack1 = this.items.get(2);
            ItemStack itemstack2 = this.items.get(3);
            int n = recycleRecipe.getRecipeOutputCount(this);
            ItemStack itemstackIngot = recycleRecipe.getRecipeOutputIngot();

            if (itemstack2.isEmpty() && n >= 9) {
                this.items.set(3, itemstackIngot.copy());
                itemstack2 = this.items.get(3);
                n -= 9;
            }
            if (itemstack2.sameItem(itemstackIngot) && n >= 9) {
                int a = Integer.min(64 - itemstack2.getCount(), n / 9);
                itemstack2.grow(a);
                n -= 9 * a;
            }
            if (itemstack1.isEmpty() && n >= 1) {
                this.items.set(2, itemstackOut.copy());
                itemstack1 = this.items.get(2);
                n -= 1;
            }
            if (itemstack1.sameItem(itemstackOut) && n >= 1) {
                itemstack1.grow(n);
            }

            if (!this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }

            itemstack.shrink(1);

            if (RecycleUtil.pair(itemstack1.getItem(), itemstack2.getItem())) {
                while (itemstack1.getCount() >= 9 && itemstack2.getCount() < 64) {
                    itemstack1.shrink(9);
                    itemstack2.grow(1);
                }
            }
        }
    }

    protected int getBurnTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(fuel)/2;
        }
    }

    protected int getCookTime() {
        return this.level.getRecipeManager().getRecipeFor((IRecipeType<RecycleRecipe>)this.recipeType, this, this.level).map(RecycleRecipe::getCookTime).orElse(100);
    }

    public static boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
    }

    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        } else {
            return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
        }
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
        return direction == Direction.DOWN && (index == 3 || index == 2);
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

        if (index == 0 && !flag) {
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
        if (index > 1) {
            return false;
        } else if (index != 1) {
            return true;
        } else {
            return isFuel(stack);
        }
    }

    public void clearContent() {
        this.items.clear();
    }

    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }

    }

    @Nullable
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipes(PlayerEntity player) {
    }

    public void unlockRecipes(PlayerEntity player) {
        List<IRecipe<?>> list = this.grantStoredRecipeExperience(player.level, player.position());
        player.awardRecipes(list);
        this.recipes.clear();
    }

    public List<IRecipe<?>> grantStoredRecipeExperience(World world, Vector3d pos) {
        List<IRecipe<?>> list = Lists.newArrayList();

        for(Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
            world.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                splitAndSpawnExperience(world, pos, entry.getIntValue(), ((RecycleRecipe)recipe).getExperience());
            });
        }

        return list;
    }

    private static void splitAndSpawnExperience(World world, Vector3d pos, int craftedAmount, float experience) {
        int i = MathHelper.floor((float)craftedAmount * experience);
        float f = MathHelper.frac((float)craftedAmount * experience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        while(i > 0) {
            int j = ExperienceOrbEntity.getExperienceValue(i);
            i -= j;
            world.addFreshEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, j));
        }

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
        this.burnTime = 0;
        super.setRemoved();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.oddc.recycle_furnace");
    }

    protected Container createMenu(int id, PlayerInventory player) {
        return new RecycleFurnaceContainer(id, player, this, this.furnaceData);
    }

}