package com.bedmen.odyssey.block.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Locale;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class GreatSaplingBlock extends TransparentSaplingBlock {
    public static final EnumProperty<Status> STATUS = EnumProperty.create("status", Status.class);
    public static final IntegerProperty AGE_2 = BlockStateProperties.AGE_2;
    public GreatSaplingBlock(AbstractTreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(STATUS, Status.GOOD).setValue(AGE_2, 0));
    }

    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (blockState.getValue(STATUS).isGood() && randomSource.nextInt(7) == 0 && serverLevel.getMaxLocalRawBrightness(blockPos.above()) >= 9) {
            if(blockState.getValue(AGE_2) + blockState.getValue(STAGE) >= 3) {
                this.advanceTree(serverLevel, blockPos, blockState, serverLevel.random);
            } else {
                serverLevel.setBlock(blockPos, blockState.setValue(STATUS, Status.randomBadStatus(randomSource)), 3);
            }
        }
    }

    public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean isClientSide) {
        return false;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STATUS, AGE_2);
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        Status status = blockState.getValue(STATUS);
        if (!status.isGood()) {
            double d0 = randomSource.nextDouble();
            double d1 = randomSource.nextDouble();
            double d2 = randomSource.nextDouble();
            level.addParticle(status.particle, (double)blockPos.getX() + d0, (double)blockPos.getY() + d1, (double)blockPos.getZ() + d2, 0.0D, 0.0D, 0.0D);
        }
    }

    public void advanceAge(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState){
        int age = blockState.getValue(AGE_2);
        BlockState newBlockState = blockState.cycle(AGE_2).setValue(STATUS, Status.GOOD);
        serverLevel.setBlock(blockPos, newBlockState, 11);
        if (age >= 2) {
            this.advanceTree(serverLevel, blockPos, newBlockState, serverLevel.random);
        }
    }

    public static void greenParticles(Level level, BlockPos blockPos) {
        for(int i = 0; i < 20; i++){
            double d0 = level.random.nextDouble();
            double d1 = level.random.nextDouble();
            double d2 = level.random.nextDouble();
            level.addParticle(ParticleTypes.HAPPY_VILLAGER, (double)blockPos.getX() + d0, (double)blockPos.getY() + d1, (double)blockPos.getZ() + d2, 0.0D, 0.0D, 0.0D);
        }
    }

    public enum Status implements StringRepresentable {
        GOOD(null),
        THIRSTY(ParticleTypes.SMOKE),
        HUNGRY(ParticleTypes.DAMAGE_INDICATOR);
        //WEEDY; TODO Implement this after overgrowth

        public final ParticleOptions particle;

        Status(ParticleOptions particle) {
            this.particle = particle;
        }

        public String toString() {
            return this.getSerializedName();
        }

        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public boolean isGood(){
            return this == GOOD;
        }

        public static Status randomBadStatus(RandomSource randomSource){
            return values()[randomSource.nextInt(values().length-1)+1];
        }
    }
}
