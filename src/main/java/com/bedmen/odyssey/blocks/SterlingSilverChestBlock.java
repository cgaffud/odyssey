package com.bedmen.odyssey.blocks;

import com.bedmen.odyssey.registry.TileEntityTypeRegistry;
import com.bedmen.odyssey.tileentity.SterlingSilverChestTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.TrappedChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;

public class SterlingSilverChestBlock extends ChestBlock {
    public SterlingSilverChestBlock(AbstractBlock.Properties p_i48306_1_) {
        super(p_i48306_1_, TileEntityTypeRegistry.STERLING_SILVER_CHEST::get);
    }

    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new SterlingSilverChestTileEntity();
    }
}