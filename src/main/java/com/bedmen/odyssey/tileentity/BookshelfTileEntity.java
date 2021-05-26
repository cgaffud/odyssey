package com.bedmen.odyssey.tileentity;

import com.bedmen.odyssey.container.BookshelfContainer;
import com.bedmen.odyssey.util.BlockRegistry;
import com.bedmen.odyssey.util.TileEntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BookshelfTileEntity extends LockableLootTileEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private int numPlayersUsing;

    private BookshelfTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public BookshelfTileEntity() {
        this(TileEntityTypeRegistry.BOOKSHELF.get());
    }

    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (!this.trySaveLootTable(compound)) {
            ItemStackHelper.saveAllItems(compound, this.items);
        }

        return compound;
    }

    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.items);
        }

    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getContainerSize() {
        return 3;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.oddc.bookshelf");
    }

    protected Container createMenu(int id, PlayerInventory player) {
        return new BookshelfContainer(id, player, this);
    }

    public void startOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;

            this.scheduleTick();
        }

    }

    private void scheduleTick() {
        this.level.getBlockTicks().scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 5);
    }

    public void bookShelfTick() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        this.numPlayersUsing = ChestTileEntity.getOpenCount(this.level, this, i, j, k);
        if (this.numPlayersUsing > 0) {
            this.scheduleTick();
        } else {
            BlockState blockstate = this.getBlockState();
            if (!blockstate.is(BlockRegistry.BOOKSHELF.get())) {
                this.setRemoved();
                return;
            }
        }

    }

    public void stopOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
        }

    }
}
