package com.bedmen.odyssey.inventory;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ContainerRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class ArcaneGrindstoneMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_END = 39;
    private final Container resultSlots = new ResultContainer();
    final Container repairSlots = new SimpleContainer(2) {
        public void setChanged() {
            super.setChanged();
            ArcaneGrindstoneMenu.this.slotsChanged(this);
        }
    };
    private final ContainerLevelAccess access;

    public ArcaneGrindstoneMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public ArcaneGrindstoneMenu(int id, Inventory inventory, final ContainerLevelAccess containerLevelAccess) {
        super(ContainerRegistry.ARCANE_GRINDSTONE.get(), id);
        this.access = containerLevelAccess;
        this.addSlot(new Slot(this.repairSlots, INPUT_SLOT, 49, 19) {
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.isDamageableItem() || itemStack.is(Items.ENCHANTED_BOOK) || itemStack.isEnchanted();
            }
        });
        this.addSlot(new Slot(this.repairSlots, ADDITIONAL_SLOT, 49, 40) {
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.isDamageableItem() || itemStack.is(Items.ENCHANTED_BOOK) || itemStack.isEnchanted();
            }
        });
        this.addSlot(new Slot(this.resultSlots, RESULT_SLOT, 129, 34) {
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }

            public void onTake(Player player, ItemStack itemStack) {
                containerLevelAccess.execute((level, blockPos) -> {
                    if (level instanceof ServerLevel) {
                        ExperienceOrb.award((ServerLevel)level, Vec3.atCenterOf(blockPos), this.getExperienceAmount(level));
                    }

                    level.levelEvent(1042, blockPos, 0);
                });
                ArcaneGrindstoneMenu.this.repairSlots.setItem(INPUT_SLOT, ItemStack.EMPTY);
                ArcaneGrindstoneMenu.this.repairSlots.setItem(ADDITIONAL_SLOT, ItemStack.EMPTY);
            }

            private int getExperienceAmount(Level level) {
                int l = 0;
                l += this.getExperienceFromItem(ArcaneGrindstoneMenu.this.repairSlots.getItem(INPUT_SLOT));
                l += this.getExperienceFromItem(ArcaneGrindstoneMenu.this.repairSlots.getItem(ADDITIONAL_SLOT));
                if (l > 0) {
                    int i1 = (int)Math.ceil((double)l / 2.0D);
                    return i1 + level.random.nextInt(i1);
                } else {
                    return 0;
                }
            }

            private int getExperienceFromItem(ItemStack itemStack) {
                int l = 0;
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);

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
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

    }

    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (container == this.repairSlots) {
            this.createResult();
        }

    }

    private void createResult() {
        ItemStack itemstack = this.repairSlots.getItem(INPUT_SLOT);
        ItemStack itemstack1 = this.repairSlots.getItem(ADDITIONAL_SLOT);
        boolean flag = !itemstack.isEmpty() || !itemstack1.isEmpty();
        boolean flag1 = !itemstack.isEmpty() && !itemstack1.isEmpty();
        if (!flag) {
            this.resultSlots.setItem(INPUT_SLOT, ItemStack.EMPTY);
        } else {
            boolean flag2 = !itemstack.isEmpty() && !itemstack.is(Items.ENCHANTED_BOOK) && !itemstack.isEnchanted() || !itemstack1.isEmpty() && !itemstack1.is(Items.ENCHANTED_BOOK) && !itemstack1.isEnchanted();
            if (itemstack.getCount() > 1 || itemstack1.getCount() > 1 || !flag1 && flag2) {
                this.resultSlots.setItem(INPUT_SLOT, ItemStack.EMPTY);
                this.broadcastChanges();
                return;
            }

            int j = 1;
            int i;
            ItemStack itemstack2;
            if (flag1) {
                if (!itemstack.is(itemstack1.getItem())) {
                    this.resultSlots.setItem(INPUT_SLOT, ItemStack.EMPTY);
                    this.broadcastChanges();
                    return;
                }

                Item item = itemstack.getItem();
                int k = itemstack.getMaxDamage() - itemstack.getDamageValue();
                int l = itemstack.getMaxDamage() - itemstack1.getDamageValue();
                int i1 = k + l + itemstack.getMaxDamage() * 5 / 100;
                i = Math.max(itemstack.getMaxDamage() - i1, 0);
                itemstack2 = this.mergeEnchants(itemstack, itemstack1);
                if (!itemstack2.isRepairable()) i = itemstack.getDamageValue();
                if (!itemstack2.isDamageableItem() || !itemstack2.isRepairable()) {
                    if (!ItemStack.matches(itemstack, itemstack1)) {
                        this.resultSlots.setItem(INPUT_SLOT, ItemStack.EMPTY);
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

            this.resultSlots.setItem(INPUT_SLOT, this.removeNonCurses(itemstack2, i, j));
        }

        this.broadcastChanges();
    }

    private ItemStack mergeEnchants(ItemStack itemStack1, ItemStack itemStack2) {
        ItemStack itemstack = itemStack1.copy();
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack2);

        for(Entry<Enchantment, Integer> entry : map.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (!enchantment.isCurse() || EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemstack) == 0) {
                itemstack.enchant(enchantment, entry.getValue());
            }
        }

        return itemstack;
    }

    private ItemStack removeNonCurses(ItemStack itemStack, int damage, int count) {
        ItemStack itemstack = itemStack.copy();
        itemstack.removeTagKey("Enchantments");
        itemstack.removeTagKey("StoredEnchantments");
        if (damage > 0) {
            itemstack.setDamageValue(damage);
        } else {
            itemstack.removeTagKey("Damage");
        }

        itemstack.setCount(count);
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack).entrySet().stream().filter((enchantmentIntegerEntry) -> {
            return enchantmentIntegerEntry.getKey().isCurse();
        }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        EnchantmentHelper.setEnchantments(map, itemstack);
        itemstack.setRepairCost(0);
        if (itemstack.is(Items.ENCHANTED_BOOK) && map.size() == 0) {
            itemstack = new ItemStack(Items.BOOK);
            if (itemStack.hasCustomHoverName()) {
                itemstack.setHoverName(itemStack.getHoverName());
            }
        }

        for(int i = 0; i < map.size(); ++i) {
            itemstack.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(itemstack.getBaseRepairCost()));
        }

        return itemstack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> {
            this.clearContainer(player, this.repairSlots);
        });
    }

    public boolean stillValid(Player player) {
        return stillValid(this.access, player, BlockRegistry.ARCANE_GRINDSTONE.get());
    }

    public ItemStack quickMoveStack(Player player, int slotID) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotID);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            ItemStack itemstack2 = this.repairSlots.getItem(INPUT_SLOT);
            ItemStack itemstack3 = this.repairSlots.getItem(ADDITIONAL_SLOT);
            if (slotID == RESULT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (slotID != INPUT_SLOT && slotID != ADDITIONAL_SLOT) {
                if (!itemstack2.isEmpty() && !itemstack3.isEmpty()) {
                    if (slotID >= INV_SLOT_START && slotID < INV_SLOT_END) {
                        if (!this.moveItemStackTo(itemstack1, INV_SLOT_END, USE_ROW_SLOT_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (slotID >= INV_SLOT_END && slotID < USE_ROW_SLOT_END && !this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, INPUT_SLOT, RESULT_SLOT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
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

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
