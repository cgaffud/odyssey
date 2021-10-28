package com.bedmen.odyssey.container;

import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.util.EnchantmentUtil;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class OdysseyGrindstoneContainer extends Container {
    public int purgeXP;
    private final IInventory resultSlots = new CraftResultInventory();
    private final IInventory repairSlots = new Inventory(2) {
        public void setChanged() {
            super.setChanged();
            OdysseyGrindstoneContainer.this.slotsChanged(this);
        }
    };
    private final IWorldPosCallable access;

    public OdysseyGrindstoneContainer(int p_i50080_1_, PlayerInventory p_i50080_2_) {
        this(p_i50080_1_, p_i50080_2_, IWorldPosCallable.NULL);
    }

    public OdysseyGrindstoneContainer(int p_i50081_1_, PlayerInventory p_i50081_2_, final IWorldPosCallable p_i50081_3_) {
        super(ContainerRegistry.GRINDSTONE.get(), p_i50081_1_);
        this.access = p_i50081_3_;
        this.addSlot(new Slot(this.repairSlots, 0, 49, 19) {
            public boolean mayPlace(ItemStack p_75214_1_) {
                return p_75214_1_.isDamageableItem() || p_75214_1_.getItem() == ItemRegistry.PURGE_TABLET.get() || p_75214_1_.isEnchanted();
            }
        });
        this.addSlot(new Slot(this.repairSlots, 1, 49, 40) {
            public boolean mayPlace(ItemStack p_75214_1_) {
                return p_75214_1_.isDamageableItem() || p_75214_1_.getItem() == ItemRegistry.PURGE_TABLET.get() || p_75214_1_.isEnchanted();
            }
        });
        this.addSlot(new OdysseyGrindstoneOutputSlot(this.resultSlots, 2, 129, 34, this) {
            public boolean mayPlace(ItemStack p_75214_1_) {
                return false;
            }

            public ItemStack onTake(PlayerEntity p_190901_1_, ItemStack p_190901_2_) {
                p_i50081_3_.execute((p_216944_1_, p_216944_2_) -> {
                    int l;
                    if(this.grindstone.purgeXP == 0){
                        l = this.getExperienceAmount(p_216944_1_);
                    } else {
                        l = this.grindstone.purgeXP;
                    }
                    this.grindstone.purgeXP = 0;

                    while(l > 0) {
                        int i1 = ExperienceOrbEntity.getExperienceValue(l);
                        l -= i1;
                        p_216944_1_.addFreshEntity(new ExperienceOrbEntity(p_216944_1_, (double)p_216944_2_.getX(), (double)p_216944_2_.getY() + 0.5D, (double)p_216944_2_.getZ() + 0.5D, i1));
                    }

                    p_216944_1_.levelEvent(1042, p_216944_2_, 0);
                });
                OdysseyGrindstoneContainer.this.repairSlots.setItem(0, ItemStack.EMPTY);
                OdysseyGrindstoneContainer.this.repairSlots.setItem(1, ItemStack.EMPTY);
                return p_190901_2_;
            }

            private int getExperienceAmount(World p_216942_1_) {
                int l = 0;
                l = l + this.getExperienceFromItem(OdysseyGrindstoneContainer.this.repairSlots.getItem(0));
                l = l + this.getExperienceFromItem(OdysseyGrindstoneContainer.this.repairSlots.getItem(1));
                if (l > 0) {
                    int i1 = (int)Math.ceil((double)l / 2.0D);
                    return i1 + p_216942_1_.random.nextInt(i1);
                } else {
                    return 0;
                }
            }

            private int getExperienceFromItem(ItemStack p_216943_1_) {
                int l = 0;
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(p_216943_1_);

                for(Entry<Enchantment, Integer> entry : map.entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    Integer integer = entry.getValue();
                    if (!enchantment.isCurse()) {
                        l += enchantment.getMinCost(integer);
                    }
                }

                return l;
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(p_i50081_2_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(p_i50081_2_, k, 8 + k * 18, 142));
        }

    }

    public void slotsChanged(IInventory p_75130_1_) {
        super.slotsChanged(p_75130_1_);
        if (p_75130_1_ == this.repairSlots) {
            this.createResult();
        }

    }

    private void createResult() {
        ItemStack itemstack = this.repairSlots.getItem(0);
        ItemStack itemstack1 = this.repairSlots.getItem(1);
        boolean flag = !itemstack.isEmpty() || !itemstack1.isEmpty(); // Not Empty
        boolean flag1 = !itemstack.isEmpty() && !itemstack1.isEmpty(); // Full
        if (!flag) { //If Empty
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            boolean flag2 = !itemstack.isEmpty() && !EnchantmentUtil.hasNonCurseNonInnateEnchant(itemstack) || !itemstack1.isEmpty() && !EnchantmentUtil.hasNonCurseNonInnateEnchant(itemstack1); // At least one exists and is not enchanted with a non-curse
            boolean flag4 = !itemstack.isEmpty() && itemstack.isEnchanted() || !itemstack1.isEmpty() && itemstack1.isEnchanted(); // At least one is enchanted with something
            if (itemstack.getCount() > 1 || itemstack1.getCount() > 1 || (!flag1 && flag2)) { //If items are multiple or a single item not enchanted with a non-curse
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.broadcastChanges();
                return;
            }

            int j = 1;
            int i;
            ItemStack itemstack2;
            if (flag1) {
                boolean flag5 = itemstack.getItem() == ItemRegistry.PURGE_TABLET.get() || itemstack1.getItem() == ItemRegistry.PURGE_TABLET.get();
                boolean flag6 = itemstack.getItem() == ItemRegistry.PURGE_TABLET.get() && itemstack1.getItem() == ItemRegistry.PURGE_TABLET.get();
                if(flag5 && flag4){ //If one is enchanted and the other is a purge tablet
                    ItemStack purgeTablet = itemstack.getItem() == ItemRegistry.PURGE_TABLET.get() ? itemstack : itemstack1;
                    ItemStack other = purgeTablet == itemstack ? itemstack1 : itemstack;
                    Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(purgeTablet);
                    Map<Enchantment, Integer> map1 = EnchantmentUtil.getEnchantmentsWithoutInnate(other);
                    int removalCount = 0;
                    for(Enchantment e : map.keySet()){
                        if(map1.containsKey(e) && map.get(e) >= (map1.get(e))){
                            removalCount += map1.get(e);
                            map1.remove(e);
                        }
                    }
                    if(removalCount == 0){
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.broadcastChanges();
                        return;
                    }
                    itemstack2 = other.copy();
                    EnchantmentHelper.setEnchantments(map1, itemstack2);
                    this.resultSlots.setItem(0, itemstack2);
                    this.broadcastChanges();
                    this.purgeXP = 25 * removalCount;
                    return;
                }
                if (itemstack.getItem() != itemstack1.getItem() || flag6) {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.broadcastChanges();
                    return;
                }

                int k = itemstack.getMaxDamage() - itemstack.getDamageValue();
                int l = itemstack.getMaxDamage() - itemstack1.getDamageValue();
                int i1 = k + l + itemstack.getMaxDamage() * 5 / 100;
                i = Math.max(itemstack.getMaxDamage() - i1, 0);
                itemstack2 = this.mergeEnchants(itemstack, itemstack1);
                if (!itemstack2.isRepairable()) i = itemstack.getDamageValue();
                if (!itemstack2.isDamageableItem() || !itemstack2.isRepairable()) {
                    if (!ItemStack.matches(itemstack, itemstack1)) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.broadcastChanges();
                        return;
                    }

                    j = 2;
                }
            } else {
                boolean flag3 = !itemstack.isEmpty();
                i = flag3 ? itemstack.getDamageValue() : itemstack1.getDamageValue();
                itemstack2 = flag3 ? itemstack : itemstack1;
            }

            this.resultSlots.setItem(0, this.removeNonCurses(itemstack2, i, j));
        }

        this.broadcastChanges();
    }

    private ItemStack mergeEnchants(ItemStack p_217011_1_, ItemStack p_217011_2_) {
        ItemStack itemstack = p_217011_1_.copy();
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(p_217011_2_);

        for(Entry<Enchantment, Integer> entry : map.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (!enchantment.isCurse() || EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemstack) == 0) {
                itemstack.enchant(enchantment, entry.getValue());
            }
        }

        return itemstack;
    }

    private ItemStack removeNonCurses(ItemStack p_217007_1_, int p_217007_2_, int p_217007_3_) {
        ItemStack itemstack = p_217007_1_.copy();
        itemstack.removeTagKey("Enchantments");
        itemstack.removeTagKey("StoredEnchantments");
        if (p_217007_2_ > 0) {
            itemstack.setDamageValue(p_217007_2_);
        } else {
            itemstack.removeTagKey("Damage");
        }

        itemstack.setCount(p_217007_3_);
        Map<Enchantment, Integer> map = EnchantmentUtil.getEnchantmentsWithoutInnate(p_217007_1_).entrySet().stream().filter((p_217012_0_) -> p_217012_0_.getKey().isCurse()).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        EnchantmentHelper.setEnchantments(map, itemstack);
        itemstack.setRepairCost(0);
        if (itemstack.getItem() == Items.ENCHANTED_BOOK && map.size() == 0) {
            itemstack = new ItemStack(Items.BOOK);
            if (p_217007_1_.hasCustomHoverName()) {
                itemstack.setHoverName(p_217007_1_.getHoverName());
            }
        }

        for(int i = 0; i < map.size(); ++i) {
            itemstack.setRepairCost(RepairContainer.calculateIncreasedRepairCost(itemstack.getBaseRepairCost()));
        }

        return itemstack;
    }

    public void removed(PlayerEntity p_75134_1_) {
        super.removed(p_75134_1_);
        this.access.execute((p_217009_2_, p_217009_3_) -> {
            this.clearContainer(p_75134_1_, p_217009_2_, this.repairSlots);
        });
    }

    public boolean stillValid(PlayerEntity p_75145_1_) {
        return stillValid(this.access, p_75145_1_, Blocks.GRINDSTONE);
    }

    public ItemStack quickMoveStack(PlayerEntity p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_82846_2_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            ItemStack itemstack2 = this.repairSlots.getItem(0);
            ItemStack itemstack3 = this.repairSlots.getItem(1);
            if (p_82846_2_ == 2) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (p_82846_2_ != 0 && p_82846_2_ != 1) {
                if (!itemstack2.isEmpty() && !itemstack3.isEmpty()) {
                    if (p_82846_2_ >= 3 && p_82846_2_ < 30) {
                        if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (p_82846_2_ >= 30 && p_82846_2_ < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
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

            slot.onTake(p_82846_1_, itemstack1);
        }

        return itemstack;
    }

    private class OdysseyGrindstoneOutputSlot extends Slot{
        public final OdysseyGrindstoneContainer grindstone;

        public OdysseyGrindstoneOutputSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, OdysseyGrindstoneContainer grindstone) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
            this.grindstone = grindstone;
        }
    }
}
