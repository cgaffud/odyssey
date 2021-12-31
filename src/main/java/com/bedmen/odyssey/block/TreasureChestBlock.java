package com.bedmen.odyssey.block;

import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.items.KeyItem;
import com.bedmen.odyssey.loot.TreasureChestMaterial;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class TreasureChestBlock extends AbstractChestBlock<TreasureChestBlockEntity> implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
    protected final TreasureChestMaterial chestMaterial;

    public TreasureChestBlock(TreasureChestMaterial chestMaterial, BlockBehaviour.Properties properties) {
        super(properties, () -> {
            return chestMaterial.getBlockEntityType();
        });
        this.chestMaterial = chestMaterial;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE).setValue(LOCKED, false));
    }

    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState p_53149_, Level p_53150_, BlockPos p_53151_, boolean p_53152_) {
        return DoubleBlockCombiner.Combiner::acceptNone;
    }

    public VoxelShape getShape(BlockState p_53171_, BlockGetter p_53172_, BlockPos p_53173_, CollisionContext p_53174_) {
        return SHAPE;
    }

    public RenderShape getRenderShape(BlockState p_53169_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_53128_) {
        FluidState fluidstate = p_53128_.getLevel().getFluidState(p_53128_.getClickedPos());
        return this.defaultBlockState().setValue(FACING, p_53128_.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if(blockState.getValue(LOCKED)){
            ItemStack itemStack = player.getItemInHand(hand);
            if(itemStack.getItem() instanceof KeyItem keyItem && keyItem.getChestMaterial() == this.chestMaterial){
                level.setBlock(blockPos, blockState.setValue(LOCKED, Boolean.FALSE), 3);
                level.playSound(player, blockPos, SoundEventRegistry.KEY_UNLOCK.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                if(!player.getAbilities().instabuild){
                    itemStack.shrink(1);
                }
            }
        }
        if(!blockState.getValue(LOCKED)){
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                MenuProvider menuprovider = this.getMenuProvider(blockState, level, blockPos);
                if (menuprovider != null) {
                    player.openMenu(menuprovider);
                    player.awardStat(this.chestMaterial.getStat());
                    PiglinAi.angerNearbyPiglins(player, true);
                }

                return InteractionResult.CONSUME;
            }
        } else {
            level.playSound(player, blockPos, SoundEventRegistry.LOCKED_CHEST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.FAIL;
        }
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TreasureChestBlockEntity(this.chestMaterial, blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153199_, BlockState p_153200_, BlockEntityType<T> p_153201_) {
        return p_153199_.isClientSide ? createTickerHelper(p_153201_, this.chestMaterial.getBlockEntityType(), TreasureChestBlockEntity::lidAnimateTick) : null;
    }

    public BlockState rotate(BlockState p_53157_, Rotation p_53158_) {
        return p_53157_.setValue(FACING, p_53158_.rotate(p_53157_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_53154_, Mirror p_53155_) {
        return p_53154_.rotate(p_53155_.getRotation(p_53154_.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53167_) {
        p_53167_.add(FACING, WATERLOGGED, LOCKED);
    }

    public FluidState getFluidState(BlockState p_53177_) {
        return p_53177_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_53177_);
    }

    public BlockState updateShape(BlockState p_53160_, Direction p_53161_, BlockState p_53162_, LevelAccessor p_53163_, BlockPos p_53164_, BlockPos p_53165_) {
        if (p_53160_.getValue(WATERLOGGED)) {
            p_53163_.scheduleTick(p_53164_, Fluids.WATER, Fluids.WATER.getTickDelay(p_53163_));
        }

        return super.updateShape(p_53160_, p_53161_, p_53162_, p_53163_, p_53164_, p_53165_);
    }

    public boolean isPathfindable(BlockState p_53132_, BlockGetter p_53133_, BlockPos p_53134_, PathComputationType p_53135_) {
        return false;
    }

    public void tick(BlockState p_153203_, ServerLevel p_153204_, BlockPos p_153205_, Random p_153206_) {
        BlockEntity blockentity = p_153204_.getBlockEntity(p_153205_);
        if (blockentity instanceof TreasureChestBlockEntity) {
            ((TreasureChestBlockEntity)blockentity).recheckOpen();
        }
    }

    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean flag) {
        if (!blockState.is(blockState1.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof Container && !blockState.getValue(LOCKED)) {
                Containers.dropContents(level, blockPos, (Container)blockEntity);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }

            super.onRemove(blockState, level, blockPos, blockState1, flag);
        }
    }
}