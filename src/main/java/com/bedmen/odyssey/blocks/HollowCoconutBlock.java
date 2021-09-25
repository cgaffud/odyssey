package com.bedmen.odyssey.blocks;

import com.bedmen.odyssey.tileentity.BookshelfTileEntity;
import com.bedmen.odyssey.tileentity.HollowCoconutTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class HollowCoconutBlock extends Block implements ITileEntityProvider {
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final VoxelShape SHAPE_FLOOR = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
    public static final VoxelShape SHAPE_HANGING = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D);

    public HollowCoconutBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, Boolean.FALSE));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(HANGING, context.getClickedFace() == Direction.DOWN);
    }

    public VoxelShape getShape(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos, ISelectionContext selectionContext) {
        return blockState.getValue(HANGING) ? SHAPE_HANGING : SHAPE_FLOOR;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(HANGING);
    }

    @Nullable
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new HollowCoconutTileEntity();
    }
}
