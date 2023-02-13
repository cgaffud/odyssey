package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.recipes.InfuserCraftingRecipe;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

public class InfuserBlockEntity extends InfusionPedestalBlockEntity {

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    private static final int DISTANCE_TO_PEDESTALS = 3;
    public static final float STRENGTH_PENALTY = 0.5f;

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
                .stream().filter(infuserCraftingRecipe -> infuserCraftingRecipe.matches(infuserBlockEntity.itemStack, infuserBlockEntity.newPedestalItemStackMap.values())).findFirst();
        optionalInfuserCraftingRecipe.ifPresent(infuserCraftingRecipe -> {
            int count = infuserBlockEntity.getMinimumCountOfInputItemStacks();
            infuserBlockEntity.reduceItemStackCountOnAllInfusionPedestals(count);
            infuserBlockEntity.itemStack = optionalInfuserCraftingRecipe.get().getResultItemWithOldItemStackData(infuserBlockEntity.itemStack);
            infuserBlockEntity.itemStack.setCount(count);
            infuserBlockEntity.markUpdated();
        });

//        if(infuserBlockEntity.inValidConfiguration()){
//            List<Player> playerList = infuserBlockEntity.getPlayersWhoMadeChanges();
//        }

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
                infusionPedestalBlockEntity.itemStack.shrink(reductionCount);
                infusionPedestalBlockEntity.markUpdated();
            });
        }
        this.itemStack.shrink(reductionCount);
        if(!this.itemStack.isEmpty() && this.level != null){
            Containers.dropItemStack(this.level, this.getBlockPos().getX(), this.getBlockPos().getY()+1.0d, this.getBlockPos().getZ(), this.getItemStackCopy());
        }
    }
    
    private int getMinimumCountOfInputItemStacks(){
        int minimumCount = this.itemStack.getCount();
        for(Direction direction: HORIZONTALS){
            Optional<InfusionPedestalBlockEntity> optionalInfusionPedestalBlockEntity = this.getInfusionPedestalBlockEntity(direction);
            if(optionalInfusionPedestalBlockEntity.isPresent()) {
                ItemStack pedestalItemStack = optionalInfusionPedestalBlockEntity.get().itemStack;
                if(!pedestalItemStack.isEmpty() && pedestalItemStack.getCount() < minimumCount){
                    minimumCount = pedestalItemStack.getCount();;
                }
            }
        }
        return minimumCount;
    }

    private boolean inValidConfiguration(){
        return true;
    }

    private List<Player> getPlayersWhoMadeChanges(){
        List<Player> playerList = new ArrayList<>();
        Optional<Player> optionalPlayer = this.getPlayer();
        if(this.itemStack != this.oldItemStack && optionalPlayer.isPresent()){
            playerList.add(optionalPlayer.get());
        }
        playerList.addAll(Arrays.stream(HORIZONTALS)
                .filter(direction -> this.newPedestalItemStackMap.get(direction) != this.oldPedestalItemStackMap.get(direction))
                .map(this::getPlayerFromInfusionPedestal)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
        return playerList;
    }

    private void updateNewItemStacks(){
        for(Direction direction: HORIZONTALS){
            this.getInfusionPedestalBlockEntity(direction)
                    .ifPresentOrElse(
                            infusionPedestalBlockEntity -> this.newPedestalItemStackMap.put(direction, infusionPedestalBlockEntity.itemStack.copy()),
                            () -> this.newPedestalItemStackMap.put(direction, ItemStack.EMPTY)
                    );
        }
    }

    private void updateOldItemStacks(){
        this.oldItemStack = this.itemStack;
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
