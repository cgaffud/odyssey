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
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class FogFeature extends Feature<NoFeatureConfig> {

    public FogFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoFeatureConfig noFeatureConfig) {
//        while(!(seedReader.canSeeSky(pos) && seedReader.isEmptyBlock(pos))) {
//            pos = pos.above();
//        }
//
//        if (isLeaf(seedReader.getBlockState(pos.below()))) {
//            pos = pos.below().below();
//            while(seedReader.isEmptyBlock(pos) || isLeaf(seedReader.getBlockState(pos))) {
//                pos = pos.below();
//            }
//            pos.above();
//        }

        for (int j = 0; j < 7; j++) {
            BlockState blockstate = seedReader.getBlockState(pos);
            if(blockstate.isAir() && !isLeaf(seedReader.getBlockState(pos)))
                this.setBlock(seedReader, pos, BlockRegistry.FOG3.get().defaultBlockState());
            pos = pos.above();
        }
        return true;
    }

    private boolean isLeaf(BlockState blockstate){
        return (blockstate.equals(BlockRegistry.AUTUMN_LEAVES_RED.get().defaultBlockState()) ||
                blockstate.equals(BlockRegistry.AUTUMN_LEAVES_ORANGE.get().defaultBlockState()) ||
                blockstate.equals(BlockRegistry.AUTUMN_LEAVES_YELLOW.get().defaultBlockState()) ||
                blockstate.equals(Blocks.BIRCH_LEAVES.defaultBlockState()));
    }
}
