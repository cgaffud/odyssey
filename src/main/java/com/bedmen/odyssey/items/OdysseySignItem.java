package com.bedmen.odyssey.items;

import javax.annotation.Nullable;

import com.bedmen.odyssey.entity.ISignEditor;
import com.bedmen.odyssey.tileentity.OdysseySignTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OdysseySignItem extends WallOrFloorItem {
    public OdysseySignItem(Item.Properties p_i50038_1_, Block p_i50038_2_, Block p_i50038_3_) {
        super(p_i50038_2_, p_i50038_3_, p_i50038_1_);
    }

    protected boolean updateCustomBlockEntityTag(BlockPos p_195943_1_, World p_195943_2_, @Nullable PlayerEntity p_195943_3_, ItemStack p_195943_4_, BlockState p_195943_5_) {
        boolean flag = super.updateCustomBlockEntityTag(p_195943_1_, p_195943_2_, p_195943_3_, p_195943_4_, p_195943_5_);
        if (!p_195943_2_.isClientSide && !flag && p_195943_3_ != null) {
            if(p_195943_3_ instanceof ISignEditor){
                ((ISignEditor)p_195943_3_).openOdysseyTextEdit((OdysseySignTileEntity)p_195943_2_.getBlockEntity(p_195943_1_));
            }
        }

        return flag;
    }
}