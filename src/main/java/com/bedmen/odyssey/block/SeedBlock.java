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
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class SeedBlock extends Block implements INeedsToRegisterRenderType {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    protected static final VoxelShape[] SHAPE = new VoxelShape[]{Block.box(6.0D, 10.0D, 6.0D, 10.0D, 14.0D, 10.0D), Block.box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D), Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D), Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D)};

    public SeedBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 1));
    }

    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(AGE) > 0;
    }

    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        int i = blockState.getValue(AGE);
        if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(serverLevel, blockPos, blockState, serverLevel.random.nextInt(5) == 0)){
            BlockState sapling = BlockRegistry.GREATWOOD_SAPLING.get().defaultBlockState();
            if (i < 3) {
                serverLevel.setBlock(blockPos, blockState.setValue(AGE, i + 1), 2);
            } else if(BlockRegistry.GREATWOOD_SAPLING.get().canSurvive(sapling, serverLevel, blockPos)) {
                serverLevel.setBlock(blockPos, sapling, 2);
            }
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(serverLevel, blockPos, blockState);
        }
    }

    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return BlockRegistry.GREATWOOD_SAPLING.get().canSurvive(blockState, levelReader, blockPos);
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE[blockState.getValue(AGE)];
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
