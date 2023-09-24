package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GraveBlockEntity extends RandomizableContainerBlockEntity {

    private final int CONTAINER_SIZE = 41;
    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);


    public GraveBlockEntity(BlockEntityType<?> p_155076_, BlockPos p_155077_, BlockState p_155078_) {
        super(p_155076_, p_155077_, p_155078_);
    }

    public GraveBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.GRAVE.get(), blockPos, blockState);
    }

//    public GraveBlockEntity(BlockPos p_155331_, BlockState p_155332_) {
//        this(BlockEntityType, p_155331_, p_155332_);
//    }


    @Override
    protected Component getDefaultName() { return Component.translatable("container.grave"); }

    @Override
    protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) { return null; }

    @Override
    protected NonNullList<ItemStack> getItems() { return this.items; }

    @Override
    public void setItems(NonNullList<ItemStack> items) { this.items = items;}

    @Override
    public int getContainerSize() { return CONTAINER_SIZE; }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compoundTag)) {
            ContainerHelper.loadAllItems(compoundTag, this.items);
        }

    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!this.trySaveLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, this.items);
        }

    }
}
