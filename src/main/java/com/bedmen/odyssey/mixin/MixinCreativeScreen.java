package com.bedmen.odyssey.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(CreativeScreen.class)
public abstract class MixinCreativeScreen extends DisplayEffectsScreen<CreativeScreen.CreativeContainer> {

    @Shadow
    private static int selectedTab;
    @Shadow
    private List<Slot> originalSlots;
    @Shadow
    private TextFieldWidget searchBox;
    @Shadow
    private Slot destroyItemSlot;
    @Shadow
    private void refreshSearchResults() {}
    @Shadow
    private static Inventory CONTAINER;
    @Shadow
    private float scrollOffs;
    @Shadow
    private boolean hasClickedOutside;
    @Shadow
    private boolean isCreativeSlot(@Nullable Slot p_208018_1_) {return false;}

    public MixinCreativeScreen(CreativeScreen.CreativeContainer p_i51091_1_, PlayerInventory p_i51091_2_, ITextComponent p_i51091_3_) {
        super(p_i51091_1_, p_i51091_2_, p_i51091_3_);
    }

    private void selectTab(ItemGroup p_147050_1_) {
        if (p_147050_1_ == null) return;
        int i = selectedTab;
        selectedTab = p_147050_1_.getId();
        slotColor = p_147050_1_.getSlotColor();
        this.quickCraftSlots.clear();
        (this.menu).items.clear();
        if (p_147050_1_ == ItemGroup.TAB_HOTBAR) {
            CreativeSettings creativesettings = this.minecraft.getHotbarManager();

            for(int j = 0; j < 9; ++j) {
                HotbarSnapshot hotbarsnapshot = creativesettings.get(j);
                if (hotbarsnapshot.isEmpty()) {
                    for(int k = 0; k < 9; ++k) {
                        if (k == j) {
                            ItemStack itemstack = new ItemStack(Items.PAPER);
                            itemstack.getOrCreateTagElement("CustomCreativeLock");
                            ITextComponent itextcomponent = this.minecraft.options.keyHotbarSlots[j].getTranslatedKeyMessage();
                            ITextComponent itextcomponent1 = this.minecraft.options.keySaveHotbarActivator.getTranslatedKeyMessage();
                            itemstack.setHoverName(new TranslationTextComponent("inventory.hotbarInfo", itextcomponent1, itextcomponent));
                            (this.menu).items.add(itemstack);
                        } else {
                            (this.menu).items.add(ItemStack.EMPTY);
                        }
                    }
                } else {
                    (this.menu).items.addAll(hotbarsnapshot);
                }
            }
        } else if (p_147050_1_ != ItemGroup.TAB_SEARCH) {
            p_147050_1_.fillItemList((this.menu).items);
        }

        if (p_147050_1_ == ItemGroup.TAB_INVENTORY) {
            Container container = this.minecraft.player.inventoryMenu;
            if (this.originalSlots == null) {
                this.originalSlots = ImmutableList.copyOf((this.menu).slots);
            }

            (this.menu).slots.clear();

            for(int l = 0; l < container.slots.size(); ++l) {
                int i1;
                int j1;
                if (l >= 5 && l < 9) {
                    int l1 = l - 5;
                    int j2 = l1 / 2;
                    int l2 = l1 % 2;
                    i1 = 54 + j2 * 54;
                    j1 = 6 + l2 * 27;
                } else if (l >= 0 && l < 5) {
                    i1 = -2000;
                    j1 = -2000;
                } else if (l == 45) {
                    i1 = 35;
                    j1 = 20;
                } else if (l == 46) {
                    i1 = 127;
                    j1 = 20;
                } else {
                    int k1 = l - 9;
                    int i2 = k1 % 9;
                    int k2 = k1 / 9;
                    i1 = 9 + i2 * 18;
                    if (l >= 36) {
                        j1 = 112;
                    } else {
                        j1 = 54 + k2 * 18;
                    }
                }

                Slot slot = new CreativeScreen.CreativeSlot(container.slots.get(l), l, i1, j1);
                (this.menu).slots.add(slot);
            }

            this.destroyItemSlot = new Slot(CONTAINER, 0, 173, 112);
            (this.menu).slots.add(this.destroyItemSlot);
        } else if (i == ItemGroup.TAB_INVENTORY.getId()) {
            (this.menu).slots.clear();
            (this.menu).slots.addAll(this.originalSlots);
            this.originalSlots = null;
        }

        if (this.searchBox != null) {
            if (p_147050_1_.hasSearchBar()) {
                this.searchBox.setVisible(true);
                this.searchBox.setCanLoseFocus(false);
                this.searchBox.setFocus(true);
                if (i != p_147050_1_.getId()) {
                    this.searchBox.setValue("");
                }
                this.searchBox.setWidth(p_147050_1_.getSearchbarWidth());
                this.searchBox.x = this.leftPos + (82 /*default left*/ + 89 /*default width*/) - this.searchBox.getWidth();

                this.refreshSearchResults();
            } else {
                this.searchBox.setVisible(false);
                this.searchBox.setCanLoseFocus(true);
                this.searchBox.setFocus(false);
                this.searchBox.setValue("");
            }
        }

        this.scrollOffs = 0.0F;
        this.menu.scrollTo(0.0F);
    }

    protected void slotClicked(@Nullable Slot p_184098_1_, int p_184098_2_, int p_184098_3_, ClickType p_184098_4_) {
        if (this.isCreativeSlot(p_184098_1_)) {
            this.searchBox.moveCursorToEnd();
            this.searchBox.setHighlightPos(0);
        }

        boolean flag = p_184098_4_ == ClickType.QUICK_MOVE;
        p_184098_4_ = p_184098_2_ == -999 && p_184098_4_ == ClickType.PICKUP ? ClickType.THROW : p_184098_4_;
        if (p_184098_1_ == null && selectedTab != ItemGroup.TAB_INVENTORY.getId() && p_184098_4_ != ClickType.QUICK_CRAFT) {
            PlayerInventory playerinventory1 = this.minecraft.player.inventory;
            if (!playerinventory1.getCarried().isEmpty() && this.hasClickedOutside) {
                if (p_184098_3_ == 0) {
                    this.minecraft.player.drop(playerinventory1.getCarried(), true);
                    this.minecraft.gameMode.handleCreativeModeItemDrop(playerinventory1.getCarried());
                    playerinventory1.setCarried(ItemStack.EMPTY);
                }

                if (p_184098_3_ == 1) {
                    ItemStack itemstack6 = playerinventory1.getCarried().split(1);
                    this.minecraft.player.drop(itemstack6, true);
                    this.minecraft.gameMode.handleCreativeModeItemDrop(itemstack6);
                }
            }
        } else {
            if (p_184098_1_ != null && !p_184098_1_.mayPickup(this.minecraft.player)) {
                return;
            }

            if (p_184098_1_ == this.destroyItemSlot && flag) {
                for(int j = 0; j < this.minecraft.player.inventoryMenu.getItems().size(); ++j) {
                    this.minecraft.gameMode.handleCreativeModeItemAdd(ItemStack.EMPTY, j);
                }
            } else if (selectedTab == ItemGroup.TAB_INVENTORY.getId()) {
                if (p_184098_1_ == this.destroyItemSlot) {
                    this.minecraft.player.inventory.setCarried(ItemStack.EMPTY);
                } else if (p_184098_4_ == ClickType.THROW && p_184098_1_ != null && p_184098_1_.hasItem()) {
                    ItemStack itemstack = p_184098_1_.remove(p_184098_3_ == 0 ? 1 : p_184098_1_.getItem().getMaxStackSize());
                    ItemStack itemstack1 = p_184098_1_.getItem();
                    this.minecraft.player.drop(itemstack, true);
                    this.minecraft.gameMode.handleCreativeModeItemDrop(itemstack);
                    this.minecraft.gameMode.handleCreativeModeItemAdd(itemstack1, ((CreativeScreen.CreativeSlot)p_184098_1_).target.index);
                } else if (p_184098_4_ == ClickType.THROW && !this.minecraft.player.inventory.getCarried().isEmpty()) {
                    this.minecraft.player.drop(this.minecraft.player.inventory.getCarried(), true);
                    this.minecraft.gameMode.handleCreativeModeItemDrop(this.minecraft.player.inventory.getCarried());
                    this.minecraft.player.inventory.setCarried(ItemStack.EMPTY);
                } else {
                    this.minecraft.player.inventoryMenu.clicked(p_184098_1_ == null ? p_184098_2_ : ((CreativeScreen.CreativeSlot)p_184098_1_).target.index, p_184098_3_, p_184098_4_, this.minecraft.player);
                    this.minecraft.player.inventoryMenu.broadcastChanges();
                }
            } else if (p_184098_4_ != ClickType.QUICK_CRAFT && p_184098_1_.container == CONTAINER) {
                PlayerInventory playerinventory = this.minecraft.player.inventory;
                ItemStack itemstack5 = playerinventory.getCarried();
                ItemStack itemstack7 = p_184098_1_.getItem();
                if (p_184098_4_ == ClickType.SWAP) {
                    if (!itemstack7.isEmpty()) {
                        ItemStack itemstack10 = itemstack7.copy();
                        itemstack10.setCount(itemstack10.getMaxStackSize());
                        this.minecraft.player.inventory.setItem(p_184098_3_, itemstack10);
                        this.minecraft.player.inventoryMenu.broadcastChanges();
                    }

                    return;
                }

                if (p_184098_4_ == ClickType.CLONE) {
                    if (playerinventory.getCarried().isEmpty() && p_184098_1_.hasItem()) {
                        ItemStack itemstack9 = p_184098_1_.getItem().copy();
                        itemstack9.setCount(itemstack9.getMaxStackSize());
                        playerinventory.setCarried(itemstack9);
                    }

                    return;
                }

                if (p_184098_4_ == ClickType.THROW) {
                    if (!itemstack7.isEmpty()) {
                        ItemStack itemstack8 = itemstack7.copy();
                        itemstack8.setCount(p_184098_3_ == 0 ? 1 : itemstack8.getMaxStackSize());
                        this.minecraft.player.drop(itemstack8, true);
                        this.minecraft.gameMode.handleCreativeModeItemDrop(itemstack8);
                    }

                    return;
                }

                if (!itemstack5.isEmpty() && !itemstack7.isEmpty() && itemstack5.sameItem(itemstack7) && ItemStack.tagMatches(itemstack5, itemstack7)) {
                    if (p_184098_3_ == 0) {
                        if (flag) {
                            itemstack5.setCount(itemstack5.getMaxStackSize());
                        } else if (itemstack5.getCount() < itemstack5.getMaxStackSize()) {
                            itemstack5.grow(1);
                        }
                    } else {
                        itemstack5.shrink(1);
                    }
                } else if (!itemstack7.isEmpty() && itemstack5.isEmpty()) {
                    playerinventory.setCarried(itemstack7.copy());
                    itemstack5 = playerinventory.getCarried();
                    if (flag) {
                        itemstack5.setCount(itemstack5.getMaxStackSize());
                    }
                } else if (p_184098_3_ == 0) {
                    playerinventory.setCarried(ItemStack.EMPTY);
                } else {
                    playerinventory.getCarried().shrink(1);
                }
            } else if (this.menu != null) {
                ItemStack itemstack3 = p_184098_1_ == null ? ItemStack.EMPTY : this.menu.getSlot(p_184098_1_.index).getItem();
                this.menu.clicked(p_184098_1_ == null ? p_184098_2_ : p_184098_1_.index, p_184098_3_, p_184098_4_, this.minecraft.player);
                if (Container.getQuickcraftHeader(p_184098_3_) == 2) {
                    for(int k = 0; k < 9; ++k) {
                        this.minecraft.gameMode.handleCreativeModeItemAdd(this.menu.getSlot(45 + k).getItem(), 36 + k);
                    }
                } else if (p_184098_1_ != null) {
                    ItemStack itemstack4 = this.menu.getSlot(p_184098_1_.index).getItem();
                    this.minecraft.gameMode.handleCreativeModeItemAdd(itemstack4, p_184098_1_.index - (this.menu).slots.size() + 9 + 36);
                    int i = 45 + p_184098_3_;
                    if (p_184098_4_ == ClickType.SWAP) {
                        this.minecraft.gameMode.handleCreativeModeItemAdd(itemstack3, i - (this.menu).slots.size() + 9 + 36);
                    } else if (p_184098_4_ == ClickType.THROW && !itemstack3.isEmpty()) {
                        ItemStack itemstack2 = itemstack3.copy();
                        itemstack2.setCount(p_184098_3_ == 0 ? 1 : itemstack2.getMaxStackSize());
                        this.minecraft.player.drop(itemstack2, true);
                        this.minecraft.gameMode.handleCreativeModeItemDrop(itemstack2);
                    }

                    this.minecraft.player.inventoryMenu.broadcastChanges();
                }
            }
        }

    }
}
