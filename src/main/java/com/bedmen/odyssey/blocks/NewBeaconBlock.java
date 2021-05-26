package com.bedmen.odyssey.blocks;

import com.bedmen.odyssey.tileentity.NewBeaconTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class NewBeaconBlock extends ContainerBlock implements IBeaconBeamColorProvider {

    public NewBeaconBlock() {

        super(AbstractBlock.Properties.of(Material.GLASS, MaterialColor.DIAMOND).strength(3.0F).lightLevel((state) -> { return 15; }).noOcclusion().isRedstoneConductor((p_test_1_, p_test_2_, p_test_3_) -> {return false;}));
    }

    public DyeColor getColor() {
        return DyeColor.WHITE;
    }

    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new NewBeaconTileEntity();
    }

    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof NewBeaconTileEntity) {
                player.openMenu((NewBeaconTileEntity)tileentity);
                player.awardStat(Stats.INTERACT_WITH_BEACON);
            }

            return ActionResultType.CONSUME;
        }
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof NewBeaconTileEntity) {
                ((NewBeaconTileEntity)tileentity).setCustomName(stack.getHoverName());
            }
        }

    }
}
