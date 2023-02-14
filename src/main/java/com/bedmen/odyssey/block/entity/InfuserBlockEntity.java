package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.aspect.AspectItemPredicates;
import com.bedmen.odyssey.aspect.AspectUtil;
import com.bedmen.odyssey.aspect.encapsulator.AspectInstance;
import com.bedmen.odyssey.combat.OdysseyRangedAmmoWeapon;
import com.bedmen.odyssey.items.aspect_items.InnateAspectItem;
import com.bedmen.odyssey.items.aspect_items.QuiverItem;
import com.bedmen.odyssey.items.aspect_items.SpearItem;
import com.bedmen.odyssey.items.aspect_items.ThrowableWeaponItem;
import com.bedmen.odyssey.magic.ExperienceCost;
import com.bedmen.odyssey.recipes.InfuserCraftingRecipe;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import com.bedmen.odyssey.util.StringUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InfuserBlockEntity extends InfusionPedestalBlockEntity {

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    private static final Class<?>[] DIGGER_CLASSES = new Class<?>[]{PickaxeItem.class, AxeItem.class, HoeItem.class, ShovelItem.class};
    private static final int DISTANCE_TO_PEDESTALS = 3;
    private static final double MAX_PLAYER_DISTANCE = 10.0d;

    protected ItemStack oldItemStack = ItemStack.EMPTY;
    private static final String OLD_ITEM_STACK_TAG = Odyssey.MOD_ID + ":OldItemStack";
    protected Map<Direction, ItemStack> oldPedestalItemStackMap = new HashMap<>();
    private static final String OLD_PEDESTAL_ITEM_STACKS_TAG = Odyssey.MOD_ID + ":OldPedestalItemStacks";
    protected Map<Direction, ItemStack> newPedestalItemStackMap = new HashMap<>();
    private static final String NEW_PEDESTAL_ITEM_STACKS_TAG = Odyssey.MOD_ID + ":NewPedestalItemStacks";

    public InfuserBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.INFUSER.get(), blockPos, blockState);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, InfuserBlockEntity infuserBlockEntity) {
        infuserBlockEntity.updateNewItemStacks();

        Optional<InfuserCraftingRecipe> optionalInfuserCraftingRecipe = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.INFUSER_CRAFTING.get())
                .stream().filter(infuserCraftingRecipe -> infuserCraftingRecipe.matches(infuserBlockEntity.getItemStackOriginal(), infuserBlockEntity.newPedestalItemStackMap.values())).findFirst();
        optionalInfuserCraftingRecipe.ifPresentOrElse(
            infuserCraftingRecipe -> {
                int count = infuserBlockEntity.getMinimumCountOfInputItemStacks();
                ExperienceCost experienceCost = infuserCraftingRecipe.experienceCost.multiplyCost(count);
                if(infuserBlockEntity.tryToPayExperienceCost(experienceCost)){
                    infuserBlockEntity.reduceItemStackCountOnAllInfusionPedestals(count);
                    infuserBlockEntity.setItemStack(infuserCraftingRecipe.getResultItemWithOldItemStackData(infuserBlockEntity.getItemStackOriginal()));
                    infuserBlockEntity.setItemStackCount(count);
                }
        }, infuserBlockEntity::tryInfusion);

        infuserBlockEntity.updateOldItemStacks();
    }

    private Optional<InfusionPedestalBlockEntity> getInfusionPedestalBlockEntity(Direction direction){
        BlockPos pedestalBlockPos = this.getBlockPos().relative(direction, DISTANCE_TO_PEDESTALS);
        if(this.level != null){
            BlockEntity blockEntity = this.level.getBlockEntity(pedestalBlockPos);
            if(blockEntity instanceof InfusionPedestalBlockEntity infusionPedestalBlockEntity && !(blockEntity instanceof InfuserBlockEntity)){
                return Optional.of(infusionPedestalBlockEntity);
            }
        }
        return Optional.empty();
    }

    private Optional<Player> getPlayerFromInfusionPedestal(Direction direction){
        return this.getInfusionPedestalBlockEntity(direction).flatMap(InfusionPedestalBlockEntity::getPlayer);
    }

    private void reduceItemStackCountOnAllInfusionPedestals(int reductionCount){
        for(Direction direction: HORIZONTALS){
            this.getInfusionPedestalBlockEntity(direction).ifPresent(infusionPedestalBlockEntity -> {
                infusionPedestalBlockEntity.shrinkItemStack(reductionCount);
            });
        }
        this.shrinkItemStack(reductionCount);
        if(!this.getItemStackOriginal().isEmpty() && this.level != null){
            Containers.dropItemStack(this.level, this.getBlockPos().getX(), this.getBlockPos().getY()+1.0d, this.getBlockPos().getZ(), this.getItemStackCopy());
        }
    }
    
    private int getMinimumCountOfInputItemStacks(){
        int minimumCount = this.getItemStackOriginal().getCount();
        for(Direction direction: HORIZONTALS){
            ItemStack pedestalItemStack = this.newPedestalItemStackMap.get(direction);
            if(!pedestalItemStack.isEmpty()){
                int pedestalItemStackCount = pedestalItemStack.getCount();
                if(pedestalItemStackCount < minimumCount){
                    minimumCount = pedestalItemStackCount;
                }
            }
        }
        return minimumCount;
    }

    private void tryInfusion(){
        for(Direction direction: HORIZONTALS){
            ItemStack pedestalItemStack = this.newPedestalItemStackMap.get(direction);
            if(canInfuse(this.getItemStackOriginal(), pedestalItemStack)){
                List<AspectInstance> infusionModifierList = getValidInfusionModifiers(this.getItemStackOriginal(), pedestalItemStack);
                if(infusionModifierList.size() > 0){
                    List<AspectInstance> adjustedModifierList = infusionModifierList.stream().map(AspectInstance::applyInfusionPenalty).collect(Collectors.toList());
                    float modifiabilityToBeUsed = adjustedModifierList.stream().map(aspectInstance -> aspectInstance.getModifiability(this.getItemStackOriginal())).reduce(0.0f, Float::sum);
                    ExperienceCost experienceCost = new ExperienceCost(modifiabilityToBeUsed * 2.0f);
                    if(AspectUtil.getModifiabilityRemaining(this.getItemStackOriginal()) >= modifiabilityToBeUsed){
                        if(this.tryToPayExperienceCost(experienceCost)){
                            this.getInfusionPedestalBlockEntity(direction).ifPresent(infusionPedestalBlockEntity -> infusionPedestalBlockEntity.setItemStack(ItemStack.EMPTY));
                            adjustedModifierList.forEach(adjustedAspectInstance -> AspectUtil.addModifier(this.getItemStackOriginal(), adjustedAspectInstance));
                            this.markUpdated();
                        }
                    } else {
                        this.getNearbyPlayersWhoMadeChanges().forEach(
                                serverPlayer -> serverPlayer.sendMessage(new TranslatableComponent("magic.oddc.modifiability", StringUtil.floatFormat(modifiabilityToBeUsed)), ChatType.GAME_INFO, Util.NIL_UUID)
                        );
                    }
                }
            }
        }
    }

    private static boolean canInfuse(ItemStack infuserItemStack, ItemStack pedestalItemStack){
        Item pedestalItem = pedestalItemStack.getItem();
        if(pedestalItem instanceof InnateAspectItem pedestalInnateAspectItem){
            Item infuserItem = infuserItemStack.getItem();
            // Make sure all aspect item predicates pass
            List<AspectInstance> pedestalInnateModifierList = pedestalInnateAspectItem.getInnateAspectHolder().innateModifierList;
            if(pedestalInnateModifierList.size() <= 0){
                return false;
            }
            // If both items are digger tools, the tool type has to match
            if(infuserItem instanceof DiggerItem && pedestalItem instanceof DiggerItem){
                boolean diggerClassesMatch = false;
                for(Class<?> diggerClass: DIGGER_CLASSES){
                    if(diggerClass.isInstance(infuserItem) && diggerClass.isInstance(pedestalItem)){
                        diggerClassesMatch = true;
                        break;
                    }
                }
                if(!diggerClassesMatch){
                    return false;
                }
            }
            // If infuserItem is armor, then pedestalItem must be armor and the slot has to match (some exceptions based on requiresSlotsAreSame)
            if(infuserItem instanceof ArmorItem infuserArmorItem){
                if(pedestalItem instanceof ArmorItem pedestalArmorItem){
                    // Checks if any of the innate modifiers require the slots to be the same
                    return infuserArmorItem.getSlot() == pedestalArmorItem.getSlot();
                } else {
                    return false;
                }
            } else if(infuserItem instanceof OdysseyRangedAmmoWeapon){
                return pedestalItem instanceof OdysseyRangedAmmoWeapon;
            } else if(infuserItem instanceof ThrowableWeaponItem && !(infuserItem instanceof SpearItem)){
                return pedestalItem instanceof ThrowableWeaponItem;
            } else if(infuserItem instanceof SpearItem){
                return pedestalItem instanceof ThrowableWeaponItem || AspectItemPredicates.MELEE.test(pedestalItem);
            }else if(infuserItem instanceof QuiverItem){
                return pedestalItem instanceof QuiverItem;
            } else if(infuserItem instanceof ArrowItem){
                return pedestalItem instanceof ArrowItem;
            } else if(infuserItem instanceof ShieldItem){
                return pedestalItem instanceof ShieldItem;
            } else if(infuserItem instanceof DiggerItem && !AspectItemPredicates.MELEE.test(pedestalItem)){
                return pedestalItem instanceof DiggerItem;
            } else if(infuserItem instanceof DiggerItem && AspectItemPredicates.MELEE.test(pedestalItem)){
                return pedestalItem instanceof DiggerItem || AspectItemPredicates.MELEE.test(pedestalItem) || pedestalItem instanceof SpearItem;
            } else if(!(infuserItem instanceof DiggerItem) && AspectItemPredicates.MELEE.test(pedestalItem)){
                return AspectItemPredicates.MELEE.test(pedestalItem) || pedestalItem instanceof SpearItem;
            }
        }
        return false;
    }

    private static List<AspectInstance> getValidInfusionModifiers(ItemStack infuserItemStack, ItemStack pedestalItemStack){
        List<AspectInstance> pedestalInnateModifierList = ((InnateAspectItem)pedestalItemStack.getItem()).getInnateAspectHolder().innateModifierList;
        // Remove any modifiers that are already innate on the infuserItemStack
        List<AspectInstance> validModifierList = new ArrayList<>(pedestalInnateModifierList);
        for(AspectInstance pedestalAspectInstance: pedestalInnateModifierList){
            if(!pedestalAspectInstance.aspect.itemPredicate.test(infuserItemStack.getItem())){
                validModifierList.remove(pedestalAspectInstance);
            } else if(AspectUtil.getAspectStrength(infuserItemStack, pedestalAspectInstance.aspect) > 0.0f){
                validModifierList.remove(pedestalAspectInstance);
            }
        }
        return validModifierList;
    }

    private Set<ServerPlayer> getNearbyPlayersWhoMadeChanges(){
        List<Player> playerList = new ArrayList<>();
        Optional<Player> optionalPlayer = this.getPlayer();
        if(!sameItemStack(this.getItemStackOriginal(), this.oldItemStack) && optionalPlayer.isPresent()){
            playerList.add(optionalPlayer.get());
        }
        playerList.addAll(Arrays.stream(HORIZONTALS)
                .filter(direction -> !sameItemStack(this.newPedestalItemStackMap.get(direction), this.oldPedestalItemStackMap.get(direction)))
                .map(this::getPlayerFromInfusionPedestal)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
        return playerList.stream().flatMap(player -> player instanceof ServerPlayer serverPlayer && this.getBlockPos().distToCenterSqr(serverPlayer.position()) < MAX_PLAYER_DISTANCE * MAX_PLAYER_DISTANCE
                ? Stream.of(serverPlayer)
                : Stream.of()).collect(Collectors.toSet());
    }

    private static boolean sameItemStack(ItemStack itemStack1, ItemStack itemStack2){
        return ItemStack.isSameItemSameTags(itemStack1, itemStack2) && itemStack1.getCount() == itemStack2.getCount();
    }

    private boolean tryToPayExperienceCost(ExperienceCost experienceCost){
        Set<ServerPlayer> serverPlayerSet = this.getNearbyPlayersWhoMadeChanges();
        Set<ServerPlayer> serverPlayersWhoCanPay = serverPlayerSet.stream().filter(experienceCost::canPay).collect(Collectors.toSet());
        if(serverPlayersWhoCanPay.isEmpty()){
            serverPlayerSet.forEach(experienceCost::displayRequirementMessage);
            return false;
        } else {
            ServerPlayer serverPlayer = serverPlayersWhoCanPay.stream().findFirst().get();
            experienceCost.pay(serverPlayer);
            return true;
        }
    }

    private void updateNewItemStacks(){
        for(Direction direction: HORIZONTALS){
            this.getInfusionPedestalBlockEntity(direction)
                    .ifPresentOrElse(
                            infusionPedestalBlockEntity -> this.newPedestalItemStackMap.put(direction, infusionPedestalBlockEntity.getItemStackCopy()),
                            () -> this.newPedestalItemStackMap.put(direction, ItemStack.EMPTY)
                    );
        }
    }

    private void updateOldItemStacks(){
        this.oldItemStack = this.getItemStackOriginal();
        this.oldPedestalItemStackMap.clear();
        this.oldPedestalItemStackMap.putAll(this.newPedestalItemStackMap);
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.put(OLD_ITEM_STACK_TAG, this.oldItemStack.save(new CompoundTag()));
        compoundTag.put(OLD_PEDESTAL_ITEM_STACKS_TAG, itemStackMapToTag(this.oldPedestalItemStackMap));
        compoundTag.put(NEW_PEDESTAL_ITEM_STACKS_TAG, itemStackMapToTag(this.newPedestalItemStackMap));
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if(compoundTag.contains(OLD_ITEM_STACK_TAG)){
            this.oldItemStack = ItemStack.of(compoundTag.getCompound(OLD_ITEM_STACK_TAG));
        }
        if(compoundTag.contains(OLD_PEDESTAL_ITEM_STACKS_TAG)){
            this.oldPedestalItemStackMap = itemStackMapFromTag(compoundTag.getCompound(OLD_PEDESTAL_ITEM_STACKS_TAG));
        }
        if(compoundTag.contains(NEW_PEDESTAL_ITEM_STACKS_TAG)){
            this.newPedestalItemStackMap = itemStackMapFromTag(compoundTag.getCompound(NEW_PEDESTAL_ITEM_STACKS_TAG));
        }
    }

    protected static CompoundTag itemStackMapToTag(Map<Direction, ItemStack> itemStackMap){
        CompoundTag compoundTag = new CompoundTag();
        itemStackMap.forEach((direction, itemStack) -> compoundTag.put(direction.name(), itemStack.save(new CompoundTag())));
        return compoundTag;
    }

    protected static Map<Direction, ItemStack> itemStackMapFromTag(CompoundTag compoundTag){
        Map<Direction, ItemStack> map = new HashMap<>();
        compoundTag.getAllKeys().forEach(key -> map.put(Direction.valueOf(key), ItemStack.of(compoundTag.getCompound(key))));
        return map;
    }
}
