package com.bedmen.odyssey.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.IArmorVanishable;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class HollowCoconutBlock extends Block  implements IArmorVanishable {
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    protected static final VoxelShape SHAPE_FLOOR = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
    protected static final VoxelShape SHAPE_HANGING = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D);

    public HollowCoconutBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, Boolean.FALSE));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(HANGING, context.getClickedFace() == Direction.DOWN);
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return p_220053_1_.getValue(HANGING) ? SHAPE_HANGING : SHAPE_FLOOR;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(HANGING);
    }
}
