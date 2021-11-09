package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.blocks.AbandonedIronGolemBlock;
import com.bedmen.odyssey.registry.BlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class AbandonedIronGolemFeature extends Feature<NoFeatureConfig> {
    public AbandonedIronGolemFeature(Codec<NoFeatureConfig> p_i231962_1_) {
        super(p_i231962_1_);
    }

    public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoFeatureConfig noFeatureConfig) {

        if(random.nextInt(100) != 0)
            return false;
        while(seedReader.isEmptyBlock(pos) && pos.getY() > 2) {
            pos = pos.below();
        }
        if(!(seedReader.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK || seedReader.getBlockState(pos).getBlock() == Blocks.SAND))
            return false;
        Direction direction;
        do {
            direction = Direction.getRandom(random);
        } while(direction.getAxis() == Direction.Axis.Y);

        BlockPos.Mutable mutable = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
        mutable = mutable.move(Direction.UP);
        this.setBlock(seedReader, mutable, BlockRegistry.ABANDONED_IRON_GOLEM.get().defaultBlockState().setValue(AbandonedIronGolemBlock.FACING, direction).setValue(AbandonedIronGolemBlock.HALF, DoubleBlockHalf.LOWER));
        mutable = mutable.move(Direction.UP);
        this.setBlock(seedReader, mutable, BlockRegistry.ABANDONED_IRON_GOLEM.get().defaultBlockState().setValue(AbandonedIronGolemBlock.FACING, direction).setValue(AbandonedIronGolemBlock.HALF, DoubleBlockHalf.UPPER));
        return true;
    }
}