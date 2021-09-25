package com.bedmen.odyssey.tileentity;

import com.bedmen.odyssey.blocks.HollowCoconutBlock;
import com.bedmen.odyssey.container.BookshelfContainer;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.TileEntityTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class HollowCoconutTileEntity extends TileEntity implements ITickableTileEntity {

    private HollowCoconutTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public HollowCoconutTileEntity() {
        this(TileEntityTypeRegistry.HOLLOW_COCONUT.get());
    }

    public void tick(){
        BlockState blockState = this.getBlockState();
        BlockPos blockPos = this.getBlockPos();
        if(blockState.getValue(HollowCoconutBlock.HANGING)){
            AxisAlignedBB axisAlignedBB = HollowCoconutBlock.SHAPE_HANGING.bounds().move(blockPos).move(0.0d,-3.0d, 0.0d).inflate(0.0d, 2.0d, 0.0d);
            List<PlayerEntity> playerEntityList = this.level.getEntitiesOfClass(PlayerEntity.class, axisAlignedBB);
            if(!playerEntityList.isEmpty()){
                FallingBlockEntity fallingblockentity = new FallingBlockEntity(this.level, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, this.level.getBlockState(blockPos));
                this.level.addFreshEntity(fallingblockentity);
            }
        }
    }

}
