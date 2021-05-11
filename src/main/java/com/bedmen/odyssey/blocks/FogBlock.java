package com.bedmen.odyssey.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class FogBlock extends AbstractGlassBlock {
    public static final IntegerProperty ALPHA = BlockStateProperties.LEVEL_1_8;

    public FogBlock() {
        super(AbstractBlock.Properties.create(Material.AIR).doesNotBlockMovement().setAir().noDrops());
        this.setDefaultState(this.stateContainer.getBaseState().with(ALPHA, Integer.valueOf(1)));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ALPHA);
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }
}