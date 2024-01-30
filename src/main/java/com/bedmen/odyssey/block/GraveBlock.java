package com.bedmen.odyssey.block;

import com.bedmen.odyssey.block.entity.GraveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GraveBlock extends BaseEntityBlock {
    // The thing gotta be bush-like
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public GraveBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GraveBlockEntity(blockPos, blockState);
    }

    public void onRemove(BlockState p_51538_, Level p_51539_, BlockPos p_51540_, BlockState p_51541_, boolean p_51542_) {
        if (!p_51538_.is(p_51541_.getBlock())) {
            BlockEntity blockentity = p_51539_.getBlockEntity(p_51540_);
            if (blockentity instanceof Container) {
                Containers.dropContents(p_51539_, p_51540_, (Container)blockentity);
                p_51539_.updateNeighbourForOutputSignal(p_51540_, this);
            }

            super.onRemove(p_51538_, p_51539_, p_51540_, p_51541_, p_51542_);
        }
    }

    public VoxelShape getShape(BlockState p_57336_, BlockGetter p_57337_, BlockPos p_57338_, CollisionContext p_57339_) {
        return SHAPE;
    }

    public RenderShape getRenderShape(BlockState p_154477_) {
        return RenderShape.MODEL;
    }

}
