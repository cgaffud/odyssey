package com.bedmen.odyssey.tileentity;

import com.bedmen.odyssey.container.OdysseyEnchantmentContainer;
import com.bedmen.odyssey.enchantment.IUpgradableEnchantment;
import com.bedmen.odyssey.enchantment.IUpgradedEnchantment;
import com.bedmen.odyssey.util.EnchantmentUtil;
import com.bedmen.odyssey.registry.TileEntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Map;
import java.util.Random;

public class OdysseyEnchantingTableTileEntity extends LockableTileEntity implements IInventory, ITickableTileEntity {
    private final int[] X_POS = {2,2,2,1,0,-1,-2,-2,-2,-2,-2,-1,0,1,2,2};
    private final int[] Z_POS = {0,1,2,2,2,2,2,1,0,-1,-2,-2,-2,-2,-2,-1};
    protected int[] enchantmentIds = new int[96];
    protected int[] levelNums = new int[96];
    protected int enchantLevel = -1;
    protected Enchantment enchant = null;
    protected int enchantCost = -1;
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
        public int getCount() {
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
        public int getCount() {
            return 96;
        }
    };
    protected final IIntArray enchantData = new IIntArray(){

        @Override
        public int get(int index) {
            if(index == 0) return enchantLevel;
            if(index == 1) return EnchantmentUtil.enchantmentToInt(enchant);
            if(index == 2) return enchantCost;
            return -1;
        }

        @Override
        public void set(int index, int value) {
            if(index == 0) enchantLevel = value;
            else if(index == 1) enchant = EnchantmentUtil.intToEnchantment(value);
            else if(index == 2) enchantCost = value;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float nextPageTurningSpeed;
    public float pageTurningSpeed;
    public float nextPageAngle;
    public float pageAngle;
    public float tRot;
    private static final Random random = new Random();

    public OdysseyEnchantingTableTileEntity() {
        super(TileEntityTypeRegistry.ENCHANTING_TABLE.get());
    }

    public void load(BlockState state, CompoundNBT nbt) { //TODO: MARK
        super.load(state, nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);

    }

    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    private void getEnchantments(int y, int i){
        TileEntity tileEntity = level.getBlockEntity(this.worldPosition.offset(this.X_POS[i],y,this.Z_POS[i]));
        if(tileEntity instanceof BookshelfTileEntity){
            NonNullList<ItemStack> items = ((BookshelfTileEntity)tileEntity).getItems();
            for(int j = 0; j < 3; j++){
                if(items.get(j).getItem() == Items.ENCHANTED_BOOK){
                    Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(items.get(j));
                    for(Enchantment e : map.keySet()){
                        int level = map.get(e);
                        tableData1.set(3*i+j+y*48, EnchantmentUtil.enchantmentToInt(e));
                        tableData2.set(3*i+j+y*48,level);
                        break;
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

        if(enchantLevel > -1  && enchant != null && enchantCost > -1) doEnchant();

        this.pageTurningSpeed = this.nextPageTurningSpeed;
        this.pageAngle = this.nextPageAngle;
        PlayerEntity playerentity = this.level.getNearestPlayer((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D, 3.0D, false);
        if (playerentity != null) {
            double d0 = playerentity.getX() - ((double)this.worldPosition.getX() + 0.5D);
            double d1 = playerentity.getZ() - ((double)this.worldPosition.getZ() + 0.5D);
            this.tRot = (float)MathHelper.atan2(d1, d0);
            this.nextPageTurningSpeed += 0.1F;
            if (this.nextPageTurningSpeed < 0.5F || random.nextInt(40) == 0) {
                float f1 = this.flipT;

                do {
                    this.flipT += (float)(random.nextInt(4) - random.nextInt(4));
                } while(f1 == this.flipT);
            }
        } else {
            this.tRot += 0.02F;
            this.nextPageTurningSpeed -= 0.1F;
        }

        while(this.nextPageAngle >= (float)Math.PI) {
            this.nextPageAngle -= ((float)Math.PI * 2F);
        }

        while(this.nextPageAngle < -(float)Math.PI) {
            this.nextPageAngle += ((float)Math.PI * 2F);
        }

        while(this.tRot >= (float)Math.PI) {
            this.tRot -= ((float)Math.PI * 2F);
        }

        while(this.tRot < -(float)Math.PI) {
            this.tRot += ((float)Math.PI * 2F);
        }

        float f2;
        for(f2 = this.tRot - this.nextPageAngle; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)) {
        }

        while(f2 < -(float)Math.PI) {
            f2 += ((float)Math.PI * 2F);
        }

        this.nextPageAngle += f2 * 0.4F;
        this.nextPageTurningSpeed = MathHelper.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
        ++this.ticks;
        this.oFlip = this.flip;
        float f = (this.flipT - this.flip) * 0.4F;
        float f3 = 0.2F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        this.flipA += (f - this.flipA) * 0.9F;
        this.flip += this.flipA;
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

        if (!flag) {
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

    /**
     * invalidates a tile entity
     */
    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.enchant");
    }

    protected Container createMenu(int id, PlayerInventory player) {
        return new OdysseyEnchantmentContainer(id, player, this, this.tableData1, this.tableData2, this.enchantData);
    }

    public void doEnchant() {
        this.items.get(1).shrink(this.enchantCost);
        ItemStack itemStack = this.items.get(0);
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
        if(this.enchant instanceof IUpgradableEnchantment){
            map.remove(((IUpgradableEnchantment)this.enchant).getUpgrade());
        }
        if(this.enchant instanceof IUpgradedEnchantment){
            map.remove(((IUpgradedEnchantment)this.enchant).getDowngrade());
        }
        map.put(enchant, enchantLevel);
        EnchantmentHelper.setEnchantments(map, itemStack);
        this.enchantData.set(0,-1);
        this.enchantData.set(1,-1);
        this.enchantData.set(2,-1);
        this.setChanged();
    }
}