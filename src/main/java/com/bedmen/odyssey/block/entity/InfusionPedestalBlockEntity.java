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
import net.minecraftforge.event.world.NoteBlockEvent;

import java.util.Optional;
import java.util.UUID;

public class InfusionPedestalBlockEntity extends BlockEntity {
    protected ItemStack itemStack = ItemStack.EMPTY;
    private static final String ITEM_STACK_TAG = Odyssey.MOD_ID + ":ItemStack";
    public Direction itemRenderDirection = Direction.NORTH;
    private static final String ITEM_RENDER_DIRECTION_TAG = Odyssey.MOD_ID + ":ItemRenderDirection";
    public UUID playerUUID = null;
    private static final String PLAYER_UUID_TAG = Odyssey.MOD_ID + ":PlayerUUID";

    public InfusionPedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BlockEntityTypeRegistry.INFUSION_PEDESTAL.get(), blockPos, blockState);
    }

    public InfusionPedestalBlockEntity(BlockEntityType<? extends InfusionPedestalBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        this.saveUpdateData(compoundTag);
        if(this.playerUUID != null){
            compoundTag.putUUID(PLAYER_UUID_TAG, this.playerUUID);
        }
    }

    protected void saveUpdateData(CompoundTag compoundTag){
        compoundTag.put(ITEM_STACK_TAG, this.itemStack.save(new CompoundTag()));
        compoundTag.put(ITEM_RENDER_DIRECTION_TAG, StringTag.valueOf(this.itemRenderDirection.name()));
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if(compoundTag.contains(ITEM_STACK_TAG)){
            this.itemStack = ItemStack.of(compoundTag.getCompound(ITEM_STACK_TAG));
        }
        if(compoundTag.contains(ITEM_RENDER_DIRECTION_TAG)){
            this.itemRenderDirection = Direction.valueOf(compoundTag.getString(ITEM_RENDER_DIRECTION_TAG));
        }
        if(compoundTag.contains(PLAYER_UUID_TAG)){
            this.playerUUID = compoundTag.getUUID(PLAYER_UUID_TAG);
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = new CompoundTag();
        saveUpdateData(compoundtag);
        return compoundtag;
    }

    public void markUpdated() {
        this.setChanged();
        Level level = this.getLevel();
        if(level != null){
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void setItemStack(ItemStack itemStack){
        this.itemStack = itemStack.copy();
    }

    // Use when giving away the itemStack to player
    public ItemStack getItemStackCopy(){
        return this.itemStack.copy();
    }

    // Use when a copy is not needed, like for renderering or just to check something about the itemStack
    public ItemStack getItemStackOriginal(){
        return this.itemStack;
    }

    protected Optional<Player> getPlayer(){
        if(this.level != null){
            Player player = this.level.getPlayerByUUID(this.playerUUID);
            if(player != null){
                return Optional.of(player);
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
