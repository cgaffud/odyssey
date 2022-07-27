package com.bedmen.odyssey.block;

import com.bedmen.odyssey.registry.BlockRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class CoconutBlock extends Block implements BonemealableBlock, INeedsToRegisterRenderType {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    protected static final VoxelShape[] AABB = new VoxelShape[]{Block.box(6.0D, 10.0D, 6.0D, 10.0D, 14.0D, 10.0D), Block.box(4.0D, 6.0D, 4.0D, 12.0D, 14.0D, 12.0D), Block.box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D)};

    public CoconutBlock(Properties p_i48426_1_) {
        super(p_i48426_1_);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (itemstack.getItem() == Items.SHEARS && blockState.getValue(AGE) == 2) {
            if (!level.isClientSide) {
                level.playSound((Player)null, blockPos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(blockPos, BlockRegistry.HOLLOW_COCONUT.get().defaultBlockState().setValue(HollowCoconutBlock.HANGING, Boolean.TRUE), 11);
                Direction direction1 = Direction.DOWN;
                ItemEntity itementity = new ItemEntity(level, (double)blockPos.getX() + 0.5D + (double)direction1.getStepX() * 0.65D, (double)blockPos.getY() + 0.1D, (double)blockPos.getZ() + 0.5D + (double)direction1.getStepZ() * 0.65D, new ItemStack(ItemRegistry.COCONUT_FLOWER.get(), 2));
                itementity.setDeltaMovement(0.05D * (double)direction1.getStepX() + level.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction1.getStepZ() + level.random.nextDouble() * 0.02D);
                level.addFreshEntity(itementity);
                itemstack.hurtAndBreak(1, player, (p_220282_1_) -> {
                    p_220282_1_.broadcastBreakEvent(interactionHand);
                });
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
        }
    }

    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(AGE) < 2;
    }

    public void randomTick(BlockState p_51782_, ServerLevel p_51783_, BlockPos p_51784_, Random p_51785_) {
        int i = p_51782_.getValue(AGE);
        if (i < 2 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(p_51783_, p_51784_, p_51782_, p_51783_.random.nextInt(5) == 0)) {
            p_51783_.setBlock(p_51784_, p_51782_.setValue(AGE, i + 1), 2);
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(p_51783_, p_51784_, p_51782_);
        }
    }

    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        return p_196260_2_.getBlockState(p_196260_3_.above()).getBlock() == BlockRegistry.PALM_LEAVES.get();
    }

    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return AABB[p_220053_1_.getValue(AGE)];
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        BlockState blockstate = this.defaultBlockState();
        LevelReader iworldreader = p_196258_1_.getLevel();
        BlockPos blockpos = p_196258_1_.getClickedPos();

        if (blockstate.canSurvive(iworldreader, blockpos)) {
            return blockstate;
        }

        return null;
    }

    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return p_196271_2_ == Direction.UP && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    public boolean isValidBonemealTarget(BlockGetter p_176473_1_, BlockPos p_176473_2_, BlockState p_176473_3_, boolean p_176473_4_) {
        return p_176473_3_.getValue(AGE) < 2;
    }

    public boolean isBonemealSuccess(Level p_180670_1_, Random p_180670_2_, BlockPos p_180670_3_, BlockState p_180670_4_) {
        return true;
    }

    public void performBonemeal(ServerLevel p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_, BlockState p_225535_4_) {
        p_225535_1_.setBlock(p_225535_3_, p_225535_4_.setValue(AGE, Integer.valueOf(p_225535_4_.getValue(AGE) + 1)), 2);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(AGE);
    }

    public boolean isPathfindable(BlockState p_196266_1_, BlockGetter p_196266_2_, BlockPos p_196266_3_, PathComputationType p_196266_4_) {
        return false;
    }

    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}
