package com.bedmen.odyssey.block.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.Locale;
import java.util.Random;

public class GreatSaplingBlock extends TransparentSaplingBlock {
    public static final EnumProperty<Status> STATUS = EnumProperty.create("status", Status.class);
    public GreatSaplingBlock(AbstractTreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(STATUS, Status.GOOD));
    }

    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if (random.nextInt(2) == 0) {
            serverLevel.setBlock(blockPos, blockState.setValue(STATUS, Status.THIRSTY), 3);
            return;
        }
        if (blockState.getValue(STATUS).isGood() && serverLevel.getMaxLocalRawBrightness(blockPos.above()) >= 9 && random.nextInt(70) == 0) {
            if (!serverLevel.isAreaLoaded(blockPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            this.advanceTree(serverLevel, blockPos, blockState, random);
        }
    }

    public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean isClientSide) {
        return false;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STATUS);
    }

    public enum Status implements StringRepresentable {
        GOOD,
        THIRSTY;
        //HUNGRY,
        //WEEDY;

        public String toString() {
            return this.getSerializedName();
        }

        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public boolean isGood(){
            return this == GOOD;
        }
    }
}
