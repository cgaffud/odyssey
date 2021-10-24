package com.bedmen.odyssey.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class FogBlock extends AbstractGlassBlock implements INeedsToRegisterRenderType {

    public FogBlock() {
        super(AbstractBlock.Properties.of(Material.AIR).noCollission().noDrops());
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    public RenderType getRenderType() {
        return RenderType.translucent();
    }
}