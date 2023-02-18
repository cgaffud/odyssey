package com.bedmen.odyssey.inventory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.object.Aspect;
import com.bedmen.odyssey.inventory.slot.BetterResultContainer;
import com.bedmen.odyssey.items.PurificationTabletItem;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class OdysseyGrindstoneMenu extends AbstractContainerMenu {
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
            OdysseyGrindstoneMenu.this.slotsChanged(this);
        }

        public void setItem(int index, ItemStack itemStack) {
            super.setItem(index, itemStack);
            OdysseyGrindstoneMenu.this.setCurrentPage(0);
        }
    };
    private final ContainerLevelAccess access;
    // stores what modifier we are interested in removing
    private final ContainerData containerData;

    public OdysseyGrindstoneMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainerData(CONTAINER_DATA_SIZE), ContainerLevelAccess.NULL);
    }

    public OdysseyGrindstoneMenu(int id, Inventory inventory, ContainerData containerData, final ContainerLevelAccess containerLevelAccess) {
        super(ContainerRegistry.ODYSSEY_GRINDSTONE.get(), id);
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
                return itemStack.is(ItemRegistry.PURIFICATION_TABLET.get());
            }
        });
        this.addSlot(new Slot(this.resultContainer, RESULT_SLOT, 116, 29) {
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }

            public void onTake(Player player, ItemStack itemStack) {
                containerLevelAccess.execute((level, blockPos) -> {
                    if (level instanceof ServerLevel) {
                        ExperienceOrb.award((ServerLevel)level, Vec3.atCenterOf(blockPos), this.getExperienceAmount());
                    }

                    level.levelEvent(1042, blockPos, 0);
                });
                OdysseyGrindstoneMenu.this.inputContainer.setItem(INPUT_SLOT, ItemStack.EMPTY);
                OdysseyGrindstoneMenu.this.inputContainer.setItem(TABLET_SLOT, ItemStack.EMPTY);
            }

            private int getExperienceAmount() {
                Optional<AspectInstance> optionalAspectInstance = OdysseyGrindstoneMenu.this.getSelectedAddedModifierAspect();
                int exp = 0;
                if(optionalAspectInstance.isPresent()){
                    ItemStack inputStack = OdysseyGrindstoneMenu.this.getInput();
                    float modifiability = optionalAspectInstance.get().getModifiability(inputStack);
                    exp = (int)(modifiability * 6.0f);
                }
                return exp;
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
        if (container == this.inputContainer) {
            this.createResult();
        }
    }

    private void createResult() {
        ItemStack resultStack = this.getInput().copy();
        Optional<AspectInstance> optionalAspectInstance = this.getSelectedAddedModifierAspect();
        if(optionalAspectInstance.isPresent()){
            AspectUtil.removeAddedModifier(resultStack, optionalAspectInstance.get().aspect);
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
        return stillValid(this.access, player, BlockRegistry.ODYSSEY_GRINDSTONE.get());
    }

    public ItemStack quickMoveStack(Player player, int slotID) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotID);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            ItemStack itemstack2 = this.getInput();
            ItemStack itemstack3 = this.getTablet();
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

    private ItemStack getInput(){
        return this.inputContainer.getItem(INPUT_SLOT);
    }

    private ItemStack getTablet(){
        return this.inputContainer.getItem(TABLET_SLOT);
    }

    private static List<AspectInstance> getNonObfuscatedModifiers(ItemStack itemStack){
        return AspectUtil.getAddedModifiersAsAspectInstanceList(itemStack).stream().filter(aspectInstance -> !aspectInstance.obfuscated).collect(Collectors.toList());
    }

    private List<AspectInstance> getRelevantModifiers(){
        ItemStack itemStack = this.getInput();
        if(itemStack.isEmpty()){
            return List.of();
        }
        ItemStack tablet = this.getTablet();
        if(tablet.isEmpty()){
            return getNonObfuscatedModifiers(itemStack);
        } else {
            List<AspectInstance> aspectInstanceList = AspectUtil.getAddedModifiersAsAspectInstanceList(itemStack);
            return aspectInstanceList.stream().filter(aspectInstance -> aspectInstance.aspect == PurificationTabletItem.getAspect(tablet) && aspectInstance.obfuscated).collect(Collectors.toList());
        }
    }

    private int getCurrentPage(){
        return this.containerData.get(0);
    }

    private void setCurrentPage(int i){
        i = Mth.clamp(i, 0, this.getRelevantModifiers().size());
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
        return this.getCurrentPage() >= this.getRelevantModifiers().size() - 1;
    }

    public Optional<AspectInstance> getSelectedAddedModifierAspect(){
        List<AspectInstance> aspectInstanceList = this.getRelevantModifiers();
        if(aspectInstanceList.isEmpty() || this.getCurrentPage() < 0 || this.getCurrentPage() >= aspectInstanceList.size()){
            return Optional.empty();
        }
        return Optional.of(aspectInstanceList.get(this.getCurrentPage()));
    }

    public boolean showBigRedX(){
        return !this.getInput().isEmpty() && this.resultContainer.getItem().isEmpty();
    }
}
