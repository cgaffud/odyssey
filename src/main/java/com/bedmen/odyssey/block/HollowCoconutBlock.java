package com.bedmen.odyssey.block;

import com.bedmen.odyssey.block.entity.HollowCoconutBlockEntity;
import com.bedmen.odyssey.entity.item.FallingHollowCoconut;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.registry.ItemRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class HollowCoconutBlock extends FallingBlock implements EntityBlock, INeedsToRegisterRenderType {
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final VoxelShape SHAPE_FLOOR = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
    public static final VoxelShape SHAPE_HANGING = Block.box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D);

    public HollowCoconutBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, Boolean.FALSE));
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createCoconutTicker(level, blockEntityType, BlockEntityTypeRegistry.HOLLOW_COCONUT.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createCoconutTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends HollowCoconutBlockEntity> blockEntityType2) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, blockEntityType2, HollowCoconutBlockEntity::serverTick);
    }

    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityType<E> blockEntityType2, BlockEntityTicker<? super E> blockEntityTicker) {
        return blockEntityType == blockEntityType2 ? (BlockEntityTicker<A>)blockEntityTicker : null;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HANGING, context.getClickedFace() == Direction.DOWN);
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext selectionContext) {
        return blockState.getValue(HANGING) ? SHAPE_HANGING : SHAPE_FLOOR;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(HANGING);
    }

    public void tick(BlockState blockState, ServerLevel serverWorld, BlockPos blockPos, Random random) {
        if(blockState.getValue(HANGING)){
            if (serverWorld.isEmptyBlock(blockPos.above()) || isFree(serverWorld.getBlockState(blockPos.above())) && blockPos.getY() >= 0) {
                FallingBlockEntity fallingblockentity = new FallingHollowCoconut(serverWorld, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, serverWorld.getBlockState(blockPos).setValue(HANGING, Boolean.FALSE));
                this.falling(fallingblockentity);
                serverWorld.addFreshEntity(fallingblockentity);
            }
        }
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
    }

    public int getDustColor(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 0x000000;
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
    {
        return ItemRegistry.HOLLOW_COCONUT.get().getDefaultInstance();
    }

    public Item asItem() {
        return ItemRegistry.HOLLOW_COCONUT.get();
    }

    public RenderType getRenderType() {
        return RenderType.cutout();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new HollowCoconutBlockEntity(blockPos, blockState);
    }
}
