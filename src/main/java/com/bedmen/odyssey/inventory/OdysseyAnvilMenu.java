package com.bedmen.odyssey.inventory;

import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.items.aspect_items.BoomerangItem;
import com.bedmen.odyssey.items.aspect_items.OdysseyMeleeItem;
import com.bedmen.odyssey.magic.MagicUtil;
import com.bedmen.odyssey.registry.ContainerRegistry;
import com.mojang.logging.LogUtils;

import java.util.List;
import java.util.Optional;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public class OdysseyAnvilMenu extends ItemCombinerMenu {
    public int repairItemCount;
    private String itemName;
    private final DataSlot cost = DataSlot.standalone();

    public OdysseyAnvilMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public OdysseyAnvilMenu(int id, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(ContainerRegistry.ANVIL.get(), id, inventory, containerLevelAccess);
        this.addDataSlot(this.cost);
    }

    protected boolean isValidBlock(BlockState blockState) {
        return blockState.is(BlockTags.ANVIL);
    }

    protected boolean mayPickup(Player player, boolean flag) {
        return (player.getAbilities().instabuild || player.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
    }

    protected void onTake(Player player, ItemStack itemStack) {
        if (!player.getAbilities().instabuild) {
            player.giveExperienceLevels(-this.cost.get());
        }

        float breakChance = net.minecraftforge.common.ForgeHooks.onAnvilRepair(player, itemStack, OdysseyAnvilMenu.this.inputSlots.getItem(0), OdysseyAnvilMenu.this.inputSlots.getItem(1));

        this.inputSlots.setItem(0, ItemStack.EMPTY);
        if (this.repairItemCount > 0) {
            ItemStack itemstack = this.inputSlots.getItem(1);
            if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCount) {
                itemstack.shrink(this.repairItemCount);
                this.inputSlots.setItem(1, itemstack);
            } else {
                this.inputSlots.setItem(1, ItemStack.EMPTY);
            }
        } else {
            this.inputSlots.setItem(1, ItemStack.EMPTY);
        }

        this.cost.set(0);
        this.access.execute((level, blockPos) -> {
            BlockState blockstate = level.getBlockState(blockPos);
            if (!player.getAbilities().instabuild && blockstate.is(BlockTags.ANVIL) && player.getRandom().nextFloat() < breakChance) {
                BlockState blockstate1 = AnvilBlock.damage(blockstate);
                if (blockstate1 == null) {
                    level.removeBlock(blockPos, false);
                    level.levelEvent(1029, blockPos, 0);
                } else {
                    level.setBlock(blockPos, blockstate1, 2);
                    level.levelEvent(1030, blockPos, 0);
                }
            } else {
                level.levelEvent(1030, blockPos, 0);
            }

        });
    }

    public void createResult() {
        ItemStack inputStack0 = this.inputSlots.getItem(0);
        this.cost.set(1);
        int levelCost = 0;
        if (inputStack0.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
        } else {
            ItemStack resultStack = inputStack0.copy();
            ItemStack inputStack1 = this.inputSlots.getItem(1);
            this.repairItemCount = 0;

            if (!inputStack1.isEmpty()) {
                int repairAmountPerRepairItem = getRepairAmount(resultStack);
                // Repairing with material specific repair item
                if (resultStack.isDamageableItem() && resultStack.getItem().isValidRepairItem(inputStack0, inputStack1)) {
                    int repairDurability = Math.min(resultStack.getDamageValue(), repairAmountPerRepairItem);
                    if (repairDurability <= 0) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    int repairItemRunningCount;
                    for(repairItemRunningCount = 0; repairDurability > 0 && repairItemRunningCount < inputStack1.getCount(); ++repairItemRunningCount) {
                        int resultDurabilityDamage = resultStack.getDamageValue() - repairDurability;
                        resultStack.setDamageValue(resultDurabilityDamage);
                        ++levelCost;
                        repairDurability = Math.min(resultStack.getDamageValue(), repairAmountPerRepairItem);
                    }

                    this.repairItemCount = repairItemRunningCount;
                } else {
                    // If 2nd item is not repair material, cannot anvil together two different items
                    if (!resultStack.is(inputStack1.getItem())) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    if (resultStack.isDamageableItem()) {
                        int input0RemainingDurability = inputStack0.getMaxDamage() - inputStack0.getDamageValue();
                        int input1RemainingDurability = inputStack1.getMaxDamage() - inputStack1.getDamageValue();
                        int resultRemainingDurability = input0RemainingDurability + input1RemainingDurability + resultStack.getMaxDamage() * 12 / 100;
                        int resultDurabilityDamage = resultStack.getMaxDamage() - resultRemainingDurability;
                        if (resultDurabilityDamage < 0) {
                            resultDurabilityDamage = 0;
                        }
                        if (resultDurabilityDamage < resultStack.getDamageValue()) {
                            int averageRepairAmount = resultRemainingDurability - (input0RemainingDurability + input1RemainingDurability)/2;
                            int numberOfRepairItemsWorth = Mth.ceil((double)averageRepairAmount/(double)repairAmountPerRepairItem);
                            resultStack.setDamageValue(resultDurabilityDamage);
                            levelCost += numberOfRepairItemsWorth;
                        }
                    }

                    // Modifier combining
                    List<AspectInstance> resultModifierList = AspectUtil.getAddedModifiersAsAspectInstanceList(resultStack);
                    List<AspectInstance> input1ModifierList = AspectUtil.getAddedModifiersAsAspectInstanceList(inputStack1);
                    while(!input1ModifierList.isEmpty() && AspectUtil.getModifiabilityRemaining(resultStack) > 0){
                        AspectInstance input1Modifier = input1ModifierList.get(0);
                        input1ModifierList.remove(0);
                        Optional<AspectInstance> optionalMatchingModifier = resultModifierList.stream().filter(resultModifier -> resultModifier.aspect == input1Modifier.aspect).findFirst();
                        float additionalModifiability;
                        if(optionalMatchingModifier.isPresent()){
                            AspectInstance matchingModifier = optionalMatchingModifier.get();
                            if(matchingModifier.strength >= input1Modifier.strength){
                                continue;
                            } else {
                                additionalModifiability = input1Modifier.getModifiability(resultStack) - matchingModifier.getModifiability(resultStack);
                            }
                        } else {
                            additionalModifiability = input1Modifier.getModifiability(resultStack);
                        }
                        if(AspectUtil.getModifiabilityRemaining(resultStack) >= additionalModifiability){
                            AspectUtil.replaceModifier(resultStack, input1Modifier);
                        }
                    }
                    float input0Modifiability = AspectUtil.getUsedModifiability(inputStack0);
                    float input1Modifiability = AspectUtil.getUsedModifiability(inputStack1);
                    float resultModifiability = AspectUtil.getUsedModifiability(resultStack);
                    float averageModifiabilityIncrease = resultModifiability - (input0Modifiability + input1Modifiability)/2.0f;
                    if(averageModifiabilityIncrease > 0.0f){
                        levelCost += Mth.ceil(averageModifiabilityIncrease * MagicUtil.MODIFIABILITY_TO_LEVEL_COST_FACTOR);
                    }
                }
            }

            // Renaming
            if (StringUtils.isBlank(this.itemName)) {
                if (inputStack0.hasCustomHoverName()) {
                    levelCost++;
                    resultStack.resetHoverName();
                }
            } else if (!this.itemName.equals(inputStack0.getHoverName().getString())) {
                levelCost++;
                resultStack.setHoverName(new TextComponent(this.itemName));
            }

            // Set the level cost
            this.cost.set(levelCost);
            if (levelCost <= 0) {
                resultStack = ItemStack.EMPTY;
            }

            this.resultSlots.setItem(0, resultStack);
            this.broadcastChanges();
        }
    }

    public static int getRepairAmount(ItemStack itemStack){
        int repairNumber = getRepairNumber(itemStack);
        int maxDamage = itemStack.getMaxDamage();
        return (int) Math.ceil(((double)maxDamage)/((double)repairNumber));
    }

    private static int getRepairNumber(ItemStack itemStack){
        Item item = itemStack.getItem();
        if(item instanceof OdysseyMeleeItem odysseyMeleeItem){
            return odysseyMeleeItem.getMeleeWeaponClass().meleeWeaponType.repairNumber;
        }
        if(item instanceof ArmorItem armorItem){
            return switch (armorItem.getSlot()) {
                case CHEST -> 8;
                case LEGS -> 7;
                case HEAD -> 5;
                default -> 4;
            };
        }
        if(item instanceof ShieldItem){
            return 6;
        }
        if(item instanceof BowItem || item instanceof CrossbowItem){
            return 3;
        }
        if(item instanceof BoomerangItem boomerangItem){
            return boomerangItem.getBoomerangType().repairNumber;
        }
        return 4;
    }

    public void setItemName(String name) {
        this.itemName = name;
        if (this.getSlot(2).hasItem()) {
            ItemStack itemstack = this.getSlot(2).getItem();
            if (StringUtils.isBlank(name)) {
                itemstack.resetHoverName();
            } else {
                itemstack.setHoverName(new TextComponent(this.itemName));
            }
        }

        this.createResult();
    }

    public int getCost() {
        return this.cost.get();
    }

    public void setMaximumCost(int value) {
        this.cost.set(value);
    }
}
