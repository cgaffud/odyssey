package com.bedmen.odyssey.blocks.permafrost;

import com.bedmen.odyssey.util.BlockRegistry;
import net.minecraft.block.*;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class PermafrostConduitBlock extends Block{
    protected static final VoxelShape SHAPE = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);

    public PermafrostConduitBlock(AbstractBlock.Properties p_i48930_1_) {
        super(p_i48930_1_);
    }

    public void onPlace(BlockState blockState, World world, BlockPos pos, BlockState blockState2, boolean bool) {
        world.getBlockTicks().scheduleTick(pos, this, this.getDelayAfterPlace());
    }

    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        p_196271_4_.getBlockTicks().scheduleTick(p_196271_5_, this, this.getDelayAfterPlace());
        return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    protected int getDelayAfterPlace() {
        return 2;
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return false;
    }

    public void tick(BlockState blockState, ServerWorld serverWorld, BlockPos pos, Random random) {
        int flag = 6;
        if (serverWorld.getBlockState(pos.above()).getBlock() != BlockRegistry.PERMAFROST_ICE4.get()){
            flag--;
        }
        if (serverWorld.getBlockState(pos.below()).getBlock() != BlockRegistry.PERMAFROST_ICE4.get()){
            flag--;
        }
        if (serverWorld.getBlockState(pos.north()).getBlock() != BlockRegistry.PERMAFROST_ICE4.get()){
            flag--;
        }
        if (serverWorld.getBlockState(pos.east()).getBlock() != BlockRegistry.PERMAFROST_ICE4.get()){
            flag--;
        }
        if (serverWorld.getBlockState(pos.south()).getBlock() != BlockRegistry.PERMAFROST_ICE4.get()){
            flag--;
        }
        if (serverWorld.getBlockState(pos.west()).getBlock() != BlockRegistry.PERMAFROST_ICE4.get()){
            flag--;
        }

        if(flag <= 0){
            //PermafrostEntity permafrostEntity = EntityTypeRegistry.PERMAFROST.get().create(serverWorld);
            //permafrostEntity.moveTo((double)pos.getX() + 0.5D, (double)pos.getY() + 0.05D, (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
            //serverWorld.addFreshEntity(permafrostEntity);
            serverWorld.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
}