package com.bedmen.odyssey.block.entity;

import com.bedmen.odyssey.Odyssey;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.UUID;

public class InfusionPedestalBlockEntity extends AbstractInfusionPedestalBlockEntity {

    protected int inUseTicks = 0;
    private static final String IN_USE_TICKS_TAG = Odyssey.MOD_ID + ":InUseTicks";
    public Optional<Direction> useDirection = Optional.empty();
    private static final String USE_DIRECTION_TAG = Odyssey.MOD_ID + ":UseDirection";

    public InfusionPedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BlockEntityTypeRegistry.INFUSION_PEDESTAL.get(), blockPos, blockState);
    }

    public InfusionPedestalBlockEntity(BlockEntityType<? extends InfusionPedestalBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        this.useDirection.ifPresent(direction -> compoundTag.putString(USE_DIRECTION_TAG, direction.name()));
    }

    protected void saveUpdateData(CompoundTag compoundTag){
        super.saveUpdateData(compoundTag);
        compoundTag.putInt(IN_USE_TICKS_TAG, this.inUseTicks);
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.inUseTicks = compoundTag.getInt(IN_USE_TICKS_TAG);
        if(compoundTag.contains(USE_DIRECTION_TAG)){
            this.useDirection = Optional.of(Direction.valueOf(compoundTag.getString(USE_DIRECTION_TAG)));
        } else {
            this.useDirection = Optional.empty();
        }
    }

    public void setInUseTicks(int inUseTicks){
        this.inUseTicks = inUseTicks;
        this.markUpdated();
    }

    // Use for InfuserBlockEntity.updateNewItemStacks code, since multiple infusers might try to get the itemStack even when one is already infusing with it
    public ItemStack getItemStackForInfuserBlockEntity(Direction direction){
        if(this.useDirection.isPresent() && this.useDirection.get() != direction){
            return ItemStack.EMPTY;
        }
        return this.getItemStackOriginal();
    }

    public float getItemRenderScale(){
        return 1.0f - ((float)this.inUseTicks / (float)InfuserBlockEntity.TOTAL_INFUSION_TIME);
    }
}
