package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.util.BlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class FogFeature extends Feature<NoFeatureConfig> {

    public FogFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoFeatureConfig noFeatureConfig) {

        // Search for ground
        while(!isGround(seedReader.getBlockState(pos)))
            pos = pos.below();
        pos.above();

        for (int j = 0; j < 7; j++) {
            BlockState blockstate = seedReader.getBlockState(pos);
            if(blockstate.isAir())
                this.setBlock(seedReader, pos, BlockRegistry.FOG5.get().defaultBlockState());
            pos = pos.above();
        }
        return true;
    }

    private boolean isGround(BlockState blockstate){
        return (blockstate.is(Blocks.GRASS_BLOCK) ||
                blockstate.is(Blocks.WATER) ||
                blockstate.is(Blocks.DIRT) ||
                blockstate.is(Blocks.STONE));
    }


}
