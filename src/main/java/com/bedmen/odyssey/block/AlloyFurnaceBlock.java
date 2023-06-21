package com.bedmen.odyssey.block;

import com.bedmen.odyssey.block.entity.AlloyFurnaceBlockEntity;
import com.bedmen.odyssey.registry.BlockEntityTypeRegistry;
import com.bedmen.odyssey.util.OdysseyStats;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;

public class AlloyFurnaceBlock extends AbstractFurnaceBlock {
    public AlloyFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AlloyFurnaceBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createAlloyFurnaceTicker(level, blockEntityType, BlockEntityTypeRegistry.ALLOY_FURNACE.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createAlloyFurnaceTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends AlloyFurnaceBlockEntity> blockEntityType2) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, blockEntityType2, AlloyFurnaceBlockEntity::serverTick);
    }

    protected void openContainer(Level level, BlockPos blockPos, Player player) {
        BlockEntity blockentity = level.getBlockEntity(blockPos);
        if (blockentity instanceof AlloyFurnaceBlockEntity) {
            player.openMenu((MenuProvider)blockentity);
            player.awardStat(OdysseyStats.INTERACT_WITH_ALLOY_FURNACE);
        }

    }

    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource randomSource) {
        if (blockState.getValue(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (randomSource.nextDouble() < 0.1D) {
                level.playLocalSound(d0, d1, d2, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = blockState.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = randomSource.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
            double d6 = randomSource.nextDouble() * 9.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
            level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    public void onRemove(BlockState state, Level level, BlockPos blockPos, BlockState blockState, boolean b) {
        if (!state.is(blockState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(blockPos);
            if (blockentity instanceof AlloyFurnaceBlockEntity alloyFurnaceBlockEntity) {
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, blockPos, alloyFurnaceBlockEntity);
                    alloyFurnaceBlockEntity.getRecipesToAwardAndPopExperience((ServerLevel)level, Vec3.atCenterOf(blockPos));
                }

                level.updateNeighbourForOutputSignal(blockPos, this);
            }

            super.onRemove(state, level, blockPos, blockState, b);
        }
    }
}