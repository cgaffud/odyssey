package com.bedmen.odyssey.blocks;

import java.util.Random;
import javax.annotation.Nullable;

import com.bedmen.odyssey.tileentity.BookshelfTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BookshelfBlock extends ContainerBlock {

    public BookshelfBlock() {
        super(AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(1.5F).sound(SoundType.WOOD));
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof BookshelfTileEntity) {
                player.openContainer((BookshelfTileEntity)tileentity);
            }

            return ActionResultType.CONSUME;
        }
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof BookshelfTileEntity) {
            ((BookshelfTileEntity)tileentity).bookShelfTick();
        }

    }

    @Nullable
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new BookshelfTileEntity();
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via IBlockState#getRenderType() whenever possible. Implementing/overriding is fine.
     */
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof BookshelfTileEntity) {
                ((BookshelfTileEntity)tileentity).setCustomName(stack.getDisplayName());
            }
        }

    }

    /**
     * @deprecated call via IBlockState#hasComparatorInputOverride() whenever possible. Implementing/overriding
     * is fine.
     */
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    /**
     * @deprecated call via IBlockState#getComparatorInputOverride(World,BlockPos) whenever possible.
     * Implementing/overriding is fine.
     */
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }
}
