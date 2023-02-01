package com.bedmen.odyssey.inventory;

import java.util.List;
import java.util.Optional;

import com.bedmen.odyssey.aspect.AspectInstance;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.aspect_objects.Aspect;
import com.bedmen.odyssey.inventory.slot.BetterResultContainer;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ContainerRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class ArcaneGrindstoneMenu extends AbstractContainerMenu {
    public static  final int CONTAINER_DATA_SIZE = 1;
    public static final int INPUT_SLOT = 0;
    public static final int TABLET_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_END = 39;
    private final BetterResultContainer resultContainer = new BetterResultContainer();
    final Container inputContainer = new SimpleContainer(2) {
        public void setChanged() {
            super.setChanged();
            ArcaneGrindstoneMenu.this.slotsChanged(this);
        }

        public void setItem(int index, ItemStack itemStack) {
            super.setItem(index, itemStack);
            if(index == INPUT_SLOT){
                ArcaneGrindstoneMenu.this.setCurrentPage(0);
            }
        }
    };
    private final ContainerLevelAccess access;
    // stores what modifier we are interested in removing
    private final ContainerData containerData;

    public ArcaneGrindstoneMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainerData(CONTAINER_DATA_SIZE), ContainerLevelAccess.NULL);
    }

    public ArcaneGrindstoneMenu(int id, Inventory inventory, ContainerData containerData, final ContainerLevelAccess containerLevelAccess) {
        super(ContainerRegistry.ARCANE_GRINDSTONE.get(), id);
        checkContainerDataCount(containerData, CONTAINER_DATA_SIZE);
        this.containerData = containerData;
        this.addDataSlots(containerData);
        this.access = containerLevelAccess;
        this.addSlot(new Slot(this.inputContainer, INPUT_SLOT, 35, 19) {
            public boolean mayPlace(ItemStack itemStack) {
                return AspectUtil.hasAddedModifiers(itemStack);
            }
        });
        this.addSlot(new Slot(this.inputContainer, TABLET_SLOT, 35, 40) {
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }
        });
        this.addSlot(new Slot(this.resultContainer, RESULT_SLOT, 116, 29) {
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }

            public void onTake(Player player, ItemStack itemStack) {
                containerLevelAccess.execute((level, blockPos) -> {
//                    if (level instanceof ServerLevel) {
//                        ExperienceOrb.award((ServerLevel)level, Vec3.atCenterOf(blockPos), this.getExperienceAmount(level));
//                    }

                    level.levelEvent(1042, blockPos, 0);
                });
                ArcaneGrindstoneMenu.this.inputContainer.setItem(INPUT_SLOT, ItemStack.EMPTY);
                ArcaneGrindstoneMenu.this.inputContainer.setItem(TABLET_SLOT, ItemStack.EMPTY);
            }

//            private int getExperienceAmount(Level level) {
//                int l = 0;
//                l += this.getExperienceFromItem(ArcaneGrindstoneMenu.this.repairSlots.getItem(INPUT_SLOT));
//                l += this.getExperienceFromItem(ArcaneGrindstoneMenu.this.repairSlots.getItem(ADDITIONAL_SLOT));
//                if (l > 0) {
//                    int i1 = (int)Math.ceil((double)l / 2.0D);
//                    return i1 + level.random.nextInt(i1);
//                } else {
//                    return 0;
//                }
//            }

//            private int getExperienceFromItem(ItemStack itemStack) {
//                int l = 0;
//                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
//
//                for(Entry<Enchantment, Integer> entry : map.entrySet()) {
//                    Enchantment enchantment = entry.getKey();
//                    Integer integer = entry.getValue();
//                    if (!enchantment.isCurse()) {
//                        l += enchantment.getMinCost(integer);
//                    }
//                }
//
//                return l;
//            }
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
        if (container == this.inputContainer) {
            this.createResult();
        }
    }

    private void createResult() {
        ItemStack itemStack = this.inputContainer.getItem(INPUT_SLOT);
        ItemStack tablet = this.inputContainer.getItem(TABLET_SLOT);
        ItemStack resultStack = itemStack.copy();
        Optional<Aspect> optionalAspect = this.getSelectedAddedModifierAspect();
        if(optionalAspect.isPresent()){
            AspectUtil.removeAddedModifier(resultStack, optionalAspect.get());
            this.resultContainer.setItem(resultStack);
        } else {
            this.resultContainer.setItem(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> {
            this.clearContainer(player, this.inputContainer);
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
            ItemStack itemstack2 = this.inputContainer.getItem(INPUT_SLOT);
            ItemStack itemstack3 = this.inputContainer.getItem(TABLET_SLOT);
            if (slotID == RESULT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (slotID != INPUT_SLOT && slotID != TABLET_SLOT) {
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

    public int getNumberOfAddedModifiers(){
        ItemStack itemStack = this.inputContainer.getItem(INPUT_SLOT);
        return itemStack.isEmpty() ? 0 : AspectUtil.getAddedModifiersAsAspectInstanceList(itemStack).size();
    }

    public int getCurrentPage(){
        return this.containerData.get(0);
    }

    public void setCurrentPage(int i){
        this.containerData.set(0, i);
        this.createResult();
    }

    public void movePageBackward(){
        int currentPage = this.getCurrentPage();
        if(!this.onFirstPage()){
            this.setCurrentPage(currentPage-1);
        }
    }

    public void movePageForward(){
        int currentPage = this.getCurrentPage();
        if(!this.onLastPage()){
            this.setCurrentPage(currentPage+1);
        }
    }

    public boolean onFirstPage(){
        return this.getCurrentPage() <= 0;
    }

    public boolean onLastPage(){
        return this.getCurrentPage() >= this.getNumberOfAddedModifiers() - 1;
    }

    public Optional<Aspect> getSelectedAddedModifierAspect(){
        ItemStack itemStack = this.inputContainer.getItem(INPUT_SLOT);
        List<AspectInstance> aspectInstanceList = AspectUtil.getAddedModifiersAsAspectInstanceList(itemStack);
        if(aspectInstanceList.isEmpty() || this.getCurrentPage() < 0 || this.getCurrentPage() >= aspectInstanceList.size()){
            return Optional.empty();
        }
        return Optional.of(aspectInstanceList.get(this.getCurrentPage()).aspect);
    }

    public boolean showBigRedX(){
        return !this.inputContainer.getItem(INPUT_SLOT).isEmpty() && this.resultContainer.getItem().isEmpty();
    }
}
