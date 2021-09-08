package com.bedmen.odyssey.container;

import java.util.Map;

import com.bedmen.odyssey.registry.ContainerRegistry;
import com.bedmen.odyssey.util.AnvilUtil;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OdysseyRepairContainer extends AbstractRepairContainer {
    private static final Logger LOGGER = LogManager.getLogger();
    public int repairItemCountCost;
    private String itemName;
    private final IntReferenceHolder cost = IntReferenceHolder.standalone();

    public OdysseyRepairContainer(int p_i50101_1_, PlayerInventory p_i50101_2_) {
        this(p_i50101_1_, p_i50101_2_, IWorldPosCallable.NULL);
    }

    public OdysseyRepairContainer(int p_i50102_1_, PlayerInventory p_i50102_2_, IWorldPosCallable p_i50102_3_) {
        super(ContainerRegistry.ANVIL.get(), p_i50102_1_, p_i50102_2_, p_i50102_3_);
        this.addDataSlot(this.cost);
    }

    protected boolean isValidBlock(BlockState p_230302_1_) {
        return p_230302_1_.is(BlockTags.ANVIL);
    }

    protected boolean mayPickup(PlayerEntity p_230303_1_, boolean p_230303_2_) {
        return (p_230303_1_.abilities.instabuild || p_230303_1_.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
    }

    protected ItemStack onTake(PlayerEntity p_230301_1_, ItemStack p_230301_2_) {
        if (!p_230301_1_.abilities.instabuild) {
            p_230301_1_.giveExperienceLevels(-this.cost.get());
        }

        float breakChance = net.minecraftforge.common.ForgeHooks.onAnvilRepair(p_230301_1_, p_230301_2_, OdysseyRepairContainer.this.inputSlots.getItem(0), OdysseyRepairContainer.this.inputSlots.getItem(1));

        this.inputSlots.setItem(0, ItemStack.EMPTY);
        if (this.repairItemCountCost > 0) {
            ItemStack itemstack = this.inputSlots.getItem(1);
            if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
                itemstack.shrink(this.repairItemCountCost);
                this.inputSlots.setItem(1, itemstack);
            } else {
                this.inputSlots.setItem(1, ItemStack.EMPTY);
            }
        } else {
            this.inputSlots.setItem(1, ItemStack.EMPTY);
        }

        this.cost.set(0);
        this.access.execute((p_234633_1_, p_234633_2_) -> {
            BlockState blockstate = p_234633_1_.getBlockState(p_234633_2_);
            if (!p_230301_1_.abilities.instabuild && blockstate.is(BlockTags.ANVIL) && p_230301_1_.getRandom().nextFloat() < breakChance) {
                BlockState blockstate1 = AnvilBlock.damage(blockstate);
                if (blockstate1 == null) {
                    p_234633_1_.removeBlock(p_234633_2_, false);
                    p_234633_1_.levelEvent(1029, p_234633_2_, 0);
                } else {
                    p_234633_1_.setBlock(p_234633_2_, blockstate1, 2);
                    p_234633_1_.levelEvent(1030, p_234633_2_, 0);
                }
            } else {
                p_234633_1_.levelEvent(1030, p_234633_2_, 0);
            }

        });
        return p_230301_2_;
    }

    public void createResult() {
        ItemStack itemstack = this.inputSlots.getItem(0);
        this.cost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        if (itemstack.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
        } else {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = this.inputSlots.getItem(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
            j = j + itemstack.getBaseRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getBaseRepairCost());
            this.repairItemCountCost = 0;
            boolean flag = false;

            if (!itemstack2.isEmpty()) {
                //if (!net.minecraftforge.common.ForgeHooks.onAnvilChange(this, itemstack, itemstack2, resultSlots, itemName, j, this.player)) return;
                if (itemstack1.isDamageableItem() && itemstack1.getItem().isValidRepairItem(itemstack, itemstack2)) {
                    int l2 = Math.min(itemstack1.getDamageValue(), AnvilUtil.getRepairAmount(itemstack1));
                    if (l2 <= 0) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    int i3;
                    for(i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
                        int j3 = itemstack1.getDamageValue() - l2;
                        itemstack1.setDamageValue(j3);
                        ++i;
                        l2 = Math.min(itemstack1.getDamageValue(), AnvilUtil.getRepairAmount(itemstack1));
                    }

                    this.repairItemCountCost = i3;
                } else if(itemstack2.getItem() == Items.PAPER) {
                    i++;
                    CompoundNBT compoundNBT = itemstack1.getOrCreateTagElement("display");
                    if (itemstack2.hasCustomHoverName()) {
                        ITextComponent iTextComponent = itemstack2.getHoverName();
                        if (compoundNBT.getTagType("Lore") == 9) {
                            ListNBT listNBT = compoundNBT.getList("Lore", 8);
                            if(listNBT.size() >= 5){
                                this.resultSlots.setItem(0, ItemStack.EMPTY);
                                this.cost.set(0);
                                return;
                            }
                            listNBT.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(iTextComponent)));
                        } else {
                            ListNBT listNBT = new ListNBT();
                            listNBT.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(iTextComponent)));
                            compoundNBT.put("Lore", listNBT);
                        }
                    } else if (compoundNBT.getTagType("Lore") == 9){
                        compoundNBT.remove("Lore");
                    } else {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }
                } else {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.cost.set(0);
                    return;
                }
            }

            if (StringUtils.isBlank(this.itemName)) {
                if (itemstack.hasCustomHoverName()) {
                    k = 1;
                    i += k;
                    itemstack1.resetHoverName();
                }
            } else if (!this.itemName.equals(itemstack.getHoverName().getString())) {
                k = 1;
                i += k;
                itemstack1.setHoverName(new StringTextComponent(this.itemName));
            }

            this.cost.set(j + i);
            if (i <= 0) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && this.cost.get() >= 40) {
                this.cost.set(39);
            }

            if (this.cost.get() >= 40 && !this.player.abilities.instabuild) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty()) {
                int k2 = itemstack1.getBaseRepairCost();
                if (!itemstack2.isEmpty() && k2 < itemstack2.getBaseRepairCost()) {
                    k2 = itemstack2.getBaseRepairCost();
                }

                EnchantmentHelper.setEnchantments(map, itemstack1);
            }

            this.resultSlots.setItem(0, itemstack1);
            this.broadcastChanges();
        }
    }

    public void setItemName(String p_82850_1_) {
        this.itemName = p_82850_1_;
        if (this.getSlot(2).hasItem()) {
            ItemStack itemstack = this.getSlot(2).getItem();
            if (StringUtils.isBlank(p_82850_1_)) {
                itemstack.resetHoverName();
            } else {
                itemstack.setHoverName(new StringTextComponent(this.itemName));
            }
        }

        this.createResult();
    }

    @OnlyIn(Dist.CLIENT)
    public int getCost() {
        return this.cost.get();
    }

    public void setMaximumCost(int value) {
        this.cost.set(value);
    }
}
