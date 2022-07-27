package com.bedmen.odyssey.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TripleHighBlockItem extends DoubleHighBlockItem {
    public TripleHighBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    protected boolean placeBlock(BlockPlaceContext context, BlockState blockState) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos().above(2);
        BlockState blockstate = level.isWaterAt(blockpos) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
        level.setBlock(blockpos, blockstate, 27);
        return super.placeBlock(context, blockState);
    }
}