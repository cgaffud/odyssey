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
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InfuserBlockEntity extends InfusionPedestalBlockEntity {

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    private static final Class<?>[] DIGGER_CLASSES = new Class<?>[]{PickaxeItem.class, AxeItem.class, HoeItem.class, ShovelItem.class};
    private static final int DISTANCE_TO_PEDESTALS = 3;
    private static final double MAX_PLAYER_DISTANCE = 10.0d;
    private static final int TOTAL_INFUSION_TIME = 60;

    protected ItemStack oldItemStack = ItemStack.EMPTY;
    private static final String OLD_ITEM_STACK_TAG = Odyssey.MOD_ID + ":OldItemStack";
    protected Map<Direction, ItemStack> oldPedestalItemStackMap = new HashMap<>();
    private static final String OLD_PEDESTAL_ITEM_STACKS_TAG = Odyssey.MOD_ID + ":OldPedestalItemStacks";
    protected Map<Direction, ItemStack> newPedestalItemStackMap = new HashMap<>();
    private static final String NEW_PEDESTAL_ITEM_STACKS_TAG = Odyssey.MOD_ID + ":NewPedestalItemStacks";
    protected int infuserCraftingTicks = 0;
    private static final String INFUSER_CRAFTING_TICKS_TAG = Odyssey.MOD_ID + ":InfuserCraftingTicks";
    protected Map<Direction, Integer> infusingTicksMap = new HashMap<>();
    private static final String INFUSING_TICKS_MAP_TAG = Odyssey.MOD_ID + ":InfusingTicksMap";

    public InfuserBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.INFUSER.get(), blockPos, blockState);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, InfuserBlockEntity infuserBlockEntity) {
        infuserBlockEntity.updateNewItemStacks();

        Optional<InfuserCraftingRecipe> optionalInfuserCraftingRecipe = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.INFUSER_CRAFTING.get())
                .stream().filter(infuserCraftingRecipe -> infuserCraftingRecipe.matches(infuserBlockEntity.getItemStackOriginal(), infuserBlockEntity.newPedestalItemStackMap.values())).findFirst();
        optionalInfuserCraftingRecipe.ifPresentOrElse(infuserBlockEntity::tryInfuserCrafting, infuserBlockEntity::tryInfusion);

        infuserBlockEntity.updateOldItemStacks();
    }

    private void tryInfuserCrafting(InfuserCraftingRecipe infuserCraftingRecipe){
        int count = this.getMinimumCountOfInputItemStacks();
        if(!this.isInfuserCrafting()){
            ExperienceCost experienceCost = infuserCraftingRecipe.experienceCost.multiplyCost(count);
            if(this.tryToPayExperienceCost(experienceCost)){
                this.infuserCraftingTicks++;
                this.stopAllInfusing();
            }
        } else if(this.infuserCraftingTicks < TOTAL_INFUSION_TIME){
            this.infuserCraftingTicks++;
        } else {
            this.reduceItemStackCountOnAllInfusionPedestals(count);
            this.setItemStack(infuserCraftingRecipe.getResultItemWithOldItemStackData(this.getItemStackOriginal()));
            this.setItemStackCount(count);
            this.stopInfuserCrafting();
        }
    }

    private boolean isInfuserCrafting(){
        return this.infuserCraftingTicks > 0;
    }

    private boolean isInfusing(Direction direction){
        Integer integer = this.infusingTicksMap.get(direction);
        return integer != null && integer > 0;
    }

    private void stopInfuserCrafting(){
        this.infuserCraftingTicks = 0;
    }

    private void stopInfusing(Direction direction){
        this.infusingTicksMap.put(direction, 0);
    }

    private void stopAllInfusing(){
        this.infusingTicksMap.keySet().forEach(this::stopInfusing);
    }

    private void incrementInfusingTick(Direction direction){
        Integer integer = this.infusingTicksMap.get(direction);
        int i = integer == null ? 0 : integer;
        this.infusingTicksMap.put(direction, i+1);
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
        this.stopInfuserCrafting();
        for(Direction direction: HORIZONTALS){
            ItemStack pedestalItemStack = this.newPedestalItemStackMap.get(direction);
            if(canInfuse(this.getItemStackOriginal(), pedestalItemStack)){
                List<AspectInstance> infusionModifierList = getValidInfusionModifiers(this.getItemStackOriginal(), pedestalItemStack);
                if(infusionModifierList.size() > 0){
                    List<AspectInstance> adjustedModifierList = infusionModifierList.stream().map(AspectInstance::applyInfusionPenalty).collect(Collectors.toList());
                    float modifiabilityToBeUsed = adjustedModifierList.stream().map(aspectInstance -> aspectInstance.getModifiability(this.getItemStackOriginal())).reduce(0.0f, Float::sum);
                    if(AspectUtil.getModifiabilityRemaining(this.getItemStackOriginal()) >= modifiabilityToBeUsed){
                        if(!this.isInfusing(direction)){
                            ExperienceCost experienceCost = new ExperienceCost(modifiabilityToBeUsed * 2.0f);
                            if(this.tryToPayExperienceCost(experienceCost)){
                                this.incrementInfusingTick(direction);
                            }
                        } else if(this.isInfusing(direction) && this.infusingTicksMap.get(direction) < TOTAL_INFUSION_TIME){
                            this.incrementInfusingTick(direction);
                        } else {
                            this.getInfusionPedestalBlockEntity(direction).ifPresent(infusionPedestalBlockEntity -> infusionPedestalBlockEntity.setItemStack(ItemStack.EMPTY));
                            adjustedModifierList.forEach(adjustedAspectInstance -> AspectUtil.addModifier(this.getItemStackOriginal(), adjustedAspectInstance));
                            this.markUpdated();
                            this.stopInfusing(direction);
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
        compoundTag.putInt(INFUSER_CRAFTING_TICKS_TAG, this.infuserCraftingTicks);
        compoundTag.put(INFUSING_TICKS_MAP_TAG, ticksMapToTag(this.infusingTicksMap));
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
        this.infuserCraftingTicks = compoundTag.getInt(INFUSER_CRAFTING_TICKS_TAG);
        if(compoundTag.contains(INFUSING_TICKS_MAP_TAG)){
            this.infusingTicksMap = ticksMapFromTag(compoundTag.getCompound(INFUSING_TICKS_MAP_TAG));
        }
    }

    protected static CompoundTag itemStackMapToTag(Map<Direction, ItemStack> itemStackMap){
        return directionMapToTag(itemStackMap, itemStack -> itemStack.save(new CompoundTag()));
    }

    protected static CompoundTag ticksMapToTag(Map<Direction, Integer> ticksMap){
        return directionMapToTag(ticksMap, IntTag::valueOf);
    }

    protected static <T> CompoundTag directionMapToTag(Map<Direction, T> directionMap, Function<T, Tag> toCompoundTagFunction){
        CompoundTag compoundTag = new CompoundTag();
        directionMap.forEach((direction, object) -> compoundTag.put(direction.name(), toCompoundTagFunction.apply(object)));
        return compoundTag;
    }

    protected static Map<Direction, ItemStack> itemStackMapFromTag(CompoundTag compoundTag){
        return directionMapFromTag(compoundTag, tag -> ItemStack.of((CompoundTag) tag));
    }

    protected static Map<Direction, Integer> ticksMapFromTag(CompoundTag compoundTag){
        return directionMapFromTag(compoundTag, tag -> ((IntTag)tag).getAsInt());
    }

    protected static <T> Map<Direction, T> directionMapFromTag(CompoundTag compoundTag, Function<Tag, T> fromCompoundTagFunction){
        Map<Direction, T> map = new HashMap<>();
        compoundTag.getAllKeys().forEach(key -> map.put(Direction.valueOf(key), fromCompoundTagFunction.apply(compoundTag.get(key))));
        return map;
    }
}
