package com.bedmen.odyssey.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class GreatSeedBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(6.0D, 10.0D, 6.0D, 10.0D, 14.0D, 10.0D);
    protected final Lazy<Block> log;

    public GreatSeedBlock(Properties properties, Lazy<Block> log) {
        super(properties);
        this.log = log;
    }

    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos.above()).is((this.log.get()));
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        return direction == Direction.UP && !blockState.canSurvive(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState1, levelAccessor, blockPos, blockPos1);
    }

    public boolean isPathfindable(BlockState p_196266_1_, BlockGetter p_196266_2_, BlockPos p_196266_3_, PathComputationType p_196266_4_) {
        return false;
    }

    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootContextBuilder) {
        ItemStack itemStack = lootContextBuilder.getOptionalParameter(LootContextParams.TOOL);
        if (itemStack != null && itemStack.getItem() instanceof AxeItem) {
            return super.getDrops(blockState, lootContextBuilder);
        }
        return List.of();
    }
}
