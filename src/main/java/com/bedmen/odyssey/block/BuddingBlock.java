package com.bedmen.odyssey.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class BuddingBlock extends Block {

    public final int GROWTH_CHANCE;
    private static final Direction[] DIRECTIONS = Direction.values();
    private final List<RegistryObject<Block>> BUDDING_STAGES;
    private final boolean onlyVertical;

    public BuddingBlock(Properties p_49795_, int growthChance, boolean onlyVertical, List<RegistryObject<Block>> stages) {
        super(p_49795_);
        BUDDING_STAGES = stages;
        GROWTH_CHANCE = growthChance;
        this.onlyVertical = onlyVertical;
    }
    public PushReaction getPistonPushReaction(BlockState p_152733_) {
        return PushReaction.DESTROY;
    }

    public void randomTick(BlockState p_220898_, ServerLevel p_220899_, BlockPos p_220900_, RandomSource p_220901_) {
        if (p_220901_.nextInt(GROWTH_CHANCE) == 0) {
            Direction direction = onlyVertical ? DIRECTIONS[p_220901_.nextInt(2)] : DIRECTIONS[p_220901_.nextInt(DIRECTIONS.length)];
            BlockPos blockpos = p_220900_.relative(direction);
            BlockState blockstate = p_220899_.getBlockState(blockpos);
            Block block = null;
            if (canClusterGrowAtState(blockstate)) {
                block = BUDDING_STAGES.get(0).get();
            } else {
                for (int i = 0; i < BUDDING_STAGES.size()-1; i++) {
                    if (blockstate.is(BUDDING_STAGES.get(i).get()) && blockstate.getValue(ClusterBlock.FACING) == direction) {
                        block = BUDDING_STAGES.get(i+1).get();
                        break;
                    }
                }
            }

            if (block != null) {
                BlockState blockstate1 = block.defaultBlockState().setValue(ClusterBlock.FACING, direction).setValue(ClusterBlock.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
                p_220899_.setBlockAndUpdate(blockpos, blockstate1);
            }

        }
    }

    public static boolean canClusterGrowAtState(BlockState p_152735_) {
        return p_152735_.isAir() || p_152735_.is(Blocks.WATER) && p_152735_.getFluidState().getAmount() == 8;
    }
}
