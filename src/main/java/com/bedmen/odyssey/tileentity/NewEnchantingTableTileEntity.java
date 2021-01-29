package com.bedmen.odyssey.tileentity;

import javax.annotation.Nullable;

import com.bedmen.odyssey.container.NewEnchantmentContainer;
import com.bedmen.odyssey.recipes.InfusingRecipe;
import com.bedmen.odyssey.recipes.ModRecipeType;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.util.TileEntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import java.util.Random;

public class NewEnchantingTableTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    private final int[] X_POS = {2,2,2,1,0,-1,-2,-2,-2,-2,-2,-1,0,1,2,2};
    private final int[] Z_POS = {0,1,2,2,2,2,2,1,0,-1,-2,-2,-2,-2,-2,-1};
    protected int[] enchantmentIds = new int[96];
    protected int[] levelNums = new int[96];
    protected final IIntArray tableData1 = new IIntArray(){

        @Override
        public int get(int index) {
            if(0 <= index && index < 96) return enchantmentIds[index];
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if(0 <= index && index < 96) enchantmentIds[index] = value;
        }

        @Override
        public int size() {
            return 96;
        }
    };
    protected final IIntArray tableData2 = new IIntArray(){

        @Override
        public int get(int index) {
            if(0 <= index && index < 96) return levelNums[index];
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if(0 <= index && index < 96) levelNums[index] = value;
        }

        @Override
        public int size() {
            return 96;
        }
    };
    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    protected final IRecipeType<InfusingRecipe> recipeType;
    public int ticks;
    public float field_195523_f;
    public float field_195524_g;
    public float field_195525_h;
    public float field_195526_i;
    public float nextPageTurningSpeed;
    public float pageTurningSpeed;
    public float nextPageAngle;
    public float pageAngle;
    public float field_195531_n;
    private static final Random random = new Random();

    public NewEnchantingTableTileEntity() {
        super(TileEntityTypeRegistry.ENCHANTING_TABLE.get());
        this.recipeType = ModRecipeType.INFUSING;
    }

    public void read(BlockState state, CompoundNBT nbt) { //TODO: MARK
        super.read(state, nbt);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);

    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    private void getEnchantments(int y, int i){
        TileEntity tileEntity = world.getTileEntity(this.pos.add(this.X_POS[i],y,this.Z_POS[i]));
        if(tileEntity instanceof BookshelfTileEntity){
            NonNullList<ItemStack> items = ((BookshelfTileEntity)tileEntity).getItems();
            for(int j = 0; j < 3; j++){
                if(items.get(j).getItem() == Items.ENCHANTED_BOOK){
                    ListNBT listNBT = EnchantedBookItem.getEnchantments(items.get(j));
                    if(!listNBT.isEmpty()){
                        String s = listNBT.get(0).toString();
                        int index1 = s.indexOf("lvl:");
                        int index2 = s.indexOf('s');
                        int level = Integer.parseInt(s.substring(index1+4,index2));
                        index1 = s.indexOf("id:\"");
                        index2 = s.indexOf('\"', index1+4);
                        String id = s.substring(index1+4,index2);

                        tableData1.set(3*i+j+y*48, EnchantmentUtil.stringToInt(id));
                        tableData2.set(3*i+j+y*48,level);
                    }
                } else if(items.get(j) == ItemStack.EMPTY){
                    tableData1.set(3*i+j+y*48,-1);
                    tableData2.set(3*i+j+y*48,-1);
                }
            }
        } else {
            tableData1.set(3*i+0+y*48,-1);
            tableData2.set(3*i+0+y*48,-1);
            tableData1.set(3*i+1+y*48,-1);
            tableData2.set(3*i+1+y*48,-1);
            tableData1.set(3*i+2+y*48,-1);
            tableData2.set(3*i+2+y*48,-1);
        }
    }

    public void tick() {
        for(int i = 0; i < 16; i++){
            getEnchantments(0,i);
            getEnchantments(1,i);
        }

        this.pageTurningSpeed = this.nextPageTurningSpeed;
        this.pageAngle = this.nextPageAngle;
        PlayerEntity playerentity = this.world.getClosestPlayer((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D, 3.0D, false);
        if (playerentity != null) {
            double d0 = playerentity.getPosX() - ((double)this.pos.getX() + 0.5D);
            double d1 = playerentity.getPosZ() - ((double)this.pos.getZ() + 0.5D);
            this.field_195531_n = (float)MathHelper.atan2(d1, d0);
            this.nextPageTurningSpeed += 0.1F;
            if (this.nextPageTurningSpeed < 0.5F || random.nextInt(40) == 0) {
                float f1 = this.field_195525_h;

                do {
                    this.field_195525_h += (float)(random.nextInt(4) - random.nextInt(4));
                } while(f1 == this.field_195525_h);
            }
        } else {
            this.field_195531_n += 0.02F;
            this.nextPageTurningSpeed -= 0.1F;
        }

        while(this.nextPageAngle >= (float)Math.PI) {
            this.nextPageAngle -= ((float)Math.PI * 2F);
        }

        while(this.nextPageAngle < -(float)Math.PI) {
            this.nextPageAngle += ((float)Math.PI * 2F);
        }

        while(this.field_195531_n >= (float)Math.PI) {
            this.field_195531_n -= ((float)Math.PI * 2F);
        }

        while(this.field_195531_n < -(float)Math.PI) {
            this.field_195531_n += ((float)Math.PI * 2F);
        }

        float f2;
        for(f2 = this.field_195531_n - this.nextPageAngle; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)) {
        }

        while(f2 < -(float)Math.PI) {
            f2 += ((float)Math.PI * 2F);
        }

        this.nextPageAngle += f2 * 0.4F;
        this.nextPageTurningSpeed = MathHelper.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        ++this.ticks;
        this.field_195524_g = this.field_195523_f;
        float f = (this.field_195525_h - this.field_195523_f) * 0.4F;
        float f3 = 0.2F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        this.field_195526_i += (f - this.field_195526_i) * 0.9F;
        this.field_195523_f += this.field_195526_i;
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

        if (index == 0 && !flag) {
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
        return new TranslationTextComponent("container.enchant");
    }

    protected Container createMenu(int id, PlayerInventory player) {
        return new NewEnchantmentContainer(id, player, this, this.tableData1, this.tableData2);
    }

}