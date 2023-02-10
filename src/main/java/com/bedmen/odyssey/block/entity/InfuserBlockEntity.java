package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class InfuserBlockEntity extends InfusionPedestalBlockEntity {

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    private static final int DISTANCE_TO_PEDESTALS = 3;

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
        // Update new itemStacks
        for(Direction direction: HORIZONTALS){
            BlockPos pedestalBlockPos = blockPos.relative(direction, DISTANCE_TO_PEDESTALS);
            BlockEntity blockEntity = level.getBlockEntity(pedestalBlockPos);
            if(blockEntity instanceof InfusionPedestalBlockEntity infusionPedestalBlockEntity && !(blockEntity instanceof InfuserBlockEntity)){
                infuserBlockEntity.newPedestalItemStackMap.put(direction, infusionPedestalBlockEntity.itemStack.copy());
            }
        }


        // Update old itemStacks
        infuserBlockEntity.oldItemStack = infuserBlockEntity.itemStack.copy();
        infuserBlockEntity.oldPedestalItemStackMap.clear();
        infuserBlockEntity.oldPedestalItemStackMap.putAll(infuserBlockEntity.newPedestalItemStackMap);
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
