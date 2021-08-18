package com.bedmen.odyssey.container;

import com.bedmen.odyssey.client.gui.OdysseyEnchantmentScreen;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.registry.EnchantmentRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;

import java.util.*;

public class OdysseyEnchantmentContainer extends Container {
    public final IInventory inv;
    protected final World world;
    private final IIntArray tableData1;
    private final IIntArray tableData2;
    public final IIntArray enchantData;
    private List<Enchantment> enchantmentList = new ArrayList<Enchantment>();
    private List<Integer> levelList = new ArrayList<Integer>();
    private List<Integer> costList = new ArrayList<Integer>();

    public OdysseyEnchantmentContainer(int p_i241921_4_, PlayerInventory p_i241921_5_) {
        this(p_i241921_4_, p_i241921_5_, new Inventory(2), new IntArray(96), new IntArray(96), new IntArray(3));
    }

    public OdysseyEnchantmentContainer(int id, PlayerInventory playerInventory, IInventory inv, IIntArray tableData1, IIntArray tableData2, IIntArray enchantData) {
        super(ContainerRegistry.ENCHANTMENT.get(), id);
        checkContainerSize(inv, 2);
        this.inv = inv;
        this.world = playerInventory.player.level;
        checkContainerDataCount(tableData1, 96);
        checkContainerDataCount(tableData2, 96);
        checkContainerDataCount(enchantData, 3);
        this.tableData1 = tableData1;
        this.tableData2 = tableData2;
        this.enchantData = enchantData;
        this.addSlot(new Slot(this.inv, 0, 29, 23){
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.inv, 1, 41, 47) {
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem().equals(Items.LAPIS_LAZULI);
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlots(tableData1);
        this.addDataSlots(tableData2);
        this.addDataSlots(enchantData);
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(PlayerEntity playerIn) {
        return this.inv.stillValid(playerIn);
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
            if (index == 0 || index  == 1) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > 1) {
                if(itemstack1.getItem().equals(Items.LAPIS_LAZULI)){
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 2 && index < 38) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
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

    private void generateLists(){
        this.enchantmentList = new ArrayList<Enchantment>();
        this.levelList = new ArrayList<Integer>();
        this.costList = new ArrayList<Integer>();
        List<Integer> list = new ArrayList<Integer>();
        ItemStack itemStack = this.inv.getItem(0);
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);

        // Let's try this

        for(int i = 0; i < this.tableData1.getCount(); i++){
            int i1 = this.tableData1.get(i);
            if(!EnchantmentUtil.canBeApplied(itemStack,i1)) continue;
            int i2 = this.tableData2.get(i);
            int i3 = 32768*i1+i2;
            if(list.contains(i3)) continue;
            Enchantment e = EnchantmentUtil.intToEnchantment(i1);
            boolean b = true;
            if(map.containsKey(e) && map.get(e) >= i2) b = false;
            else{
                for(Enchantment e1 : EnchantmentUtil.exclusiveWith(e)){
                    if(map.containsKey(e1)){
                        b = false;
                        break;
                    }
                }
            }
            if (map.containsKey(EnchantmentRegistry.UNENCHANTABLE.get())) b = false;
            if(b)
                list.add(i3);
        }

        list.sort(Integer::compare);
        for(Integer i : list){
            Enchantment e = EnchantmentUtil.intToEnchantment(i/32768);
            this.enchantmentList.add(e);
            int j = i%32768;
            this.levelList.add(j);
            if(map.containsKey(e)){
                int k = map.get(e);
                this.costList.add((j*j+j-k*k-k)/2);
            }
            else{
                this.costList.add((j*j+j)/2);
            }

        }
    }

    public int numPages(){
        this.generateLists();
        return (this.enchantmentList.size() + OdysseyEnchantmentScreen.ENCHANT_PER_PAGE - 1)/(OdysseyEnchantmentScreen.ENCHANT_PER_PAGE);
    }

    public List<Enchantment> getEnchantmentList(){
        return this.enchantmentList;
    }

    public List<Integer> getLevelList(){
        return this.levelList;
    }

    public List<Integer> getCostList(){
        return this.costList;
    }

    public void doEnchant(int level, int id, int cost){
        this.enchantData.set(0, level);
        this.enchantData.set(1, id);
        this.enchantData.set(2, cost);
    }
}