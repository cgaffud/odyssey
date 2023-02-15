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
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InfuserBlockEntity extends InfusionPedestalBlockEntity {

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    private static final Class<?>[] DIGGER_CLASSES = new Class<?>[]{PickaxeItem.class, AxeItem.class, HoeItem.class, ShovelItem.class};
    private static final int DISTANCE_TO_PEDESTALS = 3;
    private static final double MAX_PLAYER_DISTANCE = 10.0d;
    public static final int TOTAL_INFUSION_TIME = 60;

    protected ItemStack oldItemStack = ItemStack.EMPTY;
    private static final String OLD_ITEM_STACK_TAG = Odyssey.MOD_ID + ":OldItemStack";
    protected Map<Direction, ItemStack> oldPedestalItemStackMap = new HashMap<>();
    private static final String OLD_PEDESTAL_ITEM_STACKS_TAG = Odyssey.MOD_ID + ":OldPedestalItemStacks";
    protected Map<Direction, ItemStack> newPedestalItemStackMap = new HashMap<>();
    private static final String NEW_PEDESTAL_ITEM_STACKS_TAG = Odyssey.MOD_ID + ":NewPedestalItemStacks";
    public int infuserCraftingTicks = 0;
    private static final String INFUSER_CRAFTING_TICKS_TAG = Odyssey.MOD_ID + ":InfuserCraftingTicks";
    protected Map<Direction, Integer> infusingTicksMap = new HashMap<>();
    private static final String INFUSING_TICKS_MAP_TAG = Odyssey.MOD_ID + ":InfusingTicksMap";
    protected Set<Direction> pedestalsInUseSet = new HashSet<>();
    private static final String PEDESTALS_IN_USE_SET_TAG = Odyssey.MOD_ID + ":PedestalsInUseSet";
    public List<PathParticle> pathParticleList = new ArrayList<>();
    private static final String PATH_PARTICLE_LIST_TAG = Odyssey.MOD_ID + ":PathParticleList";

    public InfuserBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.INFUSER.get(), blockPos, blockState);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, InfuserBlockEntity infuserBlockEntity) {
        infuserBlockEntity.updatePathParticles();
        if(infuserBlockEntity.infuserCraftingTicks > 0 && infuserBlockEntity.infuserCraftingTicks < TOTAL_INFUSION_TIME){
            infuserBlockEntity.infuserCraftingTicks++;
        }
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, InfuserBlockEntity infuserBlockEntity) {
        infuserBlockEntity.updateNewPedestalItemStacks();

        Optional<Pair<InfuserCraftingRecipe, Set<Direction>>> optionalPair = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.INFUSER_CRAFTING.get())
                .stream()
                .map(infuserCraftingRecipe -> Pair.of(infuserCraftingRecipe, infuserCraftingRecipe.matches(infuserBlockEntity.getItemStackOriginal(), infuserBlockEntity.newPedestalItemStackMap)))
                .filter(pair -> pair.getSecond().isPresent())
                .map(pair -> Pair.of(pair.getFirst(), pair.getSecond().get()))
                .findFirst();

        optionalPair.ifPresentOrElse(pair -> infuserBlockEntity.tryInfuserCrafting(pair.getFirst(), pair.getSecond()), infuserBlockEntity::tryInfusion);
        infuserBlockEntity.updateOldPedestalItemStacks();
        infuserBlockEntity.updatePathParticles();
    }

    private void tryInfuserCrafting(InfuserCraftingRecipe infuserCraftingRecipe, Set<Direction> pedestalsToUseSet){
        int count = this.getMinimumCountOfInputItemStacks();
        if(!this.isInfuserCrafting()){
            ExperienceCost experienceCost = infuserCraftingRecipe.experienceCost.multiplyCost(count);
            Optional<ServerPlayer> payer = this.tryToPayExperienceCost(experienceCost);
            payer.ifPresent(serverPlayer -> {
                this.infuserCraftingTicks++;
                this.stopAllInfusing();
                this.pedestalsInUseSet = pedestalsToUseSet;
                this.forEveryPedestalInUse((direction, infusionPedestalBlockEntity) -> {
                    infusionPedestalBlockEntity.useDirection = Optional.of(direction);
                    infusionPedestalBlockEntity.setInUseTicks(this.infuserCraftingTicks);
                });
                this.pedestalsInUseSet.forEach(direction -> {
                    for(float delay = 0.0f; delay <= 1.0f; delay += 0.1f){
                        this.pathParticleList.add(new PathParticle(serverPlayer.position(), this.getBlockPos(), direction, delay));
                    }
                });
                this.markUpdated();
            });
        } else if(this.infuserCraftingTicks < TOTAL_INFUSION_TIME){
            this.infuserCraftingTicks++;
            this.forEveryPedestalInUse((direction, infusionPedestalBlockEntity) -> infusionPedestalBlockEntity.setInUseTicks(this.infuserCraftingTicks));
        } else {
            this.reduceItemStackCountOnInUsePedestals(count);
            this.setItemStack(infuserCraftingRecipe.getResultItemWithOldItemStackData(this.getItemStackOriginal()));
            this.setItemStackCount(count);
            this.stopInfuserCrafting();
        }
    }

    private void forEveryPedestalInUse(BiConsumer<Direction, InfusionPedestalBlockEntity> biConsumer){
        this.pedestalsInUseSet.forEach(direction -> this.getInfusionPedestalBlockEntity(direction).ifPresent(infusionPedestalBlockEntity -> biConsumer.accept(direction, infusionPedestalBlockEntity)));
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

    private void reduceItemStackCountOnInUsePedestals(int reductionCount){
        this.forEveryPedestalInUse((direction, infusionPedestalBlockEntity) -> infusionPedestalBlockEntity.shrinkItemStack(reductionCount));
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
        if(this.isInfuserCrafting()){
            this.stopInfuserCrafting();
        }
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
                            Optional<ServerPlayer> payer = this.tryToPayExperienceCost(experienceCost);
                            if(payer.isPresent()){
                                this.incrementInfusingTick(direction);
                                this.pedestalsInUseSet.add(direction);
                                this.getInfusionPedestalBlockEntity(direction).ifPresent(infusionPedestalBlockEntity -> {
                                    infusionPedestalBlockEntity.useDirection = Optional.of(direction);
                                    infusionPedestalBlockEntity.setInUseTicks(this.infusingTicksMap.get(direction));
                                });
                                return; // Avoids stopInfusing call below
                            }
                        } else if(this.isInfusing(direction) && this.infusingTicksMap.get(direction) < TOTAL_INFUSION_TIME){
                            this.incrementInfusingTick(direction);
                            this.getInfusionPedestalBlockEntity(direction).ifPresent(infusionPedestalBlockEntity -> infusionPedestalBlockEntity.setInUseTicks(this.infusingTicksMap.get(direction)));
                            return; // Avoids stopInfusing call below
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
            // Resets infusing ticker in case of one of the checks failing, or if we finish infusing
            this.stopInfusing(direction);
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

    private Optional<ServerPlayer> tryToPayExperienceCost(ExperienceCost experienceCost){
        Set<ServerPlayer> serverPlayerSet = this.getNearbyPlayersWhoMadeChanges();
        Set<ServerPlayer> serverPlayersWhoCanPay = serverPlayerSet.stream().filter(experienceCost::canPay).collect(Collectors.toSet());
        if(serverPlayersWhoCanPay.isEmpty()){
            serverPlayerSet.forEach(experienceCost::displayRequirementMessage);
            return Optional.empty();
        } else {
            ServerPlayer serverPlayer = serverPlayersWhoCanPay.stream().findFirst().get();
            experienceCost.pay(serverPlayer);
            return Optional.of(serverPlayer);
        }
    }

    private void updateNewPedestalItemStacks(){
        for(Direction direction: HORIZONTALS){
            this.getInfusionPedestalBlockEntity(direction)
                    .ifPresentOrElse(
                            infusionPedestalBlockEntity -> this.newPedestalItemStackMap.put(direction, infusionPedestalBlockEntity.getItemStackForInfuserBlockEntity(direction)),
                            () -> this.newPedestalItemStackMap.put(direction, ItemStack.EMPTY)
                    );
        }
    }

    private void updateOldPedestalItemStacks(){
        this.oldItemStack = this.getItemStackOriginal();
        this.oldPedestalItemStackMap.clear();
        this.oldPedestalItemStackMap.putAll(this.newPedestalItemStackMap);
    }

    private void updatePathParticles(){
        this.pathParticleList.forEach(pathParticle -> pathParticle.updatePosition((float)this.infuserCraftingTicks / (float)TOTAL_INFUSION_TIME));
    }

    private static boolean sameItemStack(ItemStack itemStack1, ItemStack itemStack2){
        return ItemStack.isSameItemSameTags(itemStack1, itemStack2) && itemStack1.getCount() == itemStack2.getCount();
    }

    private void incrementInfusingTick(Direction direction){
        Integer integer = this.infusingTicksMap.get(direction);
        int i = integer == null ? 0 : integer;
        this.infusingTicksMap.put(direction, i+1);
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
        this.forEveryPedestalInUse((direction, infusionPedestalBlockEntity) -> this.stopUsingInfusionPedestal(direction));
        this.pedestalsInUseSet.clear();
        this.pathParticleList.clear();
    }

    private void stopInfusing(Direction direction){
        this.infusingTicksMap.put(direction, 0);
        if(this.pedestalsInUseSet.contains(direction)){
            this.stopUsingInfusionPedestal(direction);
        }
    }

    private void stopUsingInfusionPedestal(Direction direction){
        this.getInfusionPedestalBlockEntity(direction).ifPresent(infusionPedestalBlockEntity -> {
            infusionPedestalBlockEntity.setInUseTicks(0);
            infusionPedestalBlockEntity.useDirection = Optional.empty();
        });
    }

    private void stopAllInfusing(){
        this.infusingTicksMap.keySet().forEach(this::stopInfusing);
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.put(OLD_ITEM_STACK_TAG, this.oldItemStack.save(new CompoundTag()));
        compoundTag.put(OLD_PEDESTAL_ITEM_STACKS_TAG, itemStackMapToTag(this.oldPedestalItemStackMap));
        compoundTag.put(NEW_PEDESTAL_ITEM_STACKS_TAG, itemStackMapToTag(this.newPedestalItemStackMap));
        compoundTag.put(INFUSING_TICKS_MAP_TAG, ticksMapToTag(this.infusingTicksMap));
        compoundTag.put(PEDESTALS_IN_USE_SET_TAG, directionSetToTag(this.pedestalsInUseSet));
    }

    protected void saveUpdateData(CompoundTag compoundTag){
        super.saveUpdateData(compoundTag);
        compoundTag.putInt(INFUSER_CRAFTING_TICKS_TAG, this.infuserCraftingTicks);
        ListTag pathParticleListTag = new ListTag();
        this.pathParticleList.forEach(pathParticle -> pathParticleListTag.add(pathParticle.toCompoundTag()));
        compoundTag.put(PATH_PARTICLE_LIST_TAG, pathParticleListTag);
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
        if(compoundTag.contains(PEDESTALS_IN_USE_SET_TAG)){
            this.pedestalsInUseSet = compoundTag.getList(PEDESTALS_IN_USE_SET_TAG, Tag.TAG_STRING).stream().map(tag -> Direction.valueOf(tag.getAsString())).collect(Collectors.toSet());
        }
        if(compoundTag.contains(PATH_PARTICLE_LIST_TAG)){
            this.pathParticleList = compoundTag.getList(PATH_PARTICLE_LIST_TAG, Tag.TAG_COMPOUND).stream().map(tag -> PathParticle.fromCompoundTag((CompoundTag)tag)).collect(Collectors.toList());
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

    protected static ListTag directionSetToTag(Set<Direction> set){
        ListTag listTag = new ListTag();
        set.forEach(direction -> listTag.add(StringTag.valueOf(direction.name())));
        return listTag;
    }

    public static class PathParticle{

        // All positions are relative to the Infuser's blockPos
        private final float delay;
        private final Vec3 initialPosition;
        private final Vec3 halfWayPosition;
        private static final Vec3 FINAL_POSITION = new Vec3(0.5d, 15.0d/16.0d, 0.5d);
        private Vec3 positionO;
        private Vec3 position;

        private PathParticle(Vec3 playerPosition, BlockPos blockPos, Direction direction, float delay){
            this.delay = delay;
            this.initialPosition = playerPosition.subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            this.halfWayPosition = FINAL_POSITION.add(direction.getStepX() * DISTANCE_TO_PEDESTALS, 0, direction.getStepZ() * DISTANCE_TO_PEDESTALS);
            this.positionO = this.initialPosition;
            this.position = this.initialPosition;
        }

        private PathParticle(float delay, Vec3 initialPosition, Vec3 halfWayPosition, Vec3 positionO, Vec3 position){
            this.delay = delay;
            this.initialPosition = initialPosition;
            this.halfWayPosition = halfWayPosition;
            this.positionO = positionO;
            this.position = position;
        }

        private void updatePosition(float completion){
            this.positionO = this.position;
            this.position = this.getPositionFromCompletion(completion);
        }

        private Vec3 getPositionFromCompletion(float completion){
            float completionInSeconds = completion * TOTAL_INFUSION_TIME / 20.0f;
            if(completionInSeconds <= this.delay){
                return this.initialPosition;
            } else if(completionInSeconds <= this.delay + 1.0f){
                float firstHalfCompletion = completionInSeconds - this.delay;
                return this.initialPosition.lerp(this.halfWayPosition, firstHalfCompletion);
            } else if(completionInSeconds <= this.delay + 2.0f){
                float secondHalfCompletion = completionInSeconds - this.delay - 1.0f;
                return this.halfWayPosition.lerp(FINAL_POSITION, secondHalfCompletion);
            } else {
                return FINAL_POSITION;
            }
        }

        public Vec3 getPosition(float partialTicks){
            return this.positionO.lerp(this.position, partialTicks);
        }

        private CompoundTag toCompoundTag(){
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putFloat("delay", this.delay);
            compoundTag.put("initialPosition", vec3ToListTag(this.initialPosition));
            compoundTag.put("halfWayPosition", vec3ToListTag(this.halfWayPosition));
            compoundTag.put("positionO", vec3ToListTag(this.positionO));
            compoundTag.put("position", vec3ToListTag(this.position));
            return compoundTag;
        }

        private static PathParticle fromCompoundTag(CompoundTag compoundTag){
            float delay = compoundTag.getFloat("delay");
            Vec3 initialPosition = listTagToVec3(compoundTag.getList("initialPosition", Tag.TAG_DOUBLE));
            Vec3 halfWayPosition = listTagToVec3(compoundTag.getList("halfWayPosition", Tag.TAG_DOUBLE));
            Vec3 positionO = listTagToVec3(compoundTag.getList("positionO", Tag.TAG_DOUBLE));
            Vec3 position = listTagToVec3(compoundTag.getList("position", Tag.TAG_DOUBLE));
            return new PathParticle(delay, initialPosition, halfWayPosition, positionO, position);
        }

        private static ListTag vec3ToListTag(Vec3 vec3) {
            ListTag listtag = new ListTag();
            listtag.add(DoubleTag.valueOf(vec3.x));
            listtag.add(DoubleTag.valueOf(vec3.y));
            listtag.add(DoubleTag.valueOf(vec3.z));
            return listtag;
        }

        private static Vec3 listTagToVec3(ListTag listTag) {
            return new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
        }
    }
}
