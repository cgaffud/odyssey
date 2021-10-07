package com.bedmen.odyssey.blocks;

import java.util.Random;
import javax.annotation.Nullable;

import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CoconutBlock extends Block implements IGrowable {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    protected static final VoxelShape[] AABB = new VoxelShape[]{Block.box(6.0D, 10.0D, 6.0D, 10.0D, 14.0D, 10.0D), Block.box(4.0D, 6.0D, 4.0D, 12.0D, 14.0D, 12.0D), Block.box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D)};

    public CoconutBlock(AbstractBlock.Properties p_i48426_1_) {
        super(p_i48426_1_);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        ItemStack itemstack = p_225533_4_.getItemInHand(p_225533_5_);
        if (itemstack.getItem() == Items.SHEARS) {
            if (!p_225533_2_.isClientSide) {
                p_225533_2_.playSound((PlayerEntity)null, p_225533_3_, SoundEvents.PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                p_225533_2_.setBlock(p_225533_3_, BlockRegistry.HOLLOW_COCONUT.get().defaultBlockState().setValue(HollowCoconutBlock.HANGING, Boolean.TRUE), 11);
                Direction direction1 = Direction.DOWN;
                ItemEntity itementity = new ItemEntity(p_225533_2_, (double)p_225533_3_.getX() + 0.5D + (double)direction1.getStepX() * 0.65D, (double)p_225533_3_.getY() + 0.1D, (double)p_225533_3_.getZ() + 0.5D + (double)direction1.getStepZ() * 0.65D, new ItemStack(ItemRegistry.COCONUT_FLOWER.get(), 2));
                itementity.setDeltaMovement(0.05D * (double)direction1.getStepX() + p_225533_2_.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction1.getStepZ() + p_225533_2_.random.nextDouble() * 0.02D);
                p_225533_2_.addFreshEntity(itementity);
                itemstack.hurtAndBreak(1, p_225533_4_, (p_220282_1_) -> {
                    p_220282_1_.broadcastBreakEvent(p_225533_5_);
                });
            }
            return ActionResultType.sidedSuccess(p_225533_2_.isClientSide);
        } else {
            return super.use(p_225533_1_, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
        }
    }

    public boolean isRandomlyTicking(BlockState p_149653_1_) {
        return p_149653_1_.getValue(AGE) < 2;
    }

    public void randomTick(BlockState p_225542_1_, ServerWorld p_225542_2_, BlockPos p_225542_3_, Random p_225542_4_) {
        if (true) {
            int i = p_225542_1_.getValue(AGE);
            if (i < 2 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(p_225542_2_, p_225542_3_, p_225542_1_, p_225542_2_.random.nextInt(5) == 0)) {
                p_225542_2_.setBlock(p_225542_3_, p_225542_1_.setValue(AGE, i + 1), 2);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(p_225542_2_, p_225542_3_, p_225542_1_);
            }
        }

    }

    public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
        return p_196260_2_.getBlockState(p_196260_3_.above()).getBlock() == BlockRegistry.PALM_LEAVES.get();
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return AABB[p_220053_1_.getValue(AGE)];
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        BlockState blockstate = this.defaultBlockState();
        IWorldReader iworldreader = p_196258_1_.getLevel();
        BlockPos blockpos = p_196258_1_.getClickedPos();

        if (blockstate.canSurvive(iworldreader, blockpos)) {
            return blockstate;
        }

        return null;
    }

    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return p_196271_2_ == Direction.UP && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    public boolean isValidBonemealTarget(IBlockReader p_176473_1_, BlockPos p_176473_2_, BlockState p_176473_3_, boolean p_176473_4_) {
        return p_176473_3_.getValue(AGE) < 2;
    }

    public boolean isBonemealSuccess(World p_180670_1_, Random p_180670_2_, BlockPos p_180670_3_, BlockState p_180670_4_) {
        return true;
    }

    public void performBonemeal(ServerWorld p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_, BlockState p_225535_4_) {
        p_225535_1_.setBlock(p_225535_3_, p_225535_4_.setValue(AGE, Integer.valueOf(p_225535_4_.getValue(AGE) + 1)), 2);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(AGE);
    }

    public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return false;
    }
}
