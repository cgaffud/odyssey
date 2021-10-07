package com.bedmen.odyssey.blocks;

import com.bedmen.odyssey.entity.item.FallingHollowCoconutEntity;
import com.bedmen.odyssey.registry.ItemRegistry;
import com.bedmen.odyssey.tileentity.HollowCoconutTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class HollowCoconutBlock extends FallingBlock implements ITileEntityProvider {
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

    public void onPlace(BlockState p_220082_1_, World p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_, boolean p_220082_5_) {
        p_220082_2_.getBlockTicks().scheduleTick(p_220082_3_, this, this.getDelayAfterPlace());
    }

    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        p_196271_4_.getBlockTicks().scheduleTick(p_196271_5_, this, this.getDelayAfterPlace());
        return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    public void tick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        if(blockState.getValue(HANGING)){
            if (serverWorld.isEmptyBlock(blockPos.above()) || isFree(serverWorld.getBlockState(blockPos.above())) && blockPos.getY() >= 0) {
                FallingBlockEntity fallingblockentity = new FallingHollowCoconutEntity(serverWorld, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, serverWorld.getBlockState(blockPos).setValue(HANGING, Boolean.FALSE));
                this.falling(fallingblockentity);
                serverWorld.addFreshEntity(fallingblockentity);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
    }

    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState p_189876_1_, IBlockReader p_189876_2_, BlockPos p_189876_3_) {
        return 0x000000;
    }

    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return ItemRegistry.HOLLOW_COCONUT.get().getDefaultInstance();
    }
}
