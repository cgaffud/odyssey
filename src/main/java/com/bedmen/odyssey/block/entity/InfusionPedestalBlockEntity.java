package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InfusionPedestalBlockEntity extends BlockEntity {
    public ItemStack itemStack = ItemStack.EMPTY;
    private static final String ITEM_STACK_TAG = Odyssey.MOD_ID + ":ItemStack";

    public InfusionPedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityTypeRegistry.INFUSION_PEDESTAL.get(), blockPos, blockState);
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if(compoundTag.contains(ITEM_STACK_TAG)){
            this.itemStack = ItemStack.of(compoundTag.getCompound(ITEM_STACK_TAG));
        } else {
            this.itemStack = ItemStack.EMPTY;
        }
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        CompoundTag itemStackTag = new CompoundTag();
        this.itemStack.save(itemStackTag);
        compoundTag.put(ITEM_STACK_TAG, itemStackTag);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        CompoundTag itemStackTag = new CompoundTag();
        this.itemStack.save(itemStackTag);
        compoundtag.put(ITEM_STACK_TAG, itemStackTag);
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
