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
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;

public class AlloyFurnaceBlock extends AbstractFurnaceBlock {
    public AlloyFurnaceBlock(BlockBehaviour.Properties p_49773_) {
        super(p_49773_);
    }

    public BlockEntity newBlockEntity(BlockPos p_152386_, BlockState p_152387_) {
        return new AlloyFurnaceBlockEntity(p_152386_, p_152387_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createAlloyFurnaceTicker(level, blockEntityType, BlockEntityTypeRegistry.ALLOY_FURNACE.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createAlloyFurnaceTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends AlloyFurnaceBlockEntity> blockEntityType2) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, blockEntityType2, AlloyFurnaceBlockEntity::serverTick);
    }

    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityType<E> blockEntityType2, BlockEntityTicker<? super E> blockEntityTicker) {
        return blockEntityType == blockEntityType2 ? (BlockEntityTicker<A>)blockEntityTicker : null;
    }

    protected void openContainer(Level p_49777_, BlockPos p_49778_, Player p_49779_) {
        BlockEntity blockentity = p_49777_.getBlockEntity(p_49778_);
        if (blockentity instanceof AlloyFurnaceBlockEntity) {
            p_49779_.openMenu((MenuProvider)blockentity);
            p_49779_.awardStat(OdysseyStats.INTERACT_WITH_ALLOY_FURNACE);
        }

    }

    public void animateTick(BlockState p_49781_, Level p_49782_, BlockPos p_49783_, Random p_49784_) {
        if (p_49781_.getValue(LIT)) {
            double d0 = (double)p_49783_.getX() + 0.5D;
            double d1 = (double)p_49783_.getY();
            double d2 = (double)p_49783_.getZ() + 0.5D;
            if (p_49784_.nextDouble() < 0.1D) {
                p_49782_.playLocalSound(d0, d1, d2, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = p_49781_.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = p_49784_.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
            double d6 = p_49784_.nextDouble() * 9.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
            p_49782_.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    public void onRemove(BlockState p_48713_, Level p_48714_, BlockPos p_48715_, BlockState p_48716_, boolean p_48717_) {
        if (!p_48713_.is(p_48716_.getBlock())) {
            BlockEntity blockentity = p_48714_.getBlockEntity(p_48715_);
            if (blockentity instanceof AlloyFurnaceBlockEntity alloyFurnaceBlockEntity) {
                if (p_48714_ instanceof ServerLevel) {
                    Containers.dropContents(p_48714_, p_48715_, alloyFurnaceBlockEntity);
                    alloyFurnaceBlockEntity.getRecipesToAwardAndPopExperience((ServerLevel)p_48714_, Vec3.atCenterOf(p_48715_));
                }

                p_48714_.updateNeighbourForOutputSignal(p_48715_, this);
            }

            super.onRemove(p_48713_, p_48714_, p_48715_, p_48716_, p_48717_);
        }
    }
}