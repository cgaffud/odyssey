package com.bedmen.odyssey.block;

import com.bedmen.odyssey.block.entity.AbstractInfusionPedestalBlockEntity;
import com.bedmen.odyssey.block.entity.InfusionPedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InfusionPedestalBlock extends BaseEntityBlock {

    protected static final VoxelShape BASE_1 = Block.box(1.0d, 0.0d, 1.0d, 15.0d, 2.0d, 15.0d);
    protected static final VoxelShape BASE_2 = Block.box(3.0d, 2.0d, 3.0d, 13.0d, 3.0d, 13.0d);
    protected static final VoxelShape CENTER = Block.box(6.0d, 3.0d, 6.0d, 10.0d, 13.0d, 10.0d);
    protected static final VoxelShape WEST_CENTER_PIECE = Block.box(5.0d, 3.0d, 7.0d, 6.0d, 10.0d, 9.0d);
    protected static final VoxelShape EAST_CENTER_PIECE = Block.box(10.0d, 3.0d, 7.0d, 11.0d, 10.0d, 9.0d);
    protected static final VoxelShape NORTH_CENTER_PIECE = Block.box(7.0d, 3.0d, 10.0d, 9.0d, 10.0d, 11.0d);
    protected static final VoxelShape SOUTH_CENTER_PIECE = Block.box(7.0d, 3.0d, 5.0d, 9.0d, 10.0d, 6.0d);
    protected static final VoxelShape TOP = Block.box(3.0d, 13.0d, 3.0d, 13.0d, 15.0d, 13.0d);
    protected static final VoxelShape SOUTH_TOP_PIECE = Block.box(1.0d, 11.0d, 1.0d, 15.0d, 15.0d, 3.0d);
    protected static final VoxelShape NORTH_TOP_PIECE = Block.box(1.0d, 11.0d, 13.0d, 15.0d, 15.0d, 15.0d);
    protected static final VoxelShape WEST_TOP_PIECE = Block.box(1.0d, 11.0d, 3.0d, 3.0d, 15.0d, 13.0d);
    protected static final VoxelShape EAST_TOP_PIECE = Block.box(13.0d, 11.0d, 3.0d, 15.0d, 15.0d, 13.0d);
    protected static final VoxelShape VOXEL_SHAPE = Shapes.or(BASE_1, BASE_2, CENTER, WEST_CENTER_PIECE, EAST_CENTER_PIECE, NORTH_CENTER_PIECE, SOUTH_CENTER_PIECE, TOP, SOUTH_TOP_PIECE, NORTH_TOP_PIECE, WEST_TOP_PIECE, EAST_TOP_PIECE);

    public InfusionPedestalBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VOXEL_SHAPE;
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfusionPedestalBlockEntity(blockPos, blockState);
    }

    public void onRemove(BlockState state, Level level, BlockPos blockPos, BlockState blockState, boolean b) {
        if (!state.is(blockState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(blockPos);
            if (blockentity instanceof AbstractInfusionPedestalBlockEntity blockEntity) {
                if (level instanceof ServerLevel) {
                    Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockEntity.getItemStackCopy());
                }
                level.updateNeighbourForOutputSignal(blockPos, this);
            }
            super.onRemove(state, level, blockPos, blockState, b);
        }
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockentity = level.getBlockEntity(blockPos);
        if (blockentity instanceof AbstractInfusionPedestalBlockEntity pedestalBlockEntity) {
            ItemStack handItemStack = player.getItemInHand(interactionHand);
            // Don't give pedestalItemStack to player since it is not a copy
            ItemStack pedestalItemStack = pedestalBlockEntity.getItemStackOriginal();
            boolean handEmpty = handItemStack.isEmpty();
            boolean pedestalEmpty = pedestalItemStack.isEmpty();
            boolean addToPedestal = !handEmpty && ItemStack.isSameItemSameTags(handItemStack, pedestalItemStack);
            boolean placeOnEmptyPedestal = !handEmpty && pedestalEmpty;
            boolean putOnPedestal = addToPedestal || placeOnEmptyPedestal;
            boolean takeFromPedestal = handEmpty && !pedestalEmpty;
            boolean interactWithPedestal = putOnPedestal || takeFromPedestal;
            if (level.isClientSide) {
                return interactWithPedestal ? InteractionResult.SUCCESS : InteractionResult.PASS;
            } else {
                if(interactWithPedestal){
                    if(putOnPedestal){
                        if(addToPedestal){
                            int countToMaxStack = pedestalItemStack.getMaxStackSize() - pedestalItemStack.getCount();
                            int countToAdd = Integer.min(countToMaxStack, handItemStack.getCount());
                            handItemStack.shrink(countToAdd);
                            pedestalBlockEntity.growItemStack(countToAdd);
                        } else {
                            player.setItemInHand(interactionHand, ItemStack.EMPTY);
                            pedestalBlockEntity.setItemStack(handItemStack.copy());
                            pedestalBlockEntity.itemRenderDirection = Direction.fromYRot(player.getYHeadRot());
                        }
                    } else {
                        player.setItemInHand(interactionHand, pedestalBlockEntity.getItemStackCopy());
                        pedestalBlockEntity.setItemStack(ItemStack.EMPTY);
                    }
                    pedestalBlockEntity.playerUUID = player.getUUID();
                    return InteractionResult.CONSUME;
                } else {
                    return InteractionResult.FAIL;
                }
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        BlockEntity blockentity = level.getBlockEntity(blockPos);
        if (blockentity instanceof AbstractInfusionPedestalBlockEntity blockEntity) {
            return blockEntity.getItemStackOriginal().isEmpty() ? 0 : 15;
        }
        return 0;
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
