package com.bedmen.odyssey.world.gen.feature;

import com.bedmen.odyssey.tags.OdysseyBlockTags;
import com.bedmen.odyssey.util.BlockRegistry;
import com.bedmen.odyssey.util.FogUtil;
import com.bedmen.odyssey.world.gen.OdysseyBiomeMaker;
import com.mojang.serialization.Codec;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import org.lwjgl.system.CallbackI;

public class FogFeature extends Feature<NoFeatureConfig> {
    public FogFeature(Codec<NoFeatureConfig> p_i231993_1_) {
        super(p_i231993_1_);
    }

    public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random p_241855_3_, BlockPos blockPos, NoFeatureConfig p_241855_5_) {
        int x = blockPos.getX();
        int z = blockPos.getZ();

        for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
                int k = x + i;
                int l = z + j;
                BlockPos pos =  new BlockPos(k, 0, l);

                pos = pos.above(63);
//                while(!seedReader.isEmptyBlock(pos) && (pos.getY() < 80) && !(ignoreBlock(seedReader.getBlockState(pos)))) {
//                    pos = pos.above();
//                }
                if (shouldFog(seedReader, pos)) {
                    buildFog(seedReader, pos);
                }
            }
        }
        return true;
    }

    public void buildFog(ISeedReader seedReader, BlockPos pos){
        for(int x = -5; x <= 5; x++) {
            for(int z = -5; z <= 5; z++) {
                for(int y = 0; y <= 1; y++) {
                    int fogNum = 6 - 2*(Math.abs(x)/2 + Math.abs(y) + Math.abs(z)/2);
                    if(fogNum >= 1){
                        for(int k = 0; k <= 3; k++) {
                            BlockPos pos1 = pos.offset(x,y*4+k-Math.abs(x)-Math.abs(z),z);
                            if(fogNum > FogUtil.fogToInt(seedReader.getBlockState(pos1).getBlock())){
                                Block block = FogUtil.intToFog(fogNum);
                                seedReader.setBlock(pos1, block.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean shouldFog(IWorldReader worldReader, BlockPos blockPos) {
        return worldReader.getBiome(blockPos).getRegistryName().toString().equals("oddc:autumn_forest");
    }

//    private boolean ignoreBlock(BlockState blockstate){
//        return (blockstate.is(Blocks.BIRCH_LOG));
//    }

}