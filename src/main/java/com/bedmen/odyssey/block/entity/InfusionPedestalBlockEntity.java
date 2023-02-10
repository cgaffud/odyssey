package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class InfusionPedestalBlockEntity extends BlockEntity {
    public ItemStack itemStack = ItemStack.EMPTY;
    public Direction direction = Direction.NORTH;
    private static final String ITEM_STACK_TAG = Odyssey.MOD_ID + ":ItemStack";
    private static final String DIRECTION_TAG = Odyssey.MOD_ID + ":Direction";

    public InfusionPedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BlockEntityTypeRegistry.INFUSION_PEDESTAL.get(), blockPos, blockState);
    }

    public InfusionPedestalBlockEntity(BlockEntityType<? extends InfusionPedestalBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if(compoundTag.contains(ITEM_STACK_TAG)){
            this.itemStack = ItemStack.of(compoundTag.getCompound(ITEM_STACK_TAG));
        }
        if(compoundTag.contains(DIRECTION_TAG)){
            this.direction = Direction.valueOf(compoundTag.getString(DIRECTION_TAG));
        }
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        CompoundTag itemStackTag = new CompoundTag();
        this.itemStack.save(itemStackTag);
        compoundTag.put(ITEM_STACK_TAG, itemStackTag);
        compoundTag.put(DIRECTION_TAG, StringTag.valueOf(this.direction.name()));
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        saveAdditional(compoundtag);
        return compoundtag;
    }

    public void markUpdated() {
        this.setChanged();
        Level level = this.getLevel();
        if(level != null){
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }
}
