package com.bedmen.odyssey.block;

import com.bedmen.odyssey.block.entity.TreasureChestBlockEntity;
import com.bedmen.odyssey.items.KeyItem;
import com.bedmen.odyssey.lock.TreasureChestType;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.SoundEventRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.animal.Cat;
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
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreasureChestBlock extends AbstractChestBlock<TreasureChestBlockEntity> implements SimpleWaterloggedBlock {
    private static final List<ItemStack> EMPTY_LIST = new ArrayList<>();
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
    public final TreasureChestType treasureChestType;

    public TreasureChestBlock(TreasureChestType chestMaterial, BlockBehaviour.Properties properties) {
        super(properties, BlockEntityTypeRegistry.TREASURE_CHEST::get);
        this.treasureChestType = chestMaterial;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE).setValue(LOCKED, false));
    }

    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState blockState, Level level, BlockPos blockPos, boolean b) {
        return DoubleBlockCombiner.Combiner::acceptNone;
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidstate = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if(blockState.getValue(LOCKED)){
            ItemStack itemStack = player.getItemInHand(hand);
            if(itemStack.getItem() instanceof KeyItem keyItem && keyItem.lockType == this.treasureChestType){
                level.setBlock(blockPos, blockState.setValue(LOCKED, Boolean.FALSE), 3);
                level.playSound(player, blockPos, SoundEventRegistry.KEY_UNLOCK.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                if(!player.getAbilities().instabuild){
                    itemStack.shrink(1);
                }
            } else {
                level.playSound(player, blockPos, SoundEventRegistry.LOCKED_CHEST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                return InteractionResult.FAIL;
            }
        }
        if(isChestBlockedAt(level, blockPos)){
            return InteractionResult.sidedSuccess(true);
        }
        if(!blockState.getValue(LOCKED)){
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                MenuProvider menuprovider = this.getMenuProvider(blockState, level, blockPos);
                if (menuprovider != null) {
                    player.openMenu(menuprovider);
                    player.awardStat(this.treasureChestType.stat);
                    PiglinAi.angerNearbyPiglins(player, true);
                }

                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.FAIL;
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TreasureChestBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? createTickerHelper(blockEntityType, BlockEntityTypeRegistry.TREASURE_CHEST.get(), TreasureChestBlockEntity::lidAnimateTick) : null;
    }

    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, LOCKED);
    }

    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if (blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }

        return super.updateShape(blockState, direction, blockState1, levelAccessor, blockPos, blockPos1);
    }

    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
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

    public static boolean isChestBlockedAt(LevelAccessor levelAccessor, BlockPos blockPos) {
        return isBlockedChestByBlock(levelAccessor, blockPos) || isCatSittingOnChest(levelAccessor, blockPos);
    }

    private static boolean isBlockedChestByBlock(BlockGetter blockGetter, BlockPos blockPos) {
        BlockPos blockpos = blockPos.above();
        return blockGetter.getBlockState(blockpos).isRedstoneConductor(blockGetter, blockpos);
    }

    private static boolean isCatSittingOnChest(LevelAccessor levelAccessor, BlockPos blockPos) {
        List<Cat> list = levelAccessor.getEntitiesOfClass(Cat.class, new AABB((double)blockPos.getX(), (double)(blockPos.getY() + 1), (double)blockPos.getZ(), (double)(blockPos.getX() + 1), (double)(blockPos.getY() + 2), (double)(blockPos.getZ() + 1)));
        if (!list.isEmpty()) {
            for(Cat cat : list) {
                if (cat.isInSittingPose()) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        if(blockState.getValue(LOCKED)){
            return EMPTY_LIST;
        }
        return super.getDrops(blockState, builder);
    }

    public float getDestroyProgress(BlockState blockState, Player player, BlockGetter blockGetter, BlockPos blockPos) {
        return super.getDestroyProgress(blockState, player, blockGetter, blockPos) * (blockState.getValue(LOCKED) ? 0.1f : 1.0f);
    }
}